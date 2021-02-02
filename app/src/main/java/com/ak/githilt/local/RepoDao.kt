package com.ak.githilt.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepoDao: BaseDao<RepoCacheEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repoEntity: RepoCacheEntity): Long

    @Query("SELECT * FROM repos WHERE page = :page")
    suspend fun getRepos(page: Int): List<RepoCacheEntity>

    @Query("SELECT * FROM repos WHERE " +
            "name LIKE :queryString OR repoDescription LIKE :queryString " +
            "ORDER BY starsCount DESC, name ASC")
    fun paginatedReposByName(queryString: String): PagingSource<Int, RepoCacheEntity>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()


}