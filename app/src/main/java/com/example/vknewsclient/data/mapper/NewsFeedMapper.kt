package com.example.vknewsclient.data.mapper

import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.data.model.kinopoisk.FilmsListDto
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
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
                communityName = group.name,
                publicationDate = post.date.toString(),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, post.likes.count),
                    StatisticItem(type = StatisticType.VIEWS, post.views.count),
                    StatisticItem(type = StatisticType.SHARES, post.reposts.count),
                    StatisticItem(type = StatisticType.COMMENTS, post.comments.count)
                )
            )
            result.add(feedPost)
        }

        return result
    }

    fun mapFilmsToPosts(filmsDto: FilmsListDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = filmsDto.items

        for (post in posts) {
//            if (post.posterUrlPreview == null) break // Not add to result
            val feedPost = FeedPost(
                id = post.kinopoiskId.toString(),
                communityName = post.type.toString(),
                publicationDate = post.year.toString(),
                communityImageUrl = post.posterUrlPreview.toString(),
                contentText = post.nameRu.toString(),
                contentImageUrl = post.posterUrl.toString(),
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, post.ratingKinopoisk?.toInt() ?: 0),
                    StatisticItem(type = StatisticType.VIEWS, post.ratingKinopoisk?.toInt() ?: 0),
                    StatisticItem(type = StatisticType.SHARES, post.ratingKinopoisk?.toInt() ?: 0),
                    StatisticItem(type = StatisticType.COMMENTS, post.ratingKinopoisk?.toInt() ?: 0)
                )
            )
            result.add(feedPost)
        }

        return result
    }
}