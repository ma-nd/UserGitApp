package com.example.userrepo.data

import com.example.userrepo.data.api.GithubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GithubRepository(private val githubApi: GithubApi) {

    suspend fun getUserList(query: String, page: Int, pageSize: Int) = withContext(Dispatchers.IO) {
        try {
            val response = githubApi.getUsersList(
                query = query,
                page = page,
                pageSize = pageSize,
            ).items.map { it.toUserDetailsModel() }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDetails(userName: String) = withContext(Dispatchers.IO) {
        try {
            val response = githubApi.getUserDetails(
                username = userName
            ).toUserDetailsModel()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserActivities(userName: String) = withContext(Dispatchers.IO) {
        try {
            val response = githubApi.getUserActivities(
                username = userName
            ).map { it.toUserActivityModel() }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserRepos(
        userName: String,
        page: Int,
        pageSize: Int,
        forkFilter: Boolean = true
    ) = withContext(Dispatchers.IO) {
        try {
            val response = githubApi.getUserRepos(
                username = userName,
                page = page,
                pageSize = pageSize,
            )
            val filteredResponse = if (forkFilter) {
                response.filter { !it.fork }
            } else {
                response
            }

            Result.success(filteredResponse.map { it.toUserRepoModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}