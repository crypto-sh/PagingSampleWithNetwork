package com.paging.data

import androidx.lifecycle.MutableLiveData
import com.paging.api.GithubService
import com.paging.api.searchRepos
import com.paging.model.NetworkState
import com.paging.model.RepoSearch
import com.paging.model.RepoSearchResult


class GithubRepository(private val service: GithubService) {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    private var lastRequestedPage = 1

    val networkErrors = MutableLiveData<NetworkState>()
    val data = MutableLiveData<List<RepoSearch>>()

    private var isRequestInProgress = false

//    fun search(query: String): RepoSearchResult {
//        networkErrors.postValue(NetworkState.LOADING)
//        requestAndSaveData(query)
//        return RepoSearchResult(data, networkErrors)
//    }

    fun requestNext(query: String){
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE,
            { error ->
                networkErrors.postValue(NetworkState.error(error))
                isRequestInProgress = false

            }, { repos ->
                //cache.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
                data.postValue(repos)
//            }
            })
    }
}