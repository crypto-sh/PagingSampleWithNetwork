package com.paging.api

import com.paging.model.RepoSearch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GithubService {

    @GET("search/repositories?sort=stars")
    fun searchRepos(@Query("q") query : String,
                    @Query("page") page : Int,
                    @Query("per_page") ItemsPerPage : Int) : Call<RepoSearchResponse>

    companion object {

        private const val BASE_URL = "https://api.github.com/"

        fun create() : GithubService {

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService::class.java)

        }
    }
}


private const val IN_QUALIFIER = "in:name,description"


fun searchRepos(
    service         : GithubService,
    query           : String,
    page            : Int,
    itemsPerPage    : Int,
    onError         : (error : String) -> Unit,
    onSuccess       : (repos : List<RepoSearch>) -> Unit) {
    val apiQuery = query + IN_QUALIFIER

    service.searchRepos(apiQuery,page,itemsPerPage).enqueue(object : Callback<RepoSearchResponse> {

        override fun onFailure(call: Call<RepoSearchResponse>, t: Throwable) {
            onError(t.message ?: "unknown error")
        }

        override fun onResponse(call: Call<RepoSearchResponse>, response: Response<RepoSearchResponse>) {
            if (response.isSuccessful){
                response.body().let {
                    if (it != null)
                        onSuccess(it.items)
                    else
                        onError("empty response")
                }
            }else{
                onError("error connecting server")
            }
        }
    })

}