package com.example.userrepo.ui.login

import com.example.userrepo.base.ui.BaseViewModel
import com.example.userrepo.data.AuthManager

class LoginViewModel(private val authManager: AuthManager) :
    BaseViewModel<LoginViewModel.State>(State()),
    LoginViewModelActionHandler {

    override fun onUserNameChanged(userName: String) {
        _uiState.value = _uiState.value.copy(userName = userName)
        updateButtonState()
    }

    override fun onTokenChanged(token: String) {
        _uiState.value = _uiState.value.copy(token = token)
        updateButtonState()
    }

    private fun updateButtonState() {
        val state = _uiState.value
        _uiState.value =
            state.copy(isButtonEnabled = state.token.isNotBlank() && state.userName.isNotBlank())
    }

    override fun onSaveClick(isTempLogin: Boolean) {
        val state = _uiState.value
        authManager.saveLoginData(
            userName = state.userName,
            token = state.token,
            isTempLogin = isTempLogin,
        )
        closeScreen()
    }

    data class State(
        val userName: String = "",
        val token: String = "",
        val isButtonEnabled: Boolean = false,
    )
}

interface LoginViewModelActionHandler {
    fun onSaveClick(isTempLogin: Boolean) {}
    fun onUserNameChanged(userName: String) {}
    fun onTokenChanged(token: String) {}
}