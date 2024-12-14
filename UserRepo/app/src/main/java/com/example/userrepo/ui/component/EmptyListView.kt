package com.example.userrepo.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.userrepo.R

@Composable
fun EmptyListView() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.common_padding_16)),
        text = stringResource(id = R.string.no_results_info),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
    )
}

@PreviewLightDark
@Composable
fun PreviewEmptyListView() {
    EmptyListView()
}