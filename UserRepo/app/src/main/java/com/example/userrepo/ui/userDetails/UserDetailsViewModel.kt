package com.example.userrepo.ui.userDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.userrepo.base.navigation.ArgParams
import com.example.userrepo.base.ui.BaseViewModel
import com.example.userrepo.data.GithubRepository
import com.example.userrepo.data.UserRepoPagingSource
import com.example.userrepo.data.models.UserActivityModel
import com.example.userrepo.data.models.UserDetailsModel
import com.example.userrepo.data.models.UserRepoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    handle: SavedStateHandle,
    private val githubRepository: GithubRepository
) :
    BaseViewModel<UserDetailsViewModel.State>(State()),
    UserDetailsViewModelActionHandler {
    private val userName = handle.get<String>(ArgParams.USER_NAME_ARGS)
    private val pageSize = 15

    init {
        getUserData()
    }

    private fun getUserData() {
        if (userName.isNullOrBlank()) {
            onError(Error())
            return
        }
        getUserDetails(userName)
        getUserActivities(userName)
    }

    private fun getUserDetails(userName: String) {
        viewModelScope.launch {
            githubRepository.getUserDetails(userName)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(userDetails = it)
                }
                .onFailure {
                    onError(it)
                }
        }
    }

    private fun getUserActivities(userName: String) {
        viewModelScope.launch {
            githubRepository.getUserActivities(userName)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(userActivities = it)
                }
                .onFailure {
                    onError(it)
                }
        }
    }

    fun getRepoList(): Flow<PagingData<UserRepoModel>> =
        getRepoListSource().cachedIn(viewModelScope)

    private fun getRepoListSource() = Pager(
        pagingSourceFactory = {
            UserRepoPagingSource(
                userName = userName ?: "",
                github = githubRepository
            )
        },
        config = PagingConfig(
            pageSize = pageSize
        )
    ).flow

    override fun onListError(error: Throwable) {
        onError(error)
    }

    data class State(
        val userDetails: UserDetailsModel? = null,
        val userActivities: List<UserActivityModel> = listOf()
    )
}

interface UserDetailsViewModelActionHandler {
    fun onListError(error: Throwable) {}
}