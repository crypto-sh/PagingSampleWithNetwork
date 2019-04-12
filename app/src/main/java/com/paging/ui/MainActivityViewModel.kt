package com.paging.ui

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.paging.api.GithubService
import com.paging.data.RepositoryDataSource
import com.paging.model.NetworkState
import com.paging.model.RepoSearch
import com.paging.model.RepoSearchResult



class MainActivityViewModel(private val service: GithubService) : ViewModel() {

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    private val mutableLiveData = MutableLiveData<String>()

    private val repoResult = Transformations.map(mutableLiveData) {
        search(it)
    }

    val data: LiveData<PagedList<RepoSearch>> = Transformations.switchMap(repoResult) {
        it.data
    }

    val network: LiveData<NetworkState> = Transformations.switchMap(repoResult) {
        it.network
    }

    fun searchRepositoeies(query: String) {
        mutableLiveData.postValue(query)
    }

    fun search(query: String): RepoSearchResult {
        val dataSource = RepositoryDataSource.Factory(query, NETWORK_PAGE_SIZE, service)

        val data = LivePagedListBuilder(dataSource, NETWORK_PAGE_SIZE).build()

        return RepoSearchResult(data,
            Transformations.switchMap(dataSource.liveDataSource) {
                it.networkState
            })
    }

    fun lastQueryValue(): String? = mutableLiveData.value

    class Factory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            val service = GithubService.create()

            return MainActivityViewModel(service) as T
        }
    }

}