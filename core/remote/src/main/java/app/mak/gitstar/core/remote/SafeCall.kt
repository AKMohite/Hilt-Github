package app.mak.gitstar.core.remote

import app.mak.gitstar.core.domain.model.DataError
import app.mak.gitstar.core.domain.model.GitResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): GitResult<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return GitResult.Error(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        return GitResult.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        return GitResult.Error(DataError.Network.UNKNOWN)
    }
    return responseToGitResult(response)
}

suspend inline fun <reified T> responseToGitResult(response: HttpResponse): GitResult<T, DataError.Network> =
    when (response.status.value) {
        in 200..299 -> GitResult.Success(response.body<T>())
        401 -> GitResult.Error(DataError.Network.UNAUTHORIZED)
        403 -> GitResult.Error(DataError.Network.FORBIDDEN)
        404 -> GitResult.Error(DataError.Network.NOT_FOUND)
        408 -> GitResult.Error(DataError.Network.REQUEST_TIMEOUT)
        409 -> GitResult.Error(DataError.Network.CONFLICT)
        413 -> GitResult.Error(DataError.Network.PAYLOAD_TOO_LARGE)
        422 -> GitResult.Error(DataError.Network.BAD_REQUEST)
        429 -> GitResult.Error(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> GitResult.Error(DataError.Network.SERVER_ERROR)
        else -> GitResult.Error(DataError.Network.UNKNOWN)
    }