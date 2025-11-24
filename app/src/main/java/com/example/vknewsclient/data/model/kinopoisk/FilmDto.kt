package com.example.vknewsclient.data.model.kinopoisk

import com.google.gson.annotations.SerializedName

class FilmDto(
    @SerializedName("kinopoiskId") val kinopoiskId: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String?,
    @SerializedName("nameRu") val nameRu: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("ratingKinopoisk") val ratingKinopoisk: Float?,
)