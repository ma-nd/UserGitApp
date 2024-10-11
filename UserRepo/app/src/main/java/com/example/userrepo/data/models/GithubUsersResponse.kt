package com.example.userrepo.data.models


import com.google.gson.annotations.SerializedName

data class GithubUsersResponse(
    @SerializedName("items")
    val items: List<GithubUserDetailsResponse>,
    @SerializedName("total_count")
    val totalCount: Int
)