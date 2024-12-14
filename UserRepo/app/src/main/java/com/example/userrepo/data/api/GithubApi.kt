package com.example.userrepo.data.api

import com.example.userrepo.data.models.GithubUserActivityResponse
import com.example.userrepo.data.models.GithubUserDetailsResponse
import com.example.userrepo.data.models.GithubUserRepoResponse
import com.example.userrepo.data.models.GithubUsersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/users")
    suspend fun getUsersList(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): GithubUsersResponse

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String
    ): GithubUserDetailsResponse

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<GithubUserRepoResponse>

    @GET("users/{username}/events")
    suspend fun getUserActivities(
        @Path("username") username: String,
    ): List<GithubUserActivityResponse>
}