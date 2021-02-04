package com.nasabadiam.koopod.ui.search

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search")
    suspend fun search(
        @Query("term") query: String,
        @Query("country") country: String = "US",
        @Query("media") media: String = "podcast",
        @Query("entity") entity: String = "podcast"
    ): SearchResponseDto
}