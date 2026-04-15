package app.mak.gitstar.features.discover.presentation.di

import app.mak.gitstar.features.discover.presentation.DiscoverViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val discoverViewModel = module {
    viewModelOf(::DiscoverViewModel)
}