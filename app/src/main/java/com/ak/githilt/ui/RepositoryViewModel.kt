package com.ak.githilt.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ak.githilt.model.Repo
import com.ak.githilt.repository.GithubRepoRepository
import com.ak.githilt.util.DataState
import kotlinx.coroutines.Dispatchers
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

    fun setStateEvent(repoStateEvent: RepoStateEvent){
        viewModelScope.launch{
            when(repoStateEvent){
                is RepoStateEvent.GetRepoEvents -> {
                    githubRepoRepository.getRepositories()
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
    object GetRepoEvents: RepoStateEvent()
    object None: RepoStateEvent()
}