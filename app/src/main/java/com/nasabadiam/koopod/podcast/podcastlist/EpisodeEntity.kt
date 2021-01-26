package com.nasabadiam.koopod.podcast.podcastlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "episode")
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "guid") var guid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "podcast_id") var podcastId: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "release_date") var releaseDate: Long = 0,
    @ColumnInfo(name = "sub_title") var subTitle: String = "",
    @ColumnInfo(name = "author") var author: String = "",
    @ColumnInfo(name = "summary") var summary: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "keywords") var keywords: String = "",
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "duration") var duration: Long = 0,
    @ColumnInfo(name = "category") var category: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "season_id") var seasonId: Int = 0,
    @ColumnInfo(name = "episode_id") var episodeId: Int = 0,
    @ColumnInfo(name = "played_duration") var playedDuration: Int = 0,
    @ColumnInfo(name = "download_status") var downloadStatus: Int = 0,
    @ColumnInfo(name = "play_status") var playStatus: Int = 0,
    @ColumnInfo(name = "download_url") var downloadUrl: String = "",
    @ColumnInfo(name = "stream_url") var streamUrl: String = "",
    @ColumnInfo(name = "downloaded_file") var downloadedFile: String = ""
)