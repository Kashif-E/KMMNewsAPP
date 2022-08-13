package com.kashif.kmmnewsapp



import com.kashif.kmmnewsapp.presentation.screen.ScreenViewModel
import io.ktor.client.engine.darwin.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        Darwin.create()
    }

    //single or factory can be used to get a viewmodel object for swiftui

    single {
        ScreenViewModel()
    }
}

/**
 * viewmodels object implements koin component thus its able to get any
 * dependency that is listed in platform module we can call getScreenviewmodel()
 * in swift ui to get an object of screenviewmodel
 */
object ViewModels : KoinComponent {
    fun getScreenViewModel() = get<ScreenViewModel>()

}
