package com.example.userrepo.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.userrepo.R
import com.example.userrepo.base.navigation.ArgParams.GO_BACK_RESULT_ARGS
import com.example.userrepo.base.navigation.ArgParams.MESSAGE_ARGS
import com.example.userrepo.base.navigation.ArgParams.URL_ARGS
import com.example.userrepo.base.navigation.NavigationItem
import com.example.userrepo.base.ui.BaseActivity
import com.example.userrepo.base.ui.theme.UserRepoTheme
import com.example.userrepo.ui.component.ErrorDialog
import com.example.userrepo.ui.component.WebViewCompose
import com.example.userrepo.ui.login.LoginScreen
import com.example.userrepo.ui.login.LoginViewModel
import com.example.userrepo.ui.userDetails.UserDetailsScreen
import com.example.userrepo.ui.userDetails.UserDetailsViewModel
import com.example.userrepo.ui.userList.UserListScreen
import com.example.userrepo.ui.userList.UserListViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel

class MainActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserRepoTheme {
                val navController = rememberNavController()
                var backStackNotEmpty by remember { mutableStateOf(false) }
                val isBackButtonVisible by remember { derivedStateOf { backStackNotEmpty } }
                LaunchedEffect(navController) {
                    val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
                        backStackNotEmpty = controller.previousBackStackEntry != null
                    }
                    navController.addOnDestinationChangedListener(listener)
                }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background), topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            },
                            navigationIcon = {
                                if (isBackButtonVisible) {
                                    IconButton(onClick = {
                                        navController.popBackStack()
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_back),
                                            contentDescription = "Arrow back"
                                        )
                                    }
                                }
                            },
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavigationItem.UserList.route,
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
                        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
                        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End) },
                        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End) }
                    ) {
                        composable(NavigationItem.Login.route) {
                            val viewModel: LoginViewModel =
                                koinNavViewModel<LoginViewModel>().apply {
                                    RegisterVMCommonState(
                                        navController = navController,
                                        viewModel = this
                                    )
                                }
                            LoginScreen(viewModel = viewModel,
                                onSaveClick = {
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        GO_BACK_RESULT_ARGS,
                                        true
                                    )
                                }
                            )
                        }
                        composable(NavigationItem.UserList.route) { backStackEntry ->
                            val saveBackResult =
                                backStackEntry.savedStateHandle.get<Boolean>(GO_BACK_RESULT_ARGS)
                            backStackEntry.savedStateHandle.remove<Boolean>(GO_BACK_RESULT_ARGS)

                            val viewModel: UserListViewModel =
                                koinNavViewModel<UserListViewModel>().apply {
                                    RegisterVMCommonState(
                                        navController = navController,
                                        viewModel = this
                                    )
                                }
                            UserListScreen(
                                viewModel = viewModel,
                                onUserClick = { userName ->
                                    navController.navigate(
                                        NavigationItem.UserDetails.createRoute(
                                            userName
                                        )
                                    )
                                },
                                onLogInClick = {
                                    navController.navigate(
                                        NavigationItem.Login.route
                                    )
                                }, onBackRefresh = saveBackResult ?: false
                            )
                        }
                        composable(NavigationItem.UserDetails.route) {
                            val viewModel: UserDetailsViewModel =
                                koinNavViewModel<UserDetailsViewModel>().apply {
                                    RegisterVMCommonState(
                                        navController = navController,
                                        viewModel = this
                                    )
                                }
                            UserDetailsScreen(viewModel = viewModel, onRepoClick = { url ->
                                navController.navigate(
                                    NavigationItem.WebView.createRoute(
                                        url
                                    )
                                )
                            })
                        }
                        composable(NavigationItem.WebView.route) {
                            val url =
                                navController.currentBackStackEntry?.arguments?.getString(URL_ARGS)

                            url?.let { WebViewCompose(url = url) }
                        }
                        dialog(
                            NavigationItem.ErrorDialog.route,
                            dialogProperties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true,
                            )
                        ) {
                            val errorMessage =
                                navController.currentBackStackEntry?.arguments?.getString(
                                    MESSAGE_ARGS
                                )
                            ErrorDialog(errorMessage = errorMessage ?: "")
                        }
                    }
                }
            }
        }
    }
}