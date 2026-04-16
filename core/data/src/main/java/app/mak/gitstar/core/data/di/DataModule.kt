package app.mak.gitstar.core.data.di

import app.mak.gitstar.core.data.repository.OfflineFirstRepository
import app.mak.gitstar.core.domain.repository.GitRepoRepository
import org.koin.dsl.module

val dataModule = module {
    factory<GitRepoRepository> { OfflineFirstRepository(api = get()) }
}