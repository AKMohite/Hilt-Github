package app.mak.gitstar.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class CoroutineDispatcherProvider: DispatcherProvider {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val computation: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val main: CoroutineDispatcher
}
