package com.nasabadiam.koopod.podcast.podcastlist

import com.tickaroo.tikxml.annotation.*

@Xml(name = "rss")
data class RssPodcastResponseDto(@Element val channel: RssChannelDto) {
    fun toPodcastModel(): PodcastModel {
        return with(channel) {
            val rssLink = rssLinks.first {
                it.type == "application/rss+xml" || it.type == "application/atom+xml"
            }
            PodcastModel(
                rssLink = rssLink.url,
                title = title,
                author = author.orEmpty(),
                image = image?.url.orEmpty(),
                description = description.orEmpty(),
                pubDate = pubDate.orEmpty(),
                lastBuildDate = lastBuildDate.orEmpty(),
                link = link.orEmpty(),
                language = language,
                managingEditor = managingEditor.orEmpty(),
                episodes = items.mapNotNull { it.toEpisodeModel() },
                keywords = keywords?.split(",") ?: listOf(),
                type = type.orEmpty(),
                category = category.orEmpty(),
                summary = summary.orEmpty()
            )
        }
    }
}


@Xml(name = "channel")
data class RssChannelDto(
    @PropertyElement(name = "title") val title: String,
    @PropertyElement(name = "pubDate") val pubDate: String?,
    @Element val rssLinks: List<RssLink>,
    @PropertyElement(name = "lastBuildDate") val lastBuildDate: String?,
    @PropertyElement(name = "link") val link: String?,
    @PropertyElement(name = "language") val language: String,
    @PropertyElement(name = "managingEditor") val managingEditor: String?,
    @Element val image: ChannelImageDto? = null,
    @PropertyElement(name = "description", writeAsCData = true) val description: String?,
    @PropertyElement(name = "itunes:keywords") val keywords: String?,
    @PropertyElement(name = "itunes:author") val author: String?,
    @PropertyElement(name = "itunes:type") val type: String?,
    @PropertyElement(name = "category") val category: String?,
    @PropertyElement(name = "itunes:summary") val summary: String?,
    @Element val items: List<RssEpisodeDto>
)

@Xml(name = "atom:link")
data class RssLink(
    @Attribute(name = "href") val url: String,
    @Attribute(name = "type") val type: String?,
    @Attribute(name = "rel") val rel: String?
)

@Xml(name = "image")
data class ChannelImageDto(
    @PropertyElement(name = "url") val url: String,
    @PropertyElement(name = "title") val title: String,
    @PropertyElement(name = "link", writeAsCData = true) val link: String
)

@Xml(name = "item")
data class RssEpisodeDto(
    @PropertyElement(name = "guid", writeAsCData = true) val guid: String,
    @PropertyElement(name = "title") val title: String,
    @PropertyElement(name = "itunes:subtitle") val subTitle: String?,
    @PropertyElement(name = "itunes:author") val itunesAuthor: String?,
    @PropertyElement(name = "itunes:summary") val summary: String?,
    @PropertyElement(name = "itunes:keywords") val keywords: String?,
    @PropertyElement(name = "itunes:duration") val duration: String?,
    @PropertyElement(name = "itunes:episode") val episodeId: Int?,
    @PropertyElement(name = "seasonId") val seasonId: Int?,
    @PropertyElement(name = "type") val type: String?,
    @PropertyElement(name = "author") val author: String?,
    @PropertyElement(name = "pubDate") val pubDate: String?,
    @PropertyElement(name = "link", writeAsCData = true) val link: String,
    @Path("itunes:image") @Attribute(name = "href") val image: String?,
    @Path("enclosure") @Attribute(name = "url") val downloadUrl: String?,
    @Path("enclosure") @Attribute(name = "type") val downloadFileType: String?,
    @Path("enclosure") @Attribute(name = "length") val downloadFileLengthInByte: String?,
    @PropertyElement(name = "description", writeAsCData = true) val description: String?
) {
    fun toEpisodeModel(): EpisodeModel? {
        return if (downloadUrl != null) {
            EpisodeModel(
                guid = guid,
                title = title,
                pubDate = pubDate.orEmpty(),
                link = link,
                image = image.orEmpty(),
                description = description.orEmpty(),
                subTitle = subTitle.orEmpty(),
                summary = summary.orEmpty(),
                author = author ?: itunesAuthor.orEmpty(),
                keywords = keywords?.split(",") ?: listOf(),
                duration = duration.orEmpty(),
                episodeId = episodeId ?: 0,
                seasonId = seasonId ?: 0,
                type = type.orEmpty(),
                downloadUrl = downloadUrl,
                streamUrl = downloadUrl
            )
        } else {
            null
        }
    }
}