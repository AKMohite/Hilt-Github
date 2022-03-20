package com.ak.githilt.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ak.githilt.local.CacheMapper
import com.ak.githilt.model.Repo
import com.ak.githilt.remote.GithubAPIService
import com.ak.githilt.remote.NetworkMapper
import com.ak.githilt.remote.PER_PAGE_ITEMS
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE = 1
class RepoPagingSource(
    private val apiService: GithubAPIService,
    private val query: String,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
): PagingSource<Int, Repo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {

        return try {
            val position = params.key ?: GITHUB_STARTING_PAGE

            val response = apiService.searchRepos(query, position, PER_PAGE_ITEMS)

            response.items.forEach { repo -> repo.page = position }
            val repos = networkMapper.mapFromEntityList(response.items)
            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (error: IOException){
            LoadResult.Error(error)
        } catch (error: HttpException){
            LoadResult.Error(error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return 0
    }

}