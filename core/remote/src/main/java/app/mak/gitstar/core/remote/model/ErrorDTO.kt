package app.mak.gitstar.core.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    @SerialName("code")
    val code: String? = null,
    @SerialName("field")
    val `field`: String? = null,
    @SerialName("resource")
    val resource: String? = null
)