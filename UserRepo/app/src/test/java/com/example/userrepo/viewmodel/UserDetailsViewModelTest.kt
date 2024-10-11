package com.example.userrepo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.userrepo.base.data.Response
import com.example.userrepo.base.navigation.ArgParams
import com.example.userrepo.data.GithubRepository
import com.example.userrepo.data.models.UserDetailsModel
import com.example.userrepo.ui.userDetails.UserDetailsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailsViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userModel = mockk<UserDetailsModel>(relaxed = true)
    private val testName = "User test name"
    private val githubRepository = mockk<GithubRepository>(relaxed = true)
    private val handle = mockk<SavedStateHandle>(relaxed = true)

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
    fun getUserDetails_success() = runTest {
        every { handle.get<String>(ArgParams.USER_NAME_ARGS) } returns testName
        coEvery { githubRepository.getUserDetails(testName) } returns Response.Success(userModel)
        val viewModel = UserDetailsViewModel(
            handle = handle,
            githubRepository = githubRepository
        )
        advanceUntilIdle()
        coVerify { githubRepository.getUserDetails(testName) }
        assertEquals(userModel, viewModel.uiState.value.userDetails)
    }

    @Test
    fun getUserDetails_error() = runTest {
        every { handle.get<String>(ArgParams.USER_NAME_ARGS) } returns testName
        coEvery { githubRepository.getUserDetails(testName) } returns Response.Error(Error())
        val viewModel = UserDetailsViewModel(
            handle = handle,
            githubRepository = githubRepository
        )
        advanceUntilIdle()
        coVerify { githubRepository.getUserDetails(testName) }
        assertNotNull(viewModel.commonState.value.error)
    }

    @Test
    fun getUserDetails_error_emptyName() = runTest {
        every { handle.get<String>(ArgParams.USER_NAME_ARGS) } returns ""
        coEvery { githubRepository.getUserDetails(testName) } returns Response.Error(Error())
        val viewModel = UserDetailsViewModel(
            handle = handle,
            githubRepository = githubRepository
        )
        advanceUntilIdle()
        coVerify(exactly = 0) { githubRepository.getUserDetails(testName) }
        assertNotNull(viewModel.commonState.value.error)
    }

    @Test
    fun onListError() = runTest {
        every { handle.get<String>(ArgParams.USER_NAME_ARGS) } returns ""
        val errorCode = 403
        val error = HttpException(retrofit2.Response.error<String>(errorCode, "".toResponseBody()))
        val viewModel = UserDetailsViewModel(
            handle = handle,
            githubRepository = githubRepository
        )
        viewModel.onListError(error)
        viewModel.commonState.value.error?.handle {
            assertEquals(error, it.exception)
        }
    }

//todo: Check unit testing for paging flow

//    @Test
//    fun getRepoList() = runTest {
//        val repoItem = mockk<UserRepoModel>(relaxed = true)
//        every { handle.get<String>(ArgParams.USER_NAME_ARGS) } returns ""
//        coEvery { githubRepository.getUserDetails(testName) } returns Response.Success(userModel)
//        coEvery { githubRepository.getUserRepos(any(),any(),any(), any()) } returns Response.Success(
//            listOf(repoItem)
//        )
//        val viewModel = UserDetailsViewModel(
//            handle = handle,
//            githubRepository = githubRepository
//        )
//
//        val list = mutableListOf<UserRepoModel>()
//        val job = launch(StandardTestDispatcher()) {
//             viewModel.getRepoList().asSnapshot().toCollection(list)
//        }
//        advanceUntilIdle()
//        coVerify { githubRepository.getUserRepos(any(),any(),any(), any()) }
//        assertEquals(list.first(), repoItem)
//        job.cancel()
//    }
}