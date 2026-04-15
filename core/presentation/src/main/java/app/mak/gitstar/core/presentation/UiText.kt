package app.mak.gitstar.core.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    class StringResource(
        @param:StringRes val id: Int,
        val args: Array<Any> = emptyArray()
    ) : UiText

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(id, *args)
    }

    @Composable
    fun asString(): String {
        val context = LocalContext.current
        return asString(context)
    }
}
