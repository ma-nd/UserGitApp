package com.example.userrepo.base.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.userrepo.R
import com.example.userrepo.base.data.ErrorType
import com.example.userrepo.base.navigation.NavigationItem
import com.example.userrepo.data.AuthManager
import org.koin.android.ext.android.inject
import retrofit2.HttpException

open class BaseActivity : ComponentActivity() {

    private val authManager: AuthManager by inject()

    @Composable
    fun <T : Any> RegisterVMCommonState(navController: NavController, viewModel: BaseViewModel<T>) {
        val commonState = (viewModel as BaseViewModel<*>).commonState.collectAsStateWithLifecycle()
        commonState.value.error?.handle {
            navController.navigate(
                NavigationItem.ErrorDialog.createRoute(
                    when (it) {
                        is HttpException -> {
                            when {
                                it.code() == ErrorType.AuthenticationError.code
                                        && authManager.isUserAuthorize() -> {
                                    resources.getString(R.string.auth_error_message)
                                }

                                it.code() == ErrorType.AuthenticationError.code -> {
                                    resources.getString(R.string.login_error_message)
                                }

                                else -> resources.getString(R.string.common_error_message)
                            }
                        }

                        else -> it.message ?: resources.getString(R.string.common_error_message)
                    }

                )
            )
        }
        commonState.value.closeScreen?.handle {
            navController.popBackStack()
        }
    }
}