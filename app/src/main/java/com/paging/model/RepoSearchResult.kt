package com.paging.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class RepoSearchResult(
    val data : LiveData<PagedList<RepoSearch>>,
    val network : LiveData<NetworkState>
)