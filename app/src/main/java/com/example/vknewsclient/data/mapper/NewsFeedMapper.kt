package com.example.vknewsclient.data.mapper

import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.data.model.kinopoisk.FilmsListDto
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date * 1000),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, post.likes.count),
                    StatisticItem(type = StatisticType.VIEWS, post.views.count),
                    StatisticItem(type = StatisticType.SHARES, post.reposts.count),
                    StatisticItem(type = StatisticType.COMMENTS, post.comments.count)
                ),
                isLiked = post.likes.userLikes > 0
            )
            result.add(feedPost)
        }

        return result
    }

    fun mapFilmsToPosts(filmsDto: FilmsListDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = filmsDto.items

        for (post in posts) {
            if (post.type == "VIDEO") continue
            val feedPost = FeedPost(
                id = post.kinopoiskId,
                communityId = post.kinopoiskId,
                communityName = post.type.toString(),
                publicationDate = mapTimestampToDate(1764074687392),
                communityImageUrl = post.posterUrlPreview.toString(),
                contentText = post.nameRu.toString(),
                contentImageUrl = post.posterUrl.toString(),
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, (0..300000).random()),
                    StatisticItem(type = StatisticType.VIEWS, (0..300000).random()),
                    StatisticItem(type = StatisticType.SHARES, (0..300000).random()),
                    StatisticItem(type = StatisticType.COMMENTS, (0..300000).random())
                ),
                isLiked = false
            )
            result.add(feedPost)
        }

        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}