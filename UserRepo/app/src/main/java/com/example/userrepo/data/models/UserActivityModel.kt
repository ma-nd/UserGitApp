package com.example.userrepo.data.models


data class UserActivityModel(
    val activityType: String,
    val repoName: String,
    val url: String,
    val actionType: String,
)