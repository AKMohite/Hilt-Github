package app.mak.gitstar.features.discover.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FolderOff
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mak.gitstar.core.designsystem.GitStarTheme
import app.mak.gitstar.core.designsystem.components.RepoCardSkeleton
import app.mak.gitstar.features.discover.presentation.components.RepoCard
import app.mak.gitstar.features.discover.presentation.model.DiscoverAction
import app.mak.gitstar.features.discover.presentation.model.DiscoverState
import app.mak.gitstar.features.discover.presentation.model.RepoSort
import app.mak.gitstar.features.discover.presentation.model.dummyRepositories
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiscoverRoot(
    modifier: Modifier = Modifier
) {
    val viewModel: DiscoverViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            RepositoriesTopBar(
                username = state.currentUser,
                sortBy = state.sortBy,
                showForked = state.showForked,
                onSortChange = { viewModel.onAction(DiscoverAction.OnSortChange(it)) },
                onToggleForked = { viewModel.onAction(DiscoverAction.OnToggleForked(it)) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        DiscoverScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = modifier.padding(padding),
        )
    }
}

@Composable
private fun DiscoverScreen(
    state: DiscoverState,
    onAction: (DiscoverAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier
            .fillMaxSize()
//            .nestedScroll(pullRefreshState),
    ) {
        when {
            state.isLoading && state.repos.isEmpty() -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(6) {
                        RepoCardSkeleton(modifier = Modifier.testTag("repo_card_skeleton"))
                    }
                }
            }

            state.repos.isEmpty() -> {
                EmptyReposState(modifier = Modifier.testTag("empty_repos_state"))
            }

            else -> {
                val listState = rememberLazyListState()
                val showScrollTop by remember {
                    derivedStateOf { listState.firstVisibleItemIndex > 3 }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        item {
                            RepoCountHeader(count = state.repos.size)
                        }

                        val displayed = if (state.showForked) state.repos
                        else state.repos.filter { !it.isFork }

                        items(
                            items = displayed,
                            key = { it.id },
                        ) { repo ->
                            RepoCard(
                                repo = repo,
                                onClick = { onAction(DiscoverAction.OnRepoClick(repo.fullName)) },
                                modifier = Modifier
                                    .animateItem()
                                    .testTag("repo_card"),
                            )
                        }

                        // Load-more trigger
                        item {
                            LaunchedEffect(listState) {
                                val layoutInfo = listState.layoutInfo
                                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                if (lastVisible >= layoutInfo.totalItemsCount - 3) {
                                    onAction(DiscoverAction.OnLoadMore)
                                }
                            }
                        }

                        if (state.isLoading) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                                }
                            }
                        }
                    }

                    // Scroll-to-top FAB
                    AnimatedVisibility(
                        visible = showScrollTop,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { it } + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(24.dp),
                    ) {
                        FloatingActionButton(
                            onClick = { /* scroll to top */ },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp),
                        ) {
                            Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = "Scroll to top")
                        }
                    }
                }
            }
        }

        // Pull-to-refresh indicator
        @OptIn(ExperimentalMaterial3Api::class)
        if (pullRefreshState.isAnimating || state.isRefreshing) {
            onAction(DiscoverAction.OnRefresh)
        }
        /*PullToRefreshContainer(
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .testTag("pull_to_refresh"),
            containerColor = MaterialTheme.colorScheme.surface,
        )*/
    }
}

@Composable
private fun RepoCountHeader(count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$count repositories",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun EmptyReposState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.FolderOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No repositories yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "Create your first repo on GitHub",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepositoriesTopBar(
    username: String,
    sortBy: RepoSort,
    showForked: Boolean,
    onSortChange: (RepoSort) -> Unit,
    onToggleForked: (Boolean) -> Unit,
) {
    var showSortMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Repositories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                if (username.isNotEmpty()) {
                    Text(
                        text = "@$username",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        actions = {
            // Forked toggle
            FilterChip(
                selected = !showForked,
                onClick = { onToggleForked(!showForked) },
                label = { Text("Hide forks") },
                modifier = Modifier.padding(end = 4.dp),
            )
            // Sort menu
            Box {
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(Icons.AutoMirrored.Outlined.Sort, contentDescription = "Sort repositories")
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                ) {
                    RepoSort.entries.forEach { sort ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    sort.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontWeight = if (sortBy == sort) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            onClick = {
                                onSortChange(sort)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                if (sortBy == sort) {
                                    Icon(Icons.Outlined.Check, contentDescription = null)
                                }
                            },
                        )
                    }
                }
            }
        },
    )
}

private class DiscoverScreenStateProvider: PreviewParameterProvider<DiscoverState> {

    val data = listOf(
        Pair(
            "Loading",
            DiscoverState(
                isLoading = true,
            )
        ),
        Pair(
            "Refreshing",
            DiscoverState(
                isRefreshing = true
            )
        ),
        Pair(
            "Empty",
            DiscoverState()
        ),
        Pair(
            "Success",
            DiscoverState(
                repos = dummyRepositories
            )
        )
    )

    override val values: Sequence<DiscoverState>
        get() = data.map { it.second }.asSequence()

    override fun getDisplayName(index: Int): String {
        return data[index].first
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoriesScreenLoadingPreview(
    @PreviewParameter(DiscoverScreenStateProvider::class) previewData: DiscoverState
) {
    GitStarTheme {
        DiscoverScreen(
            state = previewData,
            onAction = {}
        )
    }
}


