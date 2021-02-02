package com.ak.githilt.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RepoCacheEntity::class, RemoteKeysEntity::class],
    version = 1
)
abstract class GithubDatabase: RoomDatabase() {

    abstract fun reposDao(): RepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object{

        const val DATABASE_NAME: String = "github_repo_db"

    }
}