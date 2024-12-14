package com.example.userrepo.data.models


data class UserRepoModel(
    val name: String,
    val languages: String,
    val starsToShowCount: Int,
    val starsToAdd: Int,
    val description: String,
    val url: String,
)