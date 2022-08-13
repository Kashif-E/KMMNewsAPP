package com.kashif.kmmnewsapp.data.local.service

import com.kashif.kmmnewsapp.data.local.dao.HeadlineDAO

abstract class AbstractRealmService{
    abstract suspend fun addHeadLineToReadLater()

    abstract suspend fun getAllReadLaterHeadlines(): List<HeadlineDAO>
}