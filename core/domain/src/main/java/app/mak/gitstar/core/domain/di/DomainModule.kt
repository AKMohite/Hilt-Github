package app.mak.gitstar.core.domain.di

import app.mak.gitstar.core.domain.CoroutineDispatcherProvider
import app.mak.gitstar.core.domain.DispatcherProvider
import org.koin.dsl.module

val domainModule = module{
    single<DispatcherProvider> { CoroutineDispatcherProvider() }
}