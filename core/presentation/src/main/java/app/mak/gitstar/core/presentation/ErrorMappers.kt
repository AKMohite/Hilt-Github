package app.mak.gitstar.core.presentation

import app.mak.gitstar.core.domain.model.AuthError
import app.mak.gitstar.core.domain.model.DataError

fun DataError.toUiText(): UiText = when (this) {
//    DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
//    DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
//    DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.error_unauthorized)
//    DataError.Network.FORBIDDEN -> UiText.StringResource(R.string.error_forbidden)
//    DataError.Network.NOT_FOUND -> UiText.StringResource(R.string.error_not_found)
//    DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_rate_limit)
//    DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_timeout)
//    DataError.Network.SERIALIZATION -> UiText.StringResource(R.string.error_serialization)
//    DataError.Local.DISK_FULL -> UiText.StringResource(R.string.error_disk_full)
//    DataError.Local.NOT_FOUND -> UiText.StringResource(R.string.error_not_found_local)
    else -> UiText.StringResource(R.string.error_unknown)
}

fun AuthError.toUiText(): UiText = when (this) {
//    AuthError.NOT_AUTHENTICATED -> UiText.StringResource(R.string.error_not_authenticated)
//    AuthError.TOKEN_EXPIRED -> UiText.StringResource(R.string.error_token_expired)
//    AuthError.OAUTH_CANCELLED -> UiText.StringResource(R.string.error_oauth_cancelled)
//    AuthError.OAUTH_FAILED -> UiText.StringResource(R.string.error_oauth_failed)
//    AuthError.UNKNOWN -> UiText.StringResource(R.string.error_unknown)
    else -> UiText.StringResource(R.string.error_unknown)
}
