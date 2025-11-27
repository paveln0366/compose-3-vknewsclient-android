package com.example.vknewsclient.data.model.kinopoisk

import com.google.gson.annotations.SerializedName

data class ReviewResponseDto(
    @SerializedName("total") val total: Long,
    @SerializedName("totalPages") val totalPages: Long,
    @SerializedName("totalPositiveReviews") val totalPositiveReviews: Long,
    @SerializedName("totalNegativeReviews") val totalNegativeReviews: Long,
    @SerializedName("totalNeutralReviews") val totalNeutralReviews: Long,
    @SerializedName("items") val items: List<ReviewDto>
)