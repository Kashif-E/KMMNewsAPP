package com.kashif.kmmnewsapp.data.local.service

import com.kashif.kmmnewsapp.data.local.dao.HeadlineDAO
import io.realm.kotlin.Realm

class ImplRealmService(private val realm: Realm): AbstractRealmService(){
    override suspend fun addHeadLineToReadLater() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllReadLaterHeadlines(): List<HeadlineDAO> {
        TODO("Not yet implemented")
    }

}