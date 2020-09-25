package com.ak.githilt.remote

import retrofit2.http.GET
import retrofit2.http.Query

const val PER_PAGE_ITEMS = 30
interface GithubAPIService {

    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(
        @Query("q") repoQuery: String,
        @Query("page") pageNo: Int,
        @Query("per_page") noOfRepos: Int = PER_PAGE_ITEMS
    ): RepoSearchResponse
}