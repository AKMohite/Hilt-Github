package com.ak.githilt.remote

import com.google.gson.annotations.SerializedName

/**
 * Data class to hold repo responses from searchRepo API calls.
 */
data class RepoSearchResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<RepoInfo>,
    @SerializedName("total_count")
    val totalCount: Int
)