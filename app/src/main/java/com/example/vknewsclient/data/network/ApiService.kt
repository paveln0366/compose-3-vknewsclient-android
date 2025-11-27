package com.example.vknewsclient.data.network

import com.example.vknewsclient.data.model.CommentsResponseDto
import com.example.vknewsclient.data.model.LikesCountResponseDto
import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.data.model.kinopoisk.FilmsListDto
import com.example.vknewsclient.data.model.kinopoisk.ReviewResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadRecommendations(
        @Query("access_token") token: String
    ): NewsFeedResponseDto

    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadRecommendations(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String
    ): NewsFeedResponseDto

    @GET("api/v2.2/films")
    @Headers(
        "x-api-key: ccecf0d7-52ed-4c7e-b53a-0818d4a0c09c",
        "Content-Type: application/json",
    )
    suspend fun loadRecommendationsV2(): FilmsListDto

    @GET("likes.add?v=5.131&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long
    ): LikesCountResponseDto

    @GET("likes.delete?v=5.131&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long
    ): LikesCountResponseDto

    @GET("newsfeed.ignoreItem?v=5.131&type=wall")
    suspend fun ignorePost(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long
    )

    @GET("wall.getComments?v=5.131&extended=1&fields=photo_100")
    suspend fun getComments(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long
    ): CommentsResponseDto

    @GET("api/v2.2/films/{id}/reviews")
    @Headers(
        "x-api-key: ccecf0d7-52ed-4c7e-b53a-0818d4a0c09c",
        "Content-Type: application/json",
    )
    suspend fun getCommentsV2(
        @Path("id") id: Long
    ): ReviewResponseDto
}