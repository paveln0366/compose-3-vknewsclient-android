package com.example.vknewsclient.data.network

import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.data.model.kinopoisk.FilmsListDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadRecommendations(
        @Query("access_token") token: String
    ): NewsFeedResponseDto

    @GET("api/v2.2/films")
    @Headers(
        "x-api-key: ccecf0d7-52ed-4c7e-b53a-0818d4a0c09c",
        "Content-Type: application/json",
    )
    suspend fun loadRecommendationsV2(): FilmsListDto
}