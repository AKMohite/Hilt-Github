package com.ak.githilt.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.ak.githilt.data.GithubRemoteMediator
import com.ak.githilt.data.RepoPagingSource
import com.ak.githilt.local.CacheMapper
import com.ak.githilt.local.GithubDatabase
import com.ak.githilt.local.RepoCacheEntity
import com.ak.githilt.local.RepoDao
import com.ak.githilt.model.Repo
import com.ak.githilt.remote.GithubAPIService
import com.ak.githilt.remote.NetworkMapper
import com.ak.githilt.remote.PER_PAGE_ITEMS
import com.ak.githilt.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

@ExperimentalPagingApi
class GithubRepoRepository constructor(
    private val githubDatabase: GithubDatabase,
    private val githubAPIService: GithubAPIService,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {

    suspend fun getRepositories(repoQuery: String, pageNo: Int): Flow<DataState<List<Repo>>> = flow {
        emit(DataState.Loading)
        try {

            var cacheRepos = githubDatabase.reposDao().getRepos(pageNo)
            if (cacheRepos.isNullOrEmpty()) {
                val networkRepos = githubAPIService.searchRepos(repoQuery, pageNo, PER_PAGE_ITEMS)
                networkRepos.items.forEach { repo -> repo.page = 1 }
                val repos = networkMapper.mapFromEntityList(networkRepos.items)
                for (repo in repos) {
                    githubDatabase.reposDao().insert(cacheMapper.mapToEntity(repo))
                }

                cacheRepos = githubDatabase.reposDao().getRepos(pageNo)
            }
            emit(DataState.Success(cacheMapper.mapFromEntityList(cacheRepos)))
        } catch (e: Exception){
            emit(DataState.Error(e))
        }
    }

    fun getPaginatedRepositories(query: String): Flow<PagingData<RepoCacheEntity>>{

        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory =  { githubDatabase.reposDao().paginatedReposByName(dbQuery)}


        return Pager(
            config = PagingConfig(
                pageSize = PER_PAGE_ITEMS,
                enablePlaceholders = false
            ),
            remoteMediator = GithubRemoteMediator(
                query,
                githubAPIService,
                githubDatabase,
                cacheMapper,
                networkMapper
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}