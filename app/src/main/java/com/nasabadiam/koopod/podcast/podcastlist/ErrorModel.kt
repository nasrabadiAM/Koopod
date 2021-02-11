package com.nasabadiam.koopod.podcast.podcastlist

sealed class ErrorModel(open val throwable: Throwable) {

    class Server(override val throwable: Throwable) : ErrorModel(throwable)
    class Http(code: Int, override val throwable: Throwable) : ErrorModel(throwable)
    class Network(override val throwable: Throwable) : ErrorModel(throwable)
    class Unknown(override val throwable: Throwable) : ErrorModel(throwable)
    class Database(override val throwable: Throwable) : ErrorModel(throwable)
    class EmptyResult(override val throwable: Throwable) : ErrorModel(throwable)
}

class DuplicatePodcastException(
    message: String = DUPLICATE_SUBSCRIBE_ERROR
) : Throwable(message) {
    companion object {
        const val DUPLICATE_SUBSCRIBE_ERROR = "Podcast has subscribed already!"
    }
}

class NoSuchItemException(
    message: String = DUPLICATE_SUBSCRIBE_ERROR
) : Throwable(message) {
    companion object {
        const val DUPLICATE_SUBSCRIBE_ERROR = "No result found!"
    }
}