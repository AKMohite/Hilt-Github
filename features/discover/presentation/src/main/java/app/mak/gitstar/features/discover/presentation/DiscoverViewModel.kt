package app.mak.gitstar.features.discover.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mak.gitstar.core.domain.model.onFailure
import app.mak.gitstar.core.domain.model.onSuccess
import app.mak.gitstar.core.domain.repository.GitRepoRepository
import app.mak.gitstar.features.discover.presentation.model.DiscoverAction
import app.mak.gitstar.features.discover.presentation.model.DiscoverEvent
import app.mak.gitstar.features.discover.presentation.model.DiscoverState
import app.mak.gitstar.features.discover.presentation.model.RepoSort
import app.mak.gitstar.features.discover.presentation.model.toRepositoryUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DiscoverViewModel(
    private val gitRepo: GitRepoRepository
): ViewModel() {

    private val _state = MutableStateFlow(DiscoverState())
    val state = _state.asStateFlow()

    private val _events = Channel<DiscoverEvent>()
    val events = _events.receiveAsFlow()

    private var currentPage = 1
    private var canLoadMore = true

    init {
        loadTrendingRepositories()
    }

    fun onAction(action: DiscoverAction) {
        when(action) {
            DiscoverAction.OnLoadMore -> {}
            DiscoverAction.OnRefresh -> {}
            is DiscoverAction.OnRepoClick -> {}
            is DiscoverAction.OnSortChange -> {}
            is DiscoverAction.OnToggleForked -> {}
        }
    }

    private fun loadTrendingRepositories(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _state.update { if (isRefresh) it.copy(isRefreshing = true) else it.copy(isLoading = true) }
            gitRepo.getTrendingRepos( page = 1)
                .onSuccess { repos ->
                    val uiModels = repos.map { it.toRepositoryUi() }
                    _state.update { it.copy(repos = uiModels, isLoading = false, isRefreshing = false, error = null) }
                    applySort()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, isRefreshing = false) }
//                    _events.send(DiscoverEvent.ShowError(error.toUiText()))
                }
        }
    }

    private fun applySort() {
        _state.update { state ->
            val sorted = when (state.sortBy) {
                RepoSort.UPDATED -> state.repos.sortedByDescending { it.updatedAtFormatted }
                RepoSort.STARS -> state.repos.sortedByDescending { it.formattedStars }
                RepoSort.FORKS -> state.repos.sortedByDescending { it.formattedForks }
                RepoSort.NAME -> state.repos.sortedBy { it.name }
            }
            state.copy(repos = sorted)
        }
    }

}
