package com.example.vknewsclient.data.model.kinopoisk

import com.google.gson.annotations.SerializedName

class ReviewDto(
    @SerializedName("kinopoiskId") val kinopoiskId: Long,
    @SerializedName("type") val type: String,
    @SerializedName("date") val date: String,
    @SerializedName("positiveRating") val positiveRating: Long,
    @SerializedName("negativeRating") val negativeRating: Long,
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String
)