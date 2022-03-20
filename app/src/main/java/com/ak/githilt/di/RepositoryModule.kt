package com.ak.githilt.di

import androidx.paging.ExperimentalPagingApi
import com.ak.githilt.local.CacheMapper
import com.ak.githilt.local.GithubDatabase
import com.ak.githilt.remote.GithubAPIService
import com.ak.githilt.remote.NetworkMapper
import com.ak.githilt.repository.GithubRepoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Provides
    fun provideGithubRepoRepository(
        githubDatabase: GithubDatabase,
        githubAPIService: GithubAPIService,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): GithubRepoRepository = GithubRepoRepository(
        githubDatabase, githubAPIService, cacheMapper, networkMapper
    )

}