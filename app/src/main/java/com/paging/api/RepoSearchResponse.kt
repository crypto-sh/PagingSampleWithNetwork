package com.paging.api

import com.google.gson.annotations.SerializedName
import com.paging.model.RepoSearch


/**
 * Data class to hold repo responses from searchRepo API calls.
 */
data class RepoSearchResponse(
    @SerializedName("total_count") val total: Int = 0,

    @SerializedName("items") val items: List<RepoSearch> = emptyList(),

    val nextPage: Int? = null
)
