package com.example.userrepo.viewmodel

import com.example.userrepo.data.AuthManager
import com.example.userrepo.ui.login.LoginViewModel
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Test

class LoginViewModelTest {
    private val authManager = mockk<AuthManager>(relaxed = true)
    private val testName = "User test name"
    private val testToken = "User token"

    private val viewModel = LoginViewModel(
        authManager = authManager
    )

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun onUserNameChanged() {
        viewModel.onUserNameChanged(testName)
        assertEquals(testName, viewModel.uiState.value.userName)
    }

    @Test
    fun onTokenChanged() {
        viewModel.onTokenChanged(testToken)
        assertEquals(testToken, viewModel.uiState.value.token)
    }

    @Test
    fun onButtonState_enabled() {
        viewModel.onTokenChanged(testToken)
        viewModel.onUserNameChanged(testName)
        assertEquals(true, viewModel.uiState.value.isButtonEnabled)
    }

    @Test
    fun onButtonState_disabled() {
        val pariDisabled: List<Pair<String, String>> =
            listOf(Pair("", ""), Pair("   ", "   "), Pair("test", ""), Pair("", "test"))
        pariDisabled.forEach {
            viewModel.onTokenChanged(it.first)
            viewModel.onUserNameChanged(it.second)
            assertEquals(false, viewModel.uiState.value.isButtonEnabled)
        }
    }

    @Test
    fun onSaveClick() {
        val isTempLogin = true
        viewModel.onTokenChanged(testToken)
        viewModel.onUserNameChanged(testName)
        viewModel.onSaveClick(isTempLogin = isTempLogin)

        verify {
            authManager.saveLoginData(
                userName = testName,
                token = testToken,
                isTempLogin = isTempLogin
            )
        }
        assert(viewModel.commonState.value.closeScreen != null)
    }
}