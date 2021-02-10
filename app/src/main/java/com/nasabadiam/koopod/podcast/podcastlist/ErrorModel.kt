package com.nasabadiam.koopod.podcast.podcastlist

sealed class ErrorModel(open val throwable: Throwable) {

    class Server(override val throwable: Throwable) : ErrorModel(throwable)
    class Http(code: Int,override val  throwable: Throwable) : ErrorModel(throwable)
    class Network(override val throwable: Throwable) : ErrorModel(throwable)
    class Unknown(override val throwable: Throwable) : ErrorModel(throwable)
    class Database(override val throwable: Throwable) : ErrorModel(throwable)
}