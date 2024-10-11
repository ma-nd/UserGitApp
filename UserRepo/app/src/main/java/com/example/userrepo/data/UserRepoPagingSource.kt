package com.example.userrepo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.userrepo.base.data.Response
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

            when (result) {
                is Response.Success -> {
                    LoadResult.Page(
                        data = result.data,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (result.data.isEmpty()) null else currentPage + 1
                    )
                }

                is Response.Error -> {
                    LoadResult.Error(result.exception)
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