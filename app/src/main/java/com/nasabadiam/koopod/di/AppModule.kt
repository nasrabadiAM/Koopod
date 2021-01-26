package com.nasabadiam.koopod.di

import android.content.Context
import androidx.room.Room
import com.nasabadiam.koopod.podcast.podcastlist.PodcastDatabase
import com.nasabadiam.koopod.podcast.podcastlist.PodcastLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Provides
    fun providePodcastLocalDataSource(
        database: PodcastDatabase,
        ioDispatcher: CoroutineDispatcher
    ): PodcastLocalDataSource {
        return PodcastLocalDataSource(
            database.podcastDao(), ioDispatcher
        )
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
}