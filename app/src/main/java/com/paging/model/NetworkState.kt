package com.paging.model


enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState constructor(
    val status: Status,
    val msg: String? = null
) {

    companion object {
        public val LOADED = NetworkState(Status.SUCCESS)
        public val LOADING = NetworkState(Status.RUNNING)
        public fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}