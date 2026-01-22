package com.example.vknewsclient.data.mapper

import com.example.vknewsclient.data.model.CommentsResponseDto
import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.data.model.kinopoisk.FilmsListDto
import com.example.vknewsclient.data.model.kinopoisk.ReviewResponseDto
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.entity.StatisticItem
import com.example.vknewsclient.domain.entity.StatisticType
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
                publicationDate = mapTimestampToDate(post.date),
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

    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val comments = response.content.comments
        val profiles = response.content.profiles
        for (comment in comments) {
            if (comment.text.isBlank()) continue
            val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date),
            )
            result.add(postComment)
        }
        return result
    }

    fun mapResponseToCommentsV2(response: ReviewResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val comments = response.items
        for (comment in comments) {
            val postComment = PostComment(
                id = comment.kinopoiskId,
                authorName = comment.author,
                authorAvatarUrl = "https://kinopoiskapiunofficial.tech/images/posters/kp_small/8420095.jpg",
                commentText = "${comment.description.take(50)}.",
                publicationDate = comment.date,
            )
            result.add(postComment)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}