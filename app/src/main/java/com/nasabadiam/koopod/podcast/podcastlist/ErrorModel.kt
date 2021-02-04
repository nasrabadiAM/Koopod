package com.nasabadiam.koopod.podcast.podcastlist

sealed class ErrorModel() {

    object Server : ErrorModel()
    class Http(code: Int) : ErrorModel()
    object Network : ErrorModel()
    object Unknown : ErrorModel()
}