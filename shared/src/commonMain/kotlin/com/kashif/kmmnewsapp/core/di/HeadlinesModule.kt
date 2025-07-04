package com.kashif.kmmnewsapp.core.di

import com.kashif.kmmnewsapp.core.network.NewsApiService
import com.kashif.kmmnewsapp.core.network.NewsApiServiceImpl
import com.kashif.kmmnewsapp.feature.headlines.data.HeadlineRepositoryImpl
import com.kashif.kmmnewsapp.feature.headlines.domain.GetCachedHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.domain.GetTopHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.domain.RefreshHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.domain.HeadlineRepository
import com.kashif.kmmnewsapp.feature.headlines.presentation.vm.HeadlinesViewModel
import org.koin.dsl.module

val headlinesModule = module {
    single<NewsApiService> { NewsApiServiceImpl(get(), apiKey = "a52b414d7a4e496a81b9787ebf8993f2") }
    single<HeadlineRepository> { HeadlineRepositoryImpl(get(), get()) }
    factory { GetTopHeadlinesUseCase(get()) }
    factory { RefreshHeadlinesUseCase(get()) }
    factory { GetCachedHeadlinesUseCase(get()) }
    factory { HeadlinesViewModel() }
}
