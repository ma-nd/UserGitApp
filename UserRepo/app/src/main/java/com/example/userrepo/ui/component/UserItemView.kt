package com.example.userrepo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.userrepo.R
import com.example.userrepo.base.ui.theme.UserRepoTheme

@Composable
fun UserItemView(userName: String, userAvatarUrl: String, onUserClick: (String) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick(userName) }
            .padding(horizontal = dimensionResource(id = R.dimen.common_padding_16)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.user_avatar))
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(userAvatarUrl)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_image_error),
            contentDescription = "User avatar",
        )
        Text(
            text = userName,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.common_padding_16))
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewUserItem() {
    UserRepoTheme {
        UserItemView(
            userName = "User name",
            userAvatarUrl = "",
            onUserClick = {},
        )
    }
}