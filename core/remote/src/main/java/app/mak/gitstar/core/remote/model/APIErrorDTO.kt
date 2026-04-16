package app.mak.gitstar.core.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class APIErrorDTO(
    @SerialName("documentation_url")
    val documentationUrl: String? = null,
    @SerialName("errors")
    val errors: List<ErrorDTO>? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("status")
    val status: String? = null
)