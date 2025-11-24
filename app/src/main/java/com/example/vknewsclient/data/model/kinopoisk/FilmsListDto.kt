package com.example.vknewsclient.data.model.kinopoisk

import com.google.gson.annotations.SerializedName

class FilmsListDto(
    @SerializedName("total") val total: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("items") val items: List<FilmDto>
)