package com.ak.githilt.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repoEntity: RepoCacheEntity): Long

    @Query("SELECT * FROM repos")
    suspend fun getRepos(): List<RepoCacheEntity>
}