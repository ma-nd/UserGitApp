package com.example.userrepo.ui.component

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.userrepo.R

@Composable
fun TextFieldPlaceholderView(@StringRes textId: Int) {
    Text(
        text = stringResource(id = textId),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    )
}

@PreviewLightDark
@Composable
fun PreviewTextFieldPlaceholderView() {
    TextFieldPlaceholderView(textId = R.string.login_name_placeholder)
}