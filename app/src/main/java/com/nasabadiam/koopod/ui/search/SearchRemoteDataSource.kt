package com.nasabadiam.koopod.ui.search

import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel
import com.nasabadiam.koopod.podcast.podcastlist.Result
import com.nasabadiam.koopod.podcast.podcastlist.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val searchApiService: SearchService,
    private val iODispatcher: CoroutineDispatcher
) {
    suspend fun search(query: String): Flow<Result<List<PodcastModel>>> = flow {
        val result = safeApiCall(iODispatcher,
            apiCall = {
                searchApiService.search(query)
            },
            map = {
                it.toPodcastModel()
            })
        emit(result)
    }
}