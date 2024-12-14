package com.example.userrepo.data.models


import com.google.gson.annotations.SerializedName

data class GithubUserActivityResponse(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("payload")
    val payload: Payload?,
    @SerializedName("repo")
    val repo: Repo?,
    @SerializedName("type")
    val type: String?
) {
    data class Payload(
        @SerializedName("action")
        val action: String?,
        @SerializedName("issue")
        val issue: Issue?
    ) {
        data class Issue(
            @SerializedName("html_url")
            val htmlUrl: String?,
            @SerializedName("id")
            val id: Long?,
        )
    }

    data class Repo(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    fun toUserActivityModel() = UserActivityModel(
        activityType = type ?: "",
        repoName = repo?.name ?: "",
        url = payload?.issue?.htmlUrl ?: "",
        actionType = payload?.action ?: "",
    )
}
