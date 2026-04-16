package app.mak.gitstar.core.data.repository

import app.mak.gitstar.core.domain.model.DataError
import app.mak.gitstar.core.domain.model.GitResult
import app.mak.gitstar.core.domain.model.Owner
import app.mak.gitstar.core.domain.model.Repository
import app.mak.gitstar.core.domain.repository.GitRepoRepository
import app.mak.gitstar.core.remote.GitApi
import app.mak.gitstar.core.remote.model.OwnerDTO
import app.mak.gitstar.core.remote.model.RepositoryDTO
import app.mak.gitstar.core.remote.model.SearchRepositoryDTO

private const val CACHE_TTL_MS = 5 * 60 * 1000L  // 5 minutes

internal class OfflineFirstRepository(
    private val api: GitApi,
): GitRepoRepository {
    override suspend fun getTrendingRepos(page: Int): GitResult<List<Repository>, DataError> {
        // Try cache first on page 1
//        if (page == 1) {
//            val threshold = System.currentTimeMillis() - CACHE_TTL_MS
//            val cached = dao.getFreshRepositories(username, threshold)
//            if (cached.isNotEmpty()) {
//                return Result.Success(cached.map { it.toRepository() })
//            }
//        }
        return when(val result = api.getRepositories(page)) {
            is GitResult.Error<DataError> -> {
                GitResult.Error(result.error)
            }
            is GitResult.Success<SearchRepositoryDTO> -> {
                val r = result.data.items?.map { dto ->
                    dto.toRepository()
                } ?: emptyList()
                GitResult.Success(r)
            }
        }
    }

}

private fun RepositoryDTO.toRepository() = Repository(
    id = id,
    name = name.orEmpty(),
    fullName = fullName.orEmpty(),
    description = description,
    owner = owner?.toOwner(),
    language = language,
    stargazersCount = stargazersCount ?: 0,
    forksCount = forksCount ?: 0,
    watchersCount = watchersCount ?: 0,
    openIssuesCount = openIssuesCount ?: 0,
    isPrivate = isPrivate ?: false,
    isFork = fork ?: false,
    topics = topics ?: emptyList(),
    htmlUrl = htmlUrl.orEmpty(),
    defaultBranch = defaultBranch.orEmpty(),
    pushedAt = pushedAt,
    updatedAt = updatedAt,
    license = license?.name,
    hasWiki = hasWiki ?: false,
    hasIssues = hasIssues ?: false
)

fun OwnerDTO.toOwner(): Owner = Owner(
    id = id,
    login = login.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    htmlUrl = htmlUrl.orEmpty(),
    type = type.orEmpty()
)
