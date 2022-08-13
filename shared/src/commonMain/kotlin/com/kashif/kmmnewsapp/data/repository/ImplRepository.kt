package com.kashif.kmmnewsapp.data.repository

import com.kashif.kmmnewsapp.data.local.service.AbstractRealmService
import com.kashif.kmmnewsapp.data.remote.service.AbstractKtorService


class ImplRepository(
    private val ktorService: AbstractKtorService,
    private val realmService: AbstractRealmService
) : AbstractRepository() {


    override suspend fun getAllHeadlines(page: Int, pageSize: Int, country: String) =
        ktorService.getHeadLines(
            pageSize = pageSize,
            page = page,
            country = country

        )
}