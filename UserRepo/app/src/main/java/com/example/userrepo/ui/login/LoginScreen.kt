package com.example.userrepo.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.userrepo.R
import com.example.userrepo.ui.component.TextFieldPlaceholderView

@Composable
fun LoginScreen(viewModel: LoginViewModel, onSaveClick: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoginLayout(
        state = state,
        actionHandler = viewModel,
        onSaveClick = onSaveClick,
    )
}

@Composable
private fun LoginLayout(
    state: LoginViewModel.State,
    actionHandler: LoginViewModelActionHandler,
    onSaveClick: () -> Unit
) {
    val commonPadding = dimensionResource(id = R.dimen.common_padding_16)
    var isCheckboxOn by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(all = commonPadding)) {
        TextField(
            value = state.userName,
            onValueChange = {
                actionHandler.onUserNameChanged(it)
            },
            singleLine = true,
            placeholder = { TextFieldPlaceholderView(R.string.login_name_placeholder) },
        )

        TextField(
            modifier = Modifier.padding(top = commonPadding),
            value = state.token,
            onValueChange = {
                actionHandler.onTokenChanged(it)
            },
            singleLine = true,
            placeholder = { TextFieldPlaceholderView(R.string.login_token_placeholder) },
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.save_message),
                modifier = Modifier.padding(end = commonPadding),
            )
            Checkbox(checked = isCheckboxOn, onCheckedChange = { isCheckboxOn = !isCheckboxOn })
        }

        Button(
            enabled = state.isButtonEnabled,
            modifier = Modifier.padding(all = commonPadding),
            onClick = {
                actionHandler.onSaveClick(isTempLogin = !isCheckboxOn)
                onSaveClick()
            }) {
            Text(text = stringResource(id = R.string.long_in))
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewLoginLayout() {
    LoginLayout(state = LoginViewModel.State(
        userName = "User name",
        token = "abc",
        isButtonEnabled = true
    ),
        actionHandler = object : LoginViewModelActionHandler {}, onSaveClick = {})
}