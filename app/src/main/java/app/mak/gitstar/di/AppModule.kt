package app.mak.gitstar.di

import app.mak.gitstar.core.data.di.dataModule
import app.mak.gitstar.core.domain.di.domainModule
import app.mak.gitstar.core.remote.di.remoteModule
import app.mak.gitstar.features.discover.presentation.di.discoverViewModel

val appModules = listOf(
    domainModule,

    remoteModule,

    dataModule,

    discoverViewModel,
)