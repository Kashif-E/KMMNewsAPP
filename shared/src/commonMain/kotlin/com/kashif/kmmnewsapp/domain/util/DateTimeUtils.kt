package com.kashif.kmmnewsapp.domain.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun toLocalDate(date: String): String {

    val local = date.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())

    return "${local.year}-${local.month}-${local.dayOfMonth} "

}