package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T, R> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T,
    map: (T) -> R
): Result<R> {
    return withContext(dispatcher) {
        try {
            Result.Success(map(apiCall.invoke()))
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Result.Error(ErrorModel.Network(throwable))
                is HttpException -> {
                    val code = throwable.code()
                    Result.Error(ErrorModel.Http(code, throwable))
                }
                else -> {
                    Result.Error(ErrorModel.Unknown(throwable))
                }
            }
        }
    }
}