package com.example.userrepo.data.models


data class UserRepoModel(
    val name: String,
    val languages: String,
    val starsCount: Int,
    val description: String,
    val url: String,
) {
    private val maxStarShow = 10
    val starToShowCount
        get() = if (starsCount > maxStarShow) maxStarShow else starsCount
    val starToAdd
        get() = starsCount - maxStarShow
}