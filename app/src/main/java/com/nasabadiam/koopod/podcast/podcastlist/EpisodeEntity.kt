package com.nasabadiam.koopod.podcast.podcastlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode")
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "guid") var guid: String,
    @ColumnInfo(name = "podcast_id") var podcastId: Long,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "pub_date") var pubDate: String = "",
    @ColumnInfo(name = "sub_title") var subTitle: String = "",
    @ColumnInfo(name = "author") var author: String = "",
    @ColumnInfo(name = "summary") var summary: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "keywords") var keywords: String = "",
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "duration") var duration: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "season_id") var seasonId: Int = 0,
    @ColumnInfo(name = "episode_id") var episodeId: Int = 0,
    @ColumnInfo(name = "played_duration") var playedDuration: Int = 0,
    @ColumnInfo(name = "download_status") var downloadStatus: Int = 0,
    @ColumnInfo(name = "play_status") var playStatus: Int = 0,
    @ColumnInfo(name = "download_url") var downloadUrl: String = "",
    @ColumnInfo(name = "stream_url") var streamUrl: String = "",
    @ColumnInfo(name = "downloaded_file") var downloadedFile: String = ""
) {
    fun toEpisodeModel() = EpisodeModel(
        guid = guid,
        title = title,
        subTitle = subTitle,
        summary = summary,
        author = author,
        pubDate = pubDate,
        keywords = keywords.split(","),
        link = "",
        image = image,
        description = description,
        duration = duration,
        episodeId = episodeId,
        seasonId = seasonId,
        type = type,
        downloadUrl = downloadUrl,
        streamUrl = streamUrl,
        id = id,
        podcastId = podcastId
    )

    companion object {
        fun fromEpisodeModel(episodeModel: EpisodeModel): EpisodeEntity {
            return with(episodeModel) {
                EpisodeEntity(
                    guid = guid,
                    podcastId = podcastId, title = title,
                    pubDate = pubDate,
                    subTitle = subTitle,
                    author = author,
                    summary = summary,
                    image = image,
                    keywords = keywords.joinToString(","),
                    type = type,
                    duration = duration,
                    description = description,
                    seasonId = seasonId,
                    episodeId = episodeId,
                    downloadUrl = downloadUrl,
                    streamUrl = streamUrl
                )
            }
        }

    }
}