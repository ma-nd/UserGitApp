package com.example.userrepo.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.userrepo.R

@Composable
fun ErrorDialog(errorMessage: String) {
    Column(
        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.common_padding_16)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.error), color = MaterialTheme.colorScheme.error)
        Text(text = errorMessage, textAlign = TextAlign.Center)
    }
}

@PreviewLightDark
@Composable
private fun PreviewErrorDialog() {
    ErrorDialog("Error message")
}