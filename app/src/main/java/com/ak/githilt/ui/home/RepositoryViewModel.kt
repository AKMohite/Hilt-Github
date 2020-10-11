package com.ak.githilt.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ak.githilt.model.Repo
import com.ak.githilt.repository.GithubRepoRepository
import com.ak.githilt.util.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class RepositoryViewModel
@ViewModelInject constructor(
    private val githubRepoRepository: GithubRepoRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _dataState: MutableLiveData<DataState<List<Repo>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Repo>>>
        get() = _dataState

    private val currentQuery: MutableLiveData<String> = MutableLiveData(DEFAULT_SEARCH_QUERY)

    val repos = currentQuery.switchMap { query ->
        githubRepoRepository.getPaginatedRepositories(query)
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