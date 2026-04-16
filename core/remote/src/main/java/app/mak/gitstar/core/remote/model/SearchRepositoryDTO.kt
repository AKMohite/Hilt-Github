package app.mak.gitstar.core.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRepositoryDTO(
    @SerialName("incomplete_results")
    val incompleteResults: Boolean? = null,
    @SerialName("items")
    val items: List<RepositoryDTO>? = null,
    @SerialName("total_count")
    val totalCount: Int? = null
)