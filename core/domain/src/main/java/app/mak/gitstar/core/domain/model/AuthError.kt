package app.mak.gitstar.core.domain.model

enum class AuthError : GitError {
    NOT_AUTHENTICATED,
    TOKEN_EXPIRED,
    OAUTH_CANCELLED,
    OAUTH_FAILED,
    UNKNOWN
}