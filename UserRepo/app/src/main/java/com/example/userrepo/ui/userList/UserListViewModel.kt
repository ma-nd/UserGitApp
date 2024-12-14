package com.example.userrepo.ui.userList

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.userrepo.base.ui.BaseViewModel
import com.example.userrepo.data.AuthManager
import com.example.userrepo.data.GithubRepository
import com.example.userrepo.data.UserListPagingSource
import com.example.userrepo.data.models.UserDetailsModel
import kotlinx.coroutines.flow.Flow

class UserListViewModel(
    private val githubRepository: GithubRepository,
    private val authManager: AuthManager
) : BaseViewModel<UserListViewModel.State>(State(isUserLogIn = authManager.isUserAuthorize())),
    UserListViewModelActionHandler {
    private val pageSize = 15

    fun getUserList(): Flow<PagingData<UserDetailsModel>> =
        getUserListSource().cachedIn(viewModelScope)

    private fun getUserListSource() = Pager(
        pagingSourceFactory = {
            UserListPagingSource(
                query = _uiState.value.query,
                github = githubRepository
            )
        },
        config = PagingConfig(
            pageSize = pageSize
        )
    ).flow

    override fun onLogOutClick() {
        authManager.logOut()
        _uiState.value = _uiState.value.copy(isUserLogIn = authManager.isUserAuthorize())
    }

    override fun onListError(error: Throwable) {
        onError(error)
    }

    override fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun onRefresh() {
        _uiState.value = _uiState.value.copy(isUserLogIn = authManager.isUserAuthorize())
    }

    data class State(val query: String = "", val isUserLogIn: Boolean)
}

interface UserListViewModelActionHandler {
    fun onQueryChanged(query: String) {}
    fun onListError(error: Throwable) {}
    fun onLogOutClick() {}
}