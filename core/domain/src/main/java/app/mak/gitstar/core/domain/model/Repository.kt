package app.mak.gitstar.core.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val owner: Owner?,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val isPrivate: Boolean,
    val isFork: Boolean,
    val topics: List<String>,
    val htmlUrl: String,
    val defaultBranch: String,
    val pushedAt: String?,
    val updatedAt: String?,
    val license: String?,
    val hasWiki: Boolean,
    val hasIssues: Boolean
)

data class Owner(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val type: String  // "User" or "Organization"
)
