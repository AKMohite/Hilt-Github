package app.mak.gitstar.core.domain.model

data class Release(
    val id: Long,
    val tagName: String,
    val name: String?,
    val body: String?,
    val isDraft: Boolean,
    val isPrerelease: Boolean,
    val publishedAt: String?,
    val htmlUrl: String,
    val author: ReleaseAuthor,
    val assets: List<ReleaseAsset>
)

data class ReleaseAuthor(
    val login: String,
    val avatarUrl: String
)

data class ReleaseAsset(
    val name: String,
    val downloadCount: Int,
    val size: Long,
    val browserDownloadUrl: String,
    val contentType: String
)
