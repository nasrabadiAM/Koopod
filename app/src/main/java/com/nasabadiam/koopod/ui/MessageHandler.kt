package com.nasabadiam.koopod.ui

import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.podcast.podcastlist.DuplicatePodcastException
import com.nasabadiam.koopod.podcast.podcastlist.ErrorModel

interface MessageHandler {
    fun handle(errorModel: ErrorModel): Int
}

class GeneralMessageHandler : MessageHandler {

    override fun handle(errorModel: ErrorModel): Int {
        return when (errorModel) {
            is ErrorModel.Server -> R.string.server_connection_error_message
            is ErrorModel.Unknown -> R.string.unknown_error_message
            is ErrorModel.Http -> R.string.general_http_error_message
            is ErrorModel.Network -> R.string.network_error_message
            is ErrorModel.Database -> {
                if (errorModel.throwable is DuplicatePodcastException) {
                    R.string.podcast_already_subscribed
                } else {
                    R.string.unknown_error_message
                }
            }
            is ErrorModel.EmptyResult -> {
                R.string.no_result_found
            }
        }
    }
}
