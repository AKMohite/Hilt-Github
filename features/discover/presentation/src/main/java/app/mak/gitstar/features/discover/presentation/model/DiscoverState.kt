package app.mak.gitstar.features.discover.presentation.model

import app.mak.gitstar.core.presentation.UiText

internal data class DiscoverState(
    val repos: List<RepositoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: UiText? = null,
    val currentUser: String = "",
    val showForked: Boolean = true,
    val sortBy: RepoSort = RepoSort.UPDATED
)

internal enum class RepoSort { UPDATED, STARS, FORKS, NAME }

internal sealed interface DiscoverEvent {
    data class NavigateToDetail(val owner: String, val repo: String) : DiscoverEvent
    data class ShowError(val message: UiText) : DiscoverEvent
}

internal sealed interface DiscoverAction {
    data object OnRefresh : DiscoverAction
    data class OnRepoClick(val fullName: String) : DiscoverAction
    data class OnSortChange(val sort: RepoSort) : DiscoverAction
    data class OnToggleForked(val show: Boolean) : DiscoverAction
    data object OnLoadMore : DiscoverAction
}
