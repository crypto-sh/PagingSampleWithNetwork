package com.paging.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.paging.api.GithubService
import com.paging.api.searchRepos
import com.paging.model.NetworkState
import com.paging.model.RepoSearch

class RepositoryDataSource(
    private val query   : String,
    private val pageSize : Int,
    private val service : GithubService)  : PageKeyedDataSource<Int,RepoSearch>(){

    private var lastRequestedPage = 1
    private var isRequestInProgress = false
    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, RepoSearch>) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, pageSize,
            { error ->
                networkState.postValue(NetworkState.error(error))
                isRequestInProgress = false

            }, { repos ->
                //cache.insert(repos) {
                callback.onResult(repos,lastRequestedPage,lastRequestedPage + 1)
                lastRequestedPage++
                isRequestInProgress = false

//            }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepoSearch>) {
        requestAndSaveData(query,callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepoSearch>) {
        //requestAndSaveData(query,callback)
    }

    private fun requestAndSaveData(query: String, callback: LoadCallback<Int, RepoSearch>) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, pageSize,
            { error ->
                networkState.postValue(NetworkState.error(error))
                isRequestInProgress = false

            }, { repos ->
                //cache.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
                callback.onResult(repos,lastRequestedPage)
//            }
            })
    }

    class Factory(
        private val query   : String,
        private val pageSize: Int,
        private val service : GithubService) : DataSource.Factory<Int,RepoSearch>() {

        val liveDataSource = MutableLiveData<RepositoryDataSource>()

        override fun create(): DataSource<Int, RepoSearch> {
            val source = RepositoryDataSource(query, pageSize, service)
            liveDataSource.postValue(source)
            return source

        }
    }
}