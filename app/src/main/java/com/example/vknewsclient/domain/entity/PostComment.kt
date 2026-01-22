package com.example.vknewsclient.domain.entity

class PostComment(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String?,
    val commentText: String,
    val publicationDate: String
)