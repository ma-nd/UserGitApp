package com.example.userrepo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.userrepo.base.data.Response
import com.example.userrepo.data.models.UserDetailsModel

class UserListPagingSource(private val query: String, private val github: GithubRepository) :
    PagingSource<Int, UserDetailsModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserDetailsModel> {
        return try {
            val currentPage = params.key ?: 1
            val usersResult = if (query.isNotBlank()) {
                github.getUserList(
                    query = query,
                    page = currentPage,
                    pageSize = params.loadSize
                )
            } else {
                Response.Success(emptyList())
            }

            when (usersResult) {
                is Response.Success -> {
                    LoadResult.Page(
                        data = usersResult.data,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (usersResult.data.isEmpty()) null else currentPage + 1
                    )
                }

                is Response.Error -> {
                    LoadResult.Error(usersResult.exception)
                }
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserDetailsModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}