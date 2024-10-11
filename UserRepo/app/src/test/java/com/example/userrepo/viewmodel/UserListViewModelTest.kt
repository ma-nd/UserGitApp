package com.example.userrepo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.userrepo.base.data.Response
import com.example.userrepo.data.AuthManager
import com.example.userrepo.data.GithubRepository
import com.example.userrepo.data.models.UserDetailsModel
import com.example.userrepo.ui.userList.UserListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val authManager = mockk<AuthManager>(relaxed = true)
    private val userModel = mockk<UserDetailsModel>(relaxed = true)
    private val testName = "User test name"
    private val githubRepository = mockk<GithubRepository>(relaxed = true)

    @Before
    fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun onLogOutClick() = runTest {
        coEvery { githubRepository.getUserDetails(testName) } returns Response.Success(userModel)
        every { authManager.logOut() } just runs
        every { authManager.isUserAuthorize() } returns true
        val viewModel = UserListViewModel(
            authManager = authManager,
            githubRepository = githubRepository
        )
        assertTrue(viewModel.uiState.value.isUserLogIn)
        every { authManager.isUserAuthorize() } returns false

        viewModel.onLogOutClick()
        coVerify { authManager.logOut() }
        assertFalse(viewModel.uiState.value.isUserLogIn)
    }

    @Test
    fun onListError() = runTest {
        val errorCode = 403
        val error = HttpException(retrofit2.Response.error<String>(errorCode, "".toResponseBody()))
        val viewModel = UserListViewModel(
            authManager = authManager,
            githubRepository = githubRepository
        )

        viewModel.onListError(error)
        viewModel.commonState.value.error?.handle {
            assertEquals(error, it.exception)
        }
    }

    @Test
    fun onQueryChanged() = runTest {
        val viewModel = UserListViewModel(
            authManager = authManager,
            githubRepository = githubRepository
        )
        val testQuery = "test query"

        viewModel.onQueryChanged(testQuery)
        assertEquals(testQuery, viewModel.uiState.value.query)
    }

    @Test
    fun onRefresh() = runTest {
        every { authManager.isUserAuthorize() } returns false

        val viewModel = UserListViewModel(
            authManager = authManager,
            githubRepository = githubRepository
        )
        assertFalse(viewModel.uiState.value.isUserLogIn)
        every { authManager.isUserAuthorize() } returns true

        viewModel.onRefresh()
        assertTrue(viewModel.uiState.value.isUserLogIn)
    }
}