package com.example.userrepo.ui.userList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.userrepo.R
import com.example.userrepo.base.ui.theme.UserRepoTheme
import com.example.userrepo.data.models.UserDetailsModel
import com.example.userrepo.ui.component.TextFieldPlaceholderView
import com.example.userrepo.ui.component.UserItemView
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher

@Composable
fun UserListScreen(
    viewModel: UserListViewModel, onUserClick: (String) -> Unit, onLogInClick: (
    ) -> Unit, onBackRefresh: Boolean = false
) {
    if (onBackRefresh) {
        viewModel.onRefresh()
    }
    val userList = viewModel.getUserList().collectAsLazyPagingItems()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    UserListLayout(
        userList = userList,
        actionHandler = viewModel,
        onUserClick = onUserClick,
        onLogInClick = onLogInClick,
        query = state.query,
        isUserLogIn = state.isUserLogIn,
    )
}

@Composable
private fun UserListLayout(
    userList: LazyPagingItems<UserDetailsModel>,
    query: String,
    isUserLogIn: Boolean,
    actionHandler: UserListViewModelActionHandler,
    onUserClick: (String) -> Unit,
    onLogInClick: () -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = query,
                onValueChange = {
                    actionHandler.onQueryChanged(query = it)
                },
                placeholder = { TextFieldPlaceholderView(R.string.search_placeholder) },
                singleLine = true,
            )

            Button(onClick = {
                if (!isUserLogIn) {
                    onLogInClick()
                } else {
                    actionHandler.onLogOutClick()
                }
            }) {
                Text(text = stringResource(id = if (isUserLogIn) R.string.long_out else R.string.long_in))
            }
        }
        if (userList.loadState.refresh is LoadState.Error) {
            actionHandler.onListError((userList.loadState.refresh as LoadState.Error).error)
        }

        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(), state = listState
        ) {
            val listSize = userList.itemCount
            item {
                Spacer(
                    modifier = Modifier.padding(
                        top = dimensionResource(
                            id = R.dimen.common_padding_16
                        )
                    )
                )
            }
            items(listSize) { index ->
                val user = userList[index]
                user?.let {
                    UserItemView(
                        userName = it.login,
                        userAvatarUrl = it.avatarUrl,
                        onUserClick = onUserClick
                    )

                    if (index != listSize - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                vertical = dimensionResource(
                                    id = R.dimen.common_padding_16
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewUserListLayout() {
    UserRepoTheme {
        UserListLayout(
            userList = flowOf(
                PagingData.from(
                    listOf(
                        UserDetailsModel(
                            avatarUrl = "",
                            followersCount = 0,
                            followingCount = 0,
                            login = "",
                            name = "User Name",
                        )
                    ),
                    sourceLoadStates =
                    LoadStates(
                        refresh = LoadState.NotLoading(false),
                        append = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                    ),
                )
            ).collectAsLazyPagingItems(StandardTestDispatcher()),
            actionHandler = object : UserListViewModelActionHandler {},
            onUserClick = {},
            onLogInClick = {},
            query = "abc",
            isUserLogIn = true,
        )
    }
}