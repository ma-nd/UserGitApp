package com.example.userrepo.data.models


data class UserDetailsModel(
    val avatarUrl: String,
    val followersCount: Int,
    val followingCount: Int,
    val login: String,
    val name: String,
)