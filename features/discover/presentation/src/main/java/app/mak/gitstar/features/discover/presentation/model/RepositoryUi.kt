package app.mak.gitstar.features.discover.presentation.model

import app.mak.gitstar.core.domain.model.Repository
import kotlinx.coroutines.flow.take

internal data class RepositoryUi(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val language: String?,
    val languageColor: String,
    val formattedStars: String,
    val formattedForks: String,
    val isPrivate: Boolean,
    val isFork: Boolean,
    val topics: List<String>,
    val htmlUrl: String,
    val updatedAtFormatted: String
)

internal val dummyRepositories: List<RepositoryUi> = listOf(
    RepositoryUi(
        id = 1L,
        name = "MyAwesomeApp",
        fullName = "user/MyAwesomeApp",
        description = "A sample Android app built with Jetpack Compose.",
        ownerLogin = "user",
        ownerAvatarUrl = "https://example.com/avatar1.png",
        language = "Kotlin",
        languageColor = "#A97BFF",
        formattedStars = "2.1k",
        formattedForks = "340",
        isPrivate = false,
        isFork = false,
        topics = listOf("android", "compose", "kotlin"),
        htmlUrl = "https://github.com/user/MyAwesomeApp",
        updatedAtFormatted = "Updated Apr 15, 2026"
    ),
    RepositoryUi(
        id = 2L,
        name = "DataStoreExample",
        fullName = "user/DataStoreExample",
        description = "Example of using DataStore in Android Compose.",
        ownerLogin = "user",
        ownerAvatarUrl = "https://example.com/avatar2.png",
        language = "Kotlin",
        languageColor = "#A97BFF",
        formattedStars = "850",
        formattedForks = "93",
        isPrivate = false,
        isFork = false,
        topics = listOf("android", "datastore", "compose"),
        htmlUrl = "https://github.com/user/DataStoreExample",
        updatedAtFormatted = "Updated Mar 28, 2026"
    ),
    RepositoryUi(
        id = 3L,
        name = "RetrofitDemo",
        fullName = "user/RetrofitDemo",
        description = "Basic API integration with Retrofit and Compose.",
        ownerLogin = "user",
        ownerAvatarUrl = "https://example.com/avatar3.png",
        language = "Kotlin",
        languageColor = "#A97BFF",
        formattedStars = "1.2k",
        formattedForks = "180",
        isPrivate = false,
        isFork = true,
        topics = listOf("android", "retrofit", "networking"),
        htmlUrl = "https://github.com/user/RetrofitDemo",
        updatedAtFormatted = "Updated Feb 14, 2026"
    ),
    RepositoryUi(
        id = 4L,
        name = "ComposePlayground",
        fullName = "user/ComposePlayground",
        description = "Sandbox project to experiment with Compose layouts and animations.",
        ownerLogin = "user",
        ownerAvatarUrl = "https://example.com/avatar4.png",
        language = "Kotlin",
        languageColor = "#A97BFF",
        formattedStars = "470",
        formattedForks = "67",
        isPrivate = true,
        isFork = false,
        topics = listOf("compose", "ui", "animation"),
        htmlUrl = "https://github.com/user/ComposePlayground",
        updatedAtFormatted = "Updated Apr 10, 2026"
    )
)

internal fun Repository.toRepositoryUi(): RepositoryUi = RepositoryUi(
    id = id, name = name, fullName = fullName, description = description,
    ownerLogin = owner?.login.orEmpty(), ownerAvatarUrl = owner?.avatarUrl.orEmpty(),
    language = language,
    languageColor = LanguageColors.colorFor(language),
    formattedStars = stargazersCount.formatCount(),
    formattedForks = forksCount.formatCount(),
    isPrivate = isPrivate, isFork = isFork, topics = topics.take(5),
    htmlUrl = htmlUrl,
//    updatedAtFormatted = updatedAt?.formatRelativeTime() ?: ""
    updatedAtFormatted = ""
)

private fun Int.formatCount(): String = when {
    this >= 1_000_000 -> "${this / 1_000_000}M"
    this >= 1_000 -> "${"%.1f".format(this / 1000.0)}k"
    else -> toString()
}

object LanguageColors {
    private val colors = mapOf(
        "Kotlin" to "#A97BFF",
        "Java" to "#B07219",
        "Swift" to "#F05138",
        "Python" to "#3572A5",
        "JavaScript" to "#F1E05A",
        "TypeScript" to "#3178C6",
        "Go" to "#00ADD8",
        "Rust" to "#DEA584",
        "C++" to "#F34B7D",
        "C" to "#555555",
        "C#" to "#178600",
        "Ruby" to "#701516",
        "PHP" to "#4F5D95",
        "Dart" to "#00B4AB",
        "Shell" to "#89E051",
        "HTML" to "#E34C26",
        "CSS" to "#563D7C"
    )
    fun colorFor(lang: String?): String = colors[lang] ?: "#8B949E"
}