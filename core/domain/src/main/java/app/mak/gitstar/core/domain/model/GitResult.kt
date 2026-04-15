package app.mak.gitstar.core.domain.model

interface GitError

sealed interface GitResult<out D, out E : GitError> {
    data class Success<out D>(val data: D) : GitResult<D, Nothing>
    data class Error<out E : GitError>(val error: E) : GitResult<Nothing, E>
}

typealias EmptyResult<E> = GitResult<Unit, E>

inline fun <T, E : GitError, R> GitResult<T, E>.map(map: (T) -> R): GitResult<R, E> = when (this) {
    is GitResult.Error -> GitResult.Error(error)
    is GitResult.Success -> GitResult.Success(map(data))
}

inline fun <T, E : GitError> GitResult<T, E>.onSuccess(action: (T) -> Unit): GitResult<T, E> {
    if (this is GitResult.Success) action(data)
    return this
}

inline fun <T, E : GitError> GitResult<T, E>.onFailure(action: (E) -> Unit): GitResult<T, E> {
    if (this is GitResult.Error) action(error)
    return this
}

fun <T, E : GitError> GitResult<T, E>.asEmptyResult(): EmptyResult<E> = map { }
