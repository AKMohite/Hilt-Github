package app.mak.gitstar.core.remote

import app.mak.gitstar.core.domain.DispatcherProvider
import app.mak.gitstar.core.domain.model.DataError
import app.mak.gitstar.core.domain.model.GitResult
import app.mak.gitstar.core.remote.model.SearchRepositoryDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

internal class KtorApi(
    private val client: HttpClient,
    private val dispatcher: DispatcherProvider
): GitApi {
    override suspend fun getRepositories(page: Int): GitResult<SearchRepositoryDTO, DataError.Network> = withContext(dispatcher.io) {
        safeCall { client.get("search/repositories?q=Android&page=$page").body() }
    }
}

interface GitApi {
    suspend fun getRepositories(page: Int = 1): GitResult<SearchRepositoryDTO, DataError.Network>
}
