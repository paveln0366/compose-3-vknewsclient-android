package com.example.vknewsclient.data.repository

import android.app.Application
import com.example.vknewsclient.data.mapper.NewsFeedMapper
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import com.example.vknewsclient.extensions.mergeWith
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepository(application: Application) {

    private val token = VKID.instance.accessToken
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()
//    private val loadedListFlow = flow {
//        nextDataNeededEvents.emit(Unit)
//        nextDataNeededEvents.collect {
//            val startFrom = nextFrom
//
//            if (startFrom == null && feedPosts.isNotEmpty()) {
//                emit(feedPosts)
//                return@collect
//            }
//
//            val response = if (startFrom == null) {
//                apiService.loadRecommendations(getAccessToken())
//            } else {
//                apiService.loadRecommendations(getAccessToken(), startFrom)
//            }
//            nextFrom = response.newsFeedContent.nextFrom
//            val posts = mapper.mapResponseToPosts(response)
//            _feedPosts.addAll(posts)
//            emit(feedPosts)
//        }
//    }

    private val loadedListFlowV2 = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val response = apiService.loadRecommendationsV2()
            val posts = mapper.mapFilmsToPosts(response)
            _feedPosts.addAll(posts)
            emit(posts)
        }
    }

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

//    val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
//        .mergeWith(refreshedListFlow)
//        .stateIn(
//            scope = coroutineScope,
//            started = SharingStarted.Lazily,
//            initialValue = feedPosts
//        )

    val recommendationsV2: StateFlow<List<FeedPost>> = loadedListFlowV2
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    private fun getAccessToken(): String {
        return token?.token ?: throw IllegalStateException("Token is null")
    }

    suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            accessToken = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun deletePostV2(feedPost: FeedPost) {
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun getComments(feedPost: FeedPost): List<PostComment> {
        val comments = apiService.getComments(
            accessToken = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        return mapper.mapResponseToComments(comments)
    }

    suspend fun getCommentsV2(feedPost: FeedPost): List<PostComment> {
        val comments = apiService.getCommentsV2(id = feedPost.id)
        return mapper.mapResponseToCommentsV2(comments)
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun changeLikeStatusV2(feedPost: FeedPost) {
        val newLikesCount = if (feedPost.isLiked) 0 else 1
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)
    }
}