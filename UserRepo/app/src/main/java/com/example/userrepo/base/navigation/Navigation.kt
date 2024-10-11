package com.example.userrepo.base.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

enum class Screen {
    UserList,
    UserDetails,
    Login,
    ErrorDialog,
    WebView,
}

sealed class NavigationItem(val route: String) {
    object Login : NavigationItem(Screen.Login.name)
    object UserList : NavigationItem(Screen.UserList.name)
    object UserDetails :
        NavigationItem("${Screen.UserDetails.name}/{${ArgParams.USER_NAME_ARGS}}") {
        fun createRoute(userName: String) = "${Screen.UserDetails.name}/$userName"
    }

    object ErrorDialog : NavigationItem(
        "${Screen.ErrorDialog.name}/{${ArgParams.MESSAGE_ARGS}}"
    ) {
        fun createRoute(message: String): String {
            return "${Screen.ErrorDialog.name}/$message"
        }
    }

    object WebView : NavigationItem("${Screen.WebView.name}/{${ArgParams.URL_ARGS}}") {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())

            return "${Screen.WebView.name}/$encodedUrl"
        }
    }
}

object ArgParams {
    const val USER_NAME_ARGS = "userName"
    const val URL_ARGS = "url"
    const val MESSAGE_ARGS = "message"
    const val GO_BACK_RESULT_ARGS = "onGoBack"
}

