package app.mak.gitstar.core.remote.di

import android.util.Log
import app.mak.gitstar.core.remote.GitApi
import app.mak.gitstar.core.remote.KtorApi
import app.mak.gitstar.core.remote.exception.ExceptionType
import app.mak.gitstar.core.remote.exception.GitAPIException
import app.mak.gitstar.core.remote.exception.UnknownAPIException
import app.mak.gitstar.core.remote.model.APIErrorDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


val remoteModule = module {
    single { createHttpClient() }
    single<GitApi> { KtorApi(client = get(), dispatcher = get()) }
}


//internal fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

internal fun createHttpClient(enableNetworkLogs: Boolean = false): HttpClient {
    return HttpClient{
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = API_HOST
                header("X-ListenAPI-Key", API_KEY)
//                path("api/")
//                parametersOf("api_key", "")
            }
        }

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, request ->
                /*val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response
                if (exceptionResponse.status == HttpStatusCode.NotFound) {
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    throw MissingPageException(exceptionResponse, exceptionResponseText)
                }*/
                Log.d(":", "$request")
                throw handleKtorExceptions(exception) ?: UnknownAPIException(throwable = exception)
            }
        }
    }
}

private suspend fun handleKtorExceptions(exception: Throwable): Throwable? {
//        todo check for ktor exceptions instead java
    return when (exception) {
        is ClientRequestException -> {
            handleAPIExceptions(exception)
        }
//            is java.net.SocketTimeoutException -> RequestTimeoutException(throwable = exception)
//            is java.io.IOException -> NoNetworkException()
        else -> null
    }
}

private suspend fun handleAPIExceptions(exception: ClientRequestException): Throwable? {
    val exceptionResponse = exception.response
    val error = getErrorDTO(exceptionResponse)
    return GitAPIException(
        code = exceptionResponse.status.value,
        errorMsg = error?.message ?: ExceptionType.UNKNOWN.message,
        throwable = exception
    )
}

/*suspend inline fun <T> safeApiCall(responseFunction: () -> T): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(responseFunction.invoke()) // Or responseFunction()
    } catch (e: Throwable) {
        ResultWrapper.Error<T>(e.message ?: ExceptionType.UNKNOWN.message, e)
    }
}*/

private suspend fun getErrorDTO(exceptionResponse: HttpResponse): APIErrorDTO? {
    return try {
        exceptionResponse.body<APIErrorDTO>()
    } catch (e: Throwable) {
//        might throw json parse exception
        null
    }
}

//const val a = BuildCo
private const val API_HOST = "api.github.com"
//private const val API_HOST = "produrl"
private const val API_KEY = ""