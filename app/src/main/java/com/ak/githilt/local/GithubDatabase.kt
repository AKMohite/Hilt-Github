package com.ak.githilt.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RepoCacheEntity::class],
    version = 1
)
abstract class GithubDatabase: RoomDatabase() {

    abstract fun repoDao(): RepoDao

    companion object{

        val DATABASE_NAME: String = "github_repo_db"

    }
}