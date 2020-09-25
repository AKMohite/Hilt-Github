package com.ak.githilt.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class RepoCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "fullName")
    var fullName: String,

    @ColumnInfo(name = "repoDescription")
    var repoDescription: String?,

    @ColumnInfo(name = "repoUrl")
    var repoUrl: String,

    @ColumnInfo(name = "starsCount")
    var starsCount: Int,

    @ColumnInfo(name = "forksCount")
    var forksCount: Int,

    @ColumnInfo(name = "language")
    var language: String?,

    @ColumnInfo(name = "page")
    var page: Int
)