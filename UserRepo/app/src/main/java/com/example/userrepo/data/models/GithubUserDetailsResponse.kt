package com.example.userrepo.data.models


import com.google.gson.annotations.SerializedName

data class GithubUserDetailsResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("followers")
    val followers: Int?,
    @SerializedName("following")
    val following: Int?,
    @SerializedName("login")
    val login: String?,
    @SerializedName("name")
    val name: String?,

    ) {
    fun toUserDetailsModel() = UserDetailsModel(
        avatarUrl = avatarUrl ?: "",
        followersCount = followers ?: 0,
        followingCount = following ?: 0,
        name = name ?: "",
        login = login ?: "",
    )
}