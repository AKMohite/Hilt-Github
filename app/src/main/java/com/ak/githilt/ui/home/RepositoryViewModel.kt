package com.ak.githilt.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import com.ak.githilt.model.Repo
import com.ak.githilt.repository.GithubRepoRepository
import com.ak.githilt.util.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class RepositoryViewModel
@ViewModelInject constructor(
    private val githubRepoRepository: GithubRepoRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _dataState: MutableLiveData<DataState<List<Repo>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Repo>>>
        get() = _dataState

    private val currentQuery: MutableStateFlow<String> = MutableStateFlow(DEFAULT_SEARCH_QUERY)

    val repos = currentQuery.flatMapLatest { query ->
        githubRepoRepository.getPaginatedRepositories(query)
            .map {pagingData ->
                pagingData.map { entity ->
                    Repo(
                        id = entity.id,
                        name = entity.name,
                        fullName = entity.fullName,
                        repoDescription = entity.repoDescription,
                        repoUrl = entity.repoUrl,
                        starsCount = entity.starsCount,
                        forksCount = entity.forksCount,
                        language = entity.language,
                        page = entity.page

                    )
                }

            }
            .cachedIn(viewModelScope)
    }

    companion object{
        private const val DEFAULT_SEARCH_QUERY = "android"
    }

    fun searchRepo(query: String){
        currentQuery.value = query
    }

    fun setStateEvent(repoStateEvent: RepoStateEvent){
        viewModelScope.launch{
            when(repoStateEvent){
                is RepoStateEvent.GetRepoEvents -> {
                    githubRepoRepository.getRepositories(repoStateEvent.repoQuery, repoStateEvent.pageNo) // TODO pagination
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }

                is RepoStateEvent.None -> {

                }
            }
        }
    }

}

sealed class RepoStateEvent{
    data class GetRepoEvents(val repoQuery: String, val pageNo: Int): RepoStateEvent()
    object None: RepoStateEvent()
}