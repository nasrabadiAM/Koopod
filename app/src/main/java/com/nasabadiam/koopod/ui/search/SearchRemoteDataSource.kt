package com.nasabadiam.koopod.ui.search

import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val searchApiService: SearchService
) {
    suspend fun search(query: String): Flow<List<PodcastModel>> = flow {
        emit(searchApiService.search(query).toItems())
    }
}