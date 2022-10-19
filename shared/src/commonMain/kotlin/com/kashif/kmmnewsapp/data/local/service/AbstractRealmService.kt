package com.kashif.kmmnewsapp.data.local.service

import com.kashif.kmmnewsapp.data.local.dao.HeadlineDAO

abstract class AbstractRealmService{
    abstract suspend fun addToReadLater(headlineDAO: HeadlineDAO)

    abstract suspend fun getReadLater(): List<HeadlineDAO>
}