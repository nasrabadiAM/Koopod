package com.nasabadiam.koopod.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.nasabadiam.koopod.BuildConfig
import com.nasabadiam.koopod.podcast.podcastlist.*
import com.nasabadiam.koopod.ui.GeneralMessageHandler
import com.nasabadiam.koopod.ui.MessageHandler
import com.nasabadiam.koopod.ui.search.SearchRemoteDataSource
import com.nasabadiam.koopod.ui.search.SearchRepository
import com.nasabadiam.koopod.ui.search.SearchService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchRetrofitService

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RssRetrofitService

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
    fun provideOkHttpClient(@ApplicationContext context: Context) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val chucker = ChuckerInterceptor.Builder(context)
            .alwaysReadResponseBody(true)
            .build()
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chucker)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    @SearchRetrofitService
    fun provideItunesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://itunes.apple.com/")
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @RssRetrofitService
    fun provideRssRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(
                TikXmlConverterFactory.create(
                    TikXml.Builder()
                        .exceptionOnUnreadXml(false)
                        .build()
                )
            )
            .baseUrl("https://itunes.apple.com/")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideSearchService(@SearchRetrofitService retrofit: Retrofit) =
        retrofit.create(SearchService::class.java)

    @Provides
    @Singleton
    fun bindSearchRemoteDataSource(
        searchApiService: SearchService,
        iODispatcher: CoroutineDispatcher
    ): SearchRemoteDataSource {
        return SearchRemoteDataSource(
            searchApiService,
            iODispatcher
        )
    }

    @Provides
    @Singleton
    fun bindGeneralErrorMessageHandler(): MessageHandler {
        return GeneralMessageHandler()
    }

    @Provides
    @Singleton
    fun bindRssService(@RssRetrofitService retrofit: Retrofit): RssService {
        return retrofit.create(RssService::class.java)
    }

    @Provides
    @Singleton
    fun bindRssRemoteDataSource(
        rssService: RssService,
        iODispatcher: CoroutineDispatcher
    ): RssRemoteDataSource {
        return RssRemoteDataSource(rssService, iODispatcher)
    }

    @Provides
    @Singleton
    fun bindPodcastRepository(
        podcastLocalDataSource: PodcastLocalDataSource,
        rssRemoteDataSource: RssRemoteDataSource
    ): PodcastRepository {
        return PodcastRepository(podcastLocalDataSource, rssRemoteDataSource)
    }

    @Provides
    @Singleton
    fun bindSearchRepository(
        podcastLocalDataSource: PodcastLocalDataSource,
        searchRemoteDataSource: SearchRemoteDataSource
    ): SearchRepository {
        return SearchRepository(podcastLocalDataSource, searchRemoteDataSource)
    }
}