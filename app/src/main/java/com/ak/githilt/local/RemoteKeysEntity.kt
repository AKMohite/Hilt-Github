package com.ak.githilt.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val repoId: Long,
    @ColumnInfo(name = "previous_key")
    val prevKey: Int?,
    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)
