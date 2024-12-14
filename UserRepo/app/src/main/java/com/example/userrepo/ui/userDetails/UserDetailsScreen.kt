package com.example.userrepo.ui.userDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.userrepo.R
import com.example.userrepo.base.ui.theme.UserRepoTheme
import com.example.userrepo.data.models.UserActivityModel
import com.example.userrepo.data.models.UserDetailsModel
import com.example.userrepo.data.models.UserRepoModel
import com.example.userrepo.ui.component.EmptyListView
import com.example.userrepo.ui.component.UserItemView
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher

@Composable
fun UserDetailsScreen(viewModel: UserDetailsViewModel, onRepoClick: (String) -> Unit) {
    val userList = viewModel.getRepoList().collectAsLazyPagingItems()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    UserDetailsLayout(
        repoList = userList,
        activityList = state.userActivities,
        user = state.userDetails,
        actionHandler = viewModel,
        onRepoClick = onRepoClick,
    )
}

@Composable
private fun UserDetailsLayout(
    repoList: LazyPagingItems<UserRepoModel>,
    activityList: List<UserActivityModel>,
    user: UserDetailsModel?,
    actionHandler: UserDetailsViewModelActionHandler,
    onRepoClick: (String) -> Unit,
) {
    if (user != null) {
        Column {
            UserItemView(userName = user.login, userAvatarUrl = user.avatarUrl)
            Text(
                text = user.name,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.common_padding_16))
            )
            FollowerItem(followings = user.followingCount, followers = user.followersCount)
            if (repoList.loadState.refresh is LoadState.Error) {
                actionHandler.onListError((repoList.loadState.refresh as LoadState.Error).error)
            }
            val tabData = listOf(
                stringResource(id = R.string.tab_repository),
                stringResource(id = R.string.tab_activity),
            )
            val repoListState = rememberLazyListState()
            val activityListState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()

            val pagerState = rememberPagerState() { tabData.size }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        tabs = {
                            tabData.forEachIndexed { index, title ->
                                Tab(
                                    text = { Text(title) },
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                )
                            }
                        }
                    )

                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                    ) { tabId ->
                        when (tabId) {
                            0 ->
                                LazyColumn(state = repoListState) {
                                    items(repoList.itemCount) { index ->
                                        repoList[index]?.let {
                                            RepoItem(it, onClick = { url ->
                                                onRepoClick(url)
                                            })

                                            if (index != repoList.itemCount - 1) {
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
                                    item {
                                        if (repoList.loadState.refresh is LoadState.NotLoading &&
                                            repoList.itemCount == 0
                                        ) EmptyListView()
                                    }
                                }

                            1 ->
                                LazyColumn(state = activityListState) {
                                    items(activityList.count()) { index ->
                                        ActivityItem(activityList[index], onClick = { url ->
                                            onRepoClick(url)
                                        })

                                        if (index != activityList.count() - 1) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(
                                                    vertical = dimensionResource(
                                                        id = R.dimen.common_padding_16
                                                    )
                                                )
                                            )
                                        }
                                    }

                                    item {
                                        if (activityList.isEmpty()) EmptyListView()
                                    }
                                }
                        }
                    }
                }
            )
        }

    }
}

@Composable
private fun FollowerItem(followers: Int, followings: Int) {
    Row(
        modifier = Modifier
            .padding(all = dimensionResource(id = R.dimen.common_padding_16))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "${stringResource(id = R.string.followers)} : $followers",
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "${stringResource(id = R.string.followings)} : $followings",
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun RepoItem(item: UserRepoModel, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item.url) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.common_padding_16))
            )
            Row {
                Spacer(modifier = Modifier.weight(1f))
                val painter = painterResource(id = R.drawable.ic_star_rate)
                repeat(item.starsToShowCount) {
                    Icon(
                        painter = painter,
                        contentDescription = "Stars",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
                if (item.starsToAdd > 0) {
                    Text(
                        text = "+${item.starsToAdd}",
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.common_padding_16))
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                text = item.description,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.common_padding_16))
            )
            Text(
                text = item.languages,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.common_padding_16))
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ActivityItem(item: UserActivityModel, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.common_padding_16))
            .clickable(enabled = item.url.isNotBlank(), onClick = { onClick(item.url) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.repoName,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.common_padding_16))
            )

            Text(
                text = item.activityType + ((": ${item.actionType}").takeIf { item.actionType.isNotBlank() }
                    ?: ""),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.common_padding_16))
            )
        }
        if (item.url.isNotBlank()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewUserItem() {
    UserRepoTheme {
        UserDetailsLayout(
            repoList = flowOf(
                PagingData.from(
                    listOf(
                        UserRepoModel(
                            name = "Name",
                            languages = "Java",
                            starsToShowCount = 10,
                            starsToAdd = 5,
                            description = "Description",
                            url = "",
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
            onRepoClick = {},
            user = UserDetailsModel(
                avatarUrl = "",
                followersCount = 0,
                followingCount = 0,
                login = "User Login",
                name = "User Name",
            ),
            activityList = listOf(
                UserActivityModel(
                    activityType = "Activity",
                    actionType = "Action",
                    repoName = "Repo",
                    url = ""
                )
            ),
            actionHandler = object : UserDetailsViewModelActionHandler {},
        )
    }
}