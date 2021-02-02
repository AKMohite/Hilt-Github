package com.ak.githilt.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ak.githilt.local.CacheMapper
import com.ak.githilt.local.GithubDatabase
import com.ak.githilt.local.RemoteKeysEntity
import com.ak.githilt.local.RepoCacheEntity
import com.ak.githilt.remote.GithubAPIService
import com.ak.githilt.remote.NetworkMapper
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


private const val GITHUB_STARTING_PAGE = 1
@ExperimentalPagingApi
class GithubRemoteMediator(
    private val query: String,
    private val service: GithubAPIService,
    private val repoDatabase: GithubDatabase,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
): RemoteMediator<Int, RepoCacheEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoCacheEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                // If the previous key is null, then we can't request more data
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }
        val apiQuery = query /*+ IN_QUALIFIER*/

        try {
            val apiResponse = service.searchRepos(apiQuery, page, state.config.pageSize)

            val repos = apiResponse.items
            val endOfPaginationReached = repos.isEmpty()
            val reposNetworkMap = networkMapper.mapFromEntityList(repos)
            val reposCacheMap = cacheMapper.mapToEntityList(reposNetworkMap)
            repoDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    repoDatabase.remoteKeysDao().clearRemoteKeys()
                    repoDatabase.reposDao().clearRepos()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeysEntity(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                repoDatabase.remoteKeysDao().insertEntities(keys)
                repoDatabase.reposDao().insertEntities(reposCacheMap)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }


    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RepoCacheEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepoCacheEntity>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RepoCacheEntity>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repoDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }


}