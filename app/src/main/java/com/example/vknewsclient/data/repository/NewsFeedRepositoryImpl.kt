package com.example.vknewsclient.data.repository

import android.app.Application
import android.util.Log
import com.example.vknewsclient.data.mapper.NewsFeedMapper
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.domain.entity.AuthState
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.entity.StatisticItem
import com.example.vknewsclient.domain.entity.StatisticType
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import com.example.vknewsclient.extensions.mergeWith
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepositoryImpl(application: Application) : NewsFeedRepository {

    private val token
        get() = VKID.instance.accessToken
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
//    }.retry {
//        delay(RETRY_TIMEOUT_MILLIS)
//        true
//    }

    private val loadedListFlowV2 = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val response = apiService.loadRecommendationsV2()
            val posts = mapper.mapFilmsToPosts(response)
            _feedPosts.addAll(posts)
            emit(posts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)

    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            Log.d("NewsFeedRepository", "Token ${token?.token}")
            val loggedIn = token != null
            val authState = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
            emit(authState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

//    private val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
//        .mergeWith(refreshedListFlow)
//        .stateIn(
//            scope = coroutineScope,
//            started = SharingStarted.Lazily,
//            initialValue = feedPosts
//        )

    private val recommendationsV2: StateFlow<List<FeedPost>> = loadedListFlowV2
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    override fun getAuthStateFlow(): StateFlow<AuthState> = authStateFlow

    override fun getRecommendations(): StateFlow<List<FeedPost>> = recommendationsV2

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun checkAuthState() {
        checkAuthStateEvents.emit(Unit)
    }

    private fun getAccessToken(): String {
        return token?.token ?: throw IllegalStateException("Token is null")
    }

//    override suspend fun deletePostV1(feedPost: FeedPost) {
//        apiService.ignorePost(
//            accessToken = getAccessToken(),
//            ownerId = feedPost.communityId,
//            postId = feedPost.id
//        )
//        _feedPosts.remove(feedPost)
//        refreshedListFlow.emit(feedPosts)
//    }

    override suspend fun deletePost(feedPost: FeedPost) {
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

//    override fun getCommentsV1(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
//        val comments = apiService.getComments(
//            accessToken = getAccessToken(),
//            ownerId = feedPost.communityId,
//            postId = feedPost.id
//        )
//        emit(mapper.mapResponseToComments(comments))
//    }.retry {
//        delay(RETRY_TIMEOUT_MILLIS)
//        true
//    }.stateIn(
//        scope = coroutineScope,
//        started = SharingStarted.Lazily,
//        initialValue = listOf()
//    )

    override fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
        val comments = apiService.getCommentsV2(id = feedPost.id)
        emit(mapper.mapResponseToCommentsV2(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

//    override suspend fun changeLikeStatusV1(feedPost: FeedPost) {
//        val response = if (feedPost.isLiked) {
//            apiService.deleteLike(
//                token = getAccessToken(),
//                ownerId = feedPost.communityId,
//                postId = feedPost.id
//            )
//        } else {
//            apiService.addLike(
//                token = getAccessToken(),
//                ownerId = feedPost.communityId,
//                postId = feedPost.id
//            )
//        }
//        val newLikesCount = response.likes.count
//        val newStatistics = feedPost.statistics.toMutableList().apply {
//            removeIf { it.type == StatisticType.LIKES }
//            add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
//        }
//        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
//        val postIndex = _feedPosts.indexOf(feedPost)
//        _feedPosts[postIndex] = newPost
//        refreshedListFlow.emit(feedPosts)
//    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
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

    companion object {

        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}