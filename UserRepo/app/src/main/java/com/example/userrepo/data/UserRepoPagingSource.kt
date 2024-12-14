package com.example.userrepo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.userrepo.data.models.UserRepoModel

class UserRepoPagingSource(private val userName: String, private val github: GithubRepository) :
    PagingSource<Int, UserRepoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserRepoModel> {
        return try {
            val currentPage = params.key ?: 1
            val result = github.getUserRepos(
                userName = userName,
                page = currentPage,
                pageSize = params.loadSize
            )

            when {
                result.isSuccess -> {
                    val data = result.getOrThrow()
                    LoadResult.Page(
                        data = data,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (data.isEmpty()) null else currentPage + 1
                    )
                }

                else -> {
                    LoadResult.Error(result.exceptionOrNull() ?: Error())
                }
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserRepoModel>): Int? {
        return state.anchorPosition
    }
}