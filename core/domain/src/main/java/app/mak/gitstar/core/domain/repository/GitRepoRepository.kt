package app.mak.gitstar.core.domain.repository

import app.mak.gitstar.core.domain.model.DataError
import app.mak.gitstar.core.domain.model.GitResult
import app.mak.gitstar.core.domain.model.Owner
import app.mak.gitstar.core.domain.model.Release
import app.mak.gitstar.core.domain.model.Repository

interface GitRepoRepository {
    suspend fun getTrendingRepos(page: Int = 1): GitResult<List<Repository>, DataError>
//    suspend fun getUserRepos(username: String, page: Int = 1): GitResult<List<Repository>, DataError>
//    suspend fun getRepository(owner: String, repo: String): GitResult<Repository, DataError>
//    suspend fun getReleases(owner: String, repo: String, page: Int = 1): GitResult<List<Release>, DataError>
//    suspend fun getReadme(owner: String, repo: String): GitResult<String, DataError>
//    suspend fun getContributors(owner: String, repo: String): GitResult<List<Owner>, DataError>
}