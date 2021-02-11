package com.nasabadiam.koopod.podcast.podcastlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "podcast")
data class PodcastEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "rss_link") var rssLink: String = "",
    @ColumnInfo(name = "link") var link: String = "",
    @ColumnInfo(name = "release_date") var releaseDate: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "author") var author: String = "",
    @ColumnInfo(name = "summary") var summary: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "keywords") var keywords: String = "",
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "language") var language: String = "",
    @ColumnInfo(name = "category") var category: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @Ignore var episodes: List<EpisodeEntity> = listOf()
) {

    fun toPodcastModel(): PodcastModel {
        return PodcastModel(
            rssLink = rssLink,
            title = title,
            author = author,
            description = description,
            image = image,
            pubDate = releaseDate.toString(),
            lastBuildDate = "",
            link = link,
            language = language,
            episodes = episodes.map { it.toEpisodeModel() },
            managingEditor = ""
        )
    }

    companion object {
        fun fromPodcastModel(podcastModel: PodcastModel): PodcastEntity {
            return with(podcastModel) {
                PodcastEntity(
                    rssLink = rssLink,
                    title = title,
                    author = author,
                    description = description,
                    image = image,
                    link = link,
                    language = language,
                    episodes = episodes.map { EpisodeEntity.fromEpisodeModel(it) }
                )
            }
        }

    }
}