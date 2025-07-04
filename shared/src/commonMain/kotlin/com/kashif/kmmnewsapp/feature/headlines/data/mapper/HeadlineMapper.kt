package com.kashif.kmmnewsapp.feature.headlines.data.mapper

import com.kashif.kmmnewsapp.core.database.HeadlineEntity
import com.kashif.kmmnewsapp.core.network.HeadlineDto
import com.kashif.kmmnewsapp.feature.headlines.domain.Headline

fun HeadlineDto.toEntity(): HeadlineEntity = HeadlineEntity(
    id = id,
    title = title,
    description = description,
    url = url,
    imageUrl = imageUrl,
    publishedAt = publishedAt,
    source = source.name // Store only the source name
)

fun HeadlineEntity.toDomain(): Headline = Headline(
    id = id,
    title = title,
    description = description,
    url = url,
    imageUrl = imageUrl,
    publishedAt = publishedAt,
    source = source
)
