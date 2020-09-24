package com.ak.githilt.model

data class Repo (
    val id: Long,
    val name: String,
    val fullName: String,
    val repoDescription: String?,
    val repoUrl: String,
    val starsCount: Int,
    val forksCount: Int,
    val language: String?
)