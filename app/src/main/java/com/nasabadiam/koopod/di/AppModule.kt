package com.nasabadiam.koopod.di

import android.content.Context
import androidx.room.Room
import com.nasabadiam.koopod.BuildConfig
import com.nasabadiam.koopod.podcast.podcastlist.PodcastDatabase
import com.nasabadiam.koopod.podcast.podcastlist.PodcastLocalDataSource
import com.nasabadiam.koopod.ui.search.SearchRemoteDataSource
import com.nasabadiam.koopod.ui.search.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePodcastLocalDataSource(
        database: PodcastDatabase
    ): PodcastLocalDataSource {
        return PodcastLocalDataSource(database.podcastDao())
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): PodcastDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PodcastDatabase::class.java,
            "podcast.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideItunesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://itunes.apple.com/")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit) = retrofit.create(SearchService::class.java)


    @Provides
    @Singleton
    fun bindSearchRemoteDataSource(
        searchApiService: SearchService
    ): SearchRemoteDataSource {
        return SearchRemoteDataSource(searchApiService)
    }
}