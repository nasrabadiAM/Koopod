package com.nasabadiam.koopod.podcast.podcastlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcast", primaryKeys = ["rss_link", "id"])
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
    @ColumnInfo(name = "description") var description: String = ""
)