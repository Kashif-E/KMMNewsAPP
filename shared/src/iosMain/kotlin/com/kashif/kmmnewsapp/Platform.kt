package com.kashif.kmmnewsapp



import com.kashif.kmmnewsapp.presentation.home.HomeScreenViewModel
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
}

/**
 * viewmodels object implements koin component thus its able to get any
 * dependency that is listed in platform module we can call getHomeviewmodel()
 * in swift ui to get an object of HomeViewModel
 */
object ViewModels : KoinComponent {
    fun getHomeViewModel() = get<HomeScreenViewModel>()

}


actual interface CommonParcelable