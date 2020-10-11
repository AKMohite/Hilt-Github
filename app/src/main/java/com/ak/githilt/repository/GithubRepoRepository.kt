package com.ak.githilt.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ak.githilt.data.RepoPagingSource
import com.ak.githilt.local.CacheMapper
import com.ak.githilt.local.RepoDao
import com.ak.githilt.model.Repo
import com.ak.githilt.remote.GithubAPIService
import com.ak.githilt.remote.NetworkMapper
import com.ak.githilt.remote.PER_PAGE_ITEMS
import com.ak.githilt.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GithubRepoRepository constructor(
    private val repoDao: RepoDao,
    private val githubAPIService: GithubAPIService,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {

    suspend fun getRepositories(repoQuery: String, pageNo: Int): Flow<DataState<List<Repo>>> = flow {
        emit(DataState.Loading)
        try {

            var cacheRepos = repoDao.getRepos(pageNo)
            if (cacheRepos.isNullOrEmpty()) {
                val networkRepos = githubAPIService.searchRepos(repoQuery, pageNo, PER_PAGE_ITEMS)
                networkRepos.items.forEach { repo -> repo.page = 1 }
                val repos = networkMapper.mapFromEntityList(networkRepos.items)
                for (repo in repos) {
                    repoDao.insert(cacheMapper.mapToEntity(repo))
                }

                cacheRepos = repoDao.getRepos(pageNo)
            }
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheRepos)))
        } catch (e: Exception){
            emit(DataState.Error(e))
        }
    }

    fun getPaginatedRepositories(query: String): LiveData<PagingData<Repo>>{

        return Pager(
            config = PagingConfig(
                pageSize = PER_PAGE_ITEMS,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepoPagingSource(githubAPIService, query, cacheMapper, networkMapper) }
        ).liveData
    }
}