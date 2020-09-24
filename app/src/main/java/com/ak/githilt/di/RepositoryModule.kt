package com.ak.githilt.di

import com.ak.githilt.local.CacheMapper
import com.ak.githilt.local.RepoDao
import com.ak.githilt.remote.GithubAPIService
import com.ak.githilt.remote.NetworkMapper
import com.ak.githilt.repository.GithubRepoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGithubRepoRepository(
        repoDao: RepoDao,
        githubAPIService: GithubAPIService,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): GithubRepoRepository = GithubRepoRepository(
        repoDao, githubAPIService, cacheMapper, networkMapper
    )

}