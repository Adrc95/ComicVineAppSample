package com.adrc95.testing.builder

import com.adrc95.domain.model.Comic

class ComicBuilder {
    var title: String = "Amazing Fantasy #15"
    var description: String = "A comic used for domain unit tests."
    var year: String = "1962"
    var pages: Int = 32
    var thumbnailUrl: String = "https://example.com/amazing-fantasy-15.png"

    fun withTitle(title: String) = apply { this.title = title }

    fun withDescription(description: String) = apply { this.description = description }

    fun withYear(year: String) = apply { this.year = year }

    fun withPages(pages: Int) = apply { this.pages = pages }

    fun withThumbnailUrl(thumbnailUrl: String) = apply { this.thumbnailUrl = thumbnailUrl }

    fun build() = Comic(
        title = title,
        description = description,
        year = year,
        pages = pages,
        thumbnailUrl = thumbnailUrl
    )
}

fun comic(block: ComicBuilder.() -> Unit = {}) = ComicBuilder().apply(block).build()
