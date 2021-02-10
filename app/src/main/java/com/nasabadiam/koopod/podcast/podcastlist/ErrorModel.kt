package com.nasabadiam.koopod.podcast.podcastlist

sealed class ErrorModel(throwable: Throwable) {

    class Server(throwable: Throwable) : ErrorModel(throwable)
    class Http(code: Int, throwable: Throwable) : ErrorModel(throwable)
    class Network(throwable: Throwable) : ErrorModel(throwable)
    class Unknown(throwable: Throwable) : ErrorModel(throwable)
}