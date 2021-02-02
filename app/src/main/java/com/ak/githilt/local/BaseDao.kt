package com.ak.githilt.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntities(entities: List<T>)

    @Update
    suspend fun updateEntity(entity: T)

    @Delete
    suspend fun deleteEntity(entity: T)

}