package com.kashif.kmmnewsapp


import com.kashif.kmmnewsapp.presentation.home.HomeScreenViewModel
import com.kashif.kmmnewsapp.presentation.newdetails.NewsDetailsViewModel
import com.kashif.kmmnewsapp.presentation.readlater.ReadLaterViewModel
import io.ktor.client.engine.darwin.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        Darwin.create()
    }

    //single or factory can be used to get a view-model object for swiftui

    single {
        HomeScreenViewModel(get())
    }
    factory {
        NewsDetailsViewModel(get())
    }
    factory {
        ReadLaterViewModel(get())
    }
}

/**
 * ViewModels object implements koin component thus its able to get any
 * dependency that is listed in platform module we can call getHomeviewmodel()
 * in swift ui to get an object of HomeViewModel
 */
object ViewModels : KoinComponent {
    fun getHomeViewModel() = get<HomeScreenViewModel>()
    fun getNewsDetailsViewModel() = get<NewsDetailsViewModel>()

    fun getReadLaterViewModel() = get<ReadLaterViewModel>()

}


actual interface CommonParcelable