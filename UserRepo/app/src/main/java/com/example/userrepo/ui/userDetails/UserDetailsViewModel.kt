package com.example.userrepo.ui.userDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.userrepo.base.data.Response
import com.example.userrepo.base.navigation.ArgParams
import com.example.userrepo.base.ui.BaseViewModel
import com.example.userrepo.data.GithubRepository
import com.example.userrepo.data.UserRepoPagingSource
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
        getUserDetails()
    }

    private fun getUserDetails() {
        if (userName.isNullOrBlank()) {
            onError(Response.Error(Error()))
            return
        }

        viewModelScope.launch {
            githubRepository.getUserDetails(userName)
                .apply {
                    onSuccess {
                        _uiState.value = _uiState.value.copy(userDetails = it as UserDetailsModel)
                    }
                    onError {
                        onError(it)
                    }
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
        onError(Response.Error(error))
    }

    data class State(val userDetails: UserDetailsModel? = null)
}

interface UserDetailsViewModelActionHandler {
    fun onListError(error: Throwable) {}
}