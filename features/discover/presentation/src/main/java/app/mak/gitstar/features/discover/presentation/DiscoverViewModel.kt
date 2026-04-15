package app.mak.gitstar.features.discover.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class DiscoverViewModel: ViewModel() {

    private val _state = MutableStateFlow(DiscoverState())
    val state = _state.asStateFlow()

    private val _events = Channel<DiscoverEvent>()
    val events = _events.receiveAsFlow()

    private var currentPage = 1
    private var canLoadMore = true

    init {
        loadCurrentUser()
    }

    fun onAction(action: DiscoverAction) {
        when(action) {
            DiscoverAction.OnLoadMore -> TODO()
            DiscoverAction.OnRefresh -> TODO()
            is DiscoverAction.OnRepoClick -> TODO()
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {

        }
    }

}

internal data class DiscoverState(
    val repos: List<RepositoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
//    val error: UiText? = null,
    val currentUser: String = "",
    val showForked: Boolean = true,
    val sortBy: RepoSort = RepoSort.UPDATED
)

internal enum class RepoSort { UPDATED, STARS, FORKS, NAME }

internal sealed interface DiscoverEvent {
    data class NavigateToDetail(val owner: String, val repo: String) : DiscoverEvent
//    data class ShowError(val message: UiText) : DiscoverEvent
}

internal sealed interface DiscoverAction {
    data object OnRefresh : DiscoverAction
    data class OnRepoClick(val fullName: String) : DiscoverAction
//    data class OnSortChange(val sort: RepoSort) : DiscoverAction
//    data class OnToggleForked(val show: Boolean) : DiscoverAction
    data object OnLoadMore : DiscoverAction
}

internal data class RepositoryUi(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val language: String?,
    val languageColor: String,
    val formattedStars: String,
    val formattedForks: String,
    val isPrivate: Boolean,
    val isFork: Boolean,
    val topics: List<String>,
    val htmlUrl: String,
    val updatedAtFormatted: String
)
