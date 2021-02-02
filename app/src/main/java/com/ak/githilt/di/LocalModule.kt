package com.ak.githilt.di

import android.content.Context
import androidx.room.Room
import com.ak.githilt.local.GithubDatabase
import com.ak.githilt.local.RepoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object LocalModule {

    @Singleton
    @Provides
    fun provideGithubDb(@ApplicationContext context: Context): GithubDatabase = Room
        .databaseBuilder(
            context,
            GithubDatabase::class.java,
            GithubDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideRepoDao(githubDatabase: GithubDatabase): RepoDao = githubDatabase.reposDao()

}