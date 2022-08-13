package com.kashif.kmmnewsapp


import com.kashif.kmmnewsapp.presentation.screen.ScreenViewModel
import io.ktor.client.engine.android.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


actual fun platformModule() = module {


    single {
        Android.create()
    }
    /**
     *
     * for android koin has a special viewmodel scope that we can use
     * to create a viewmodel
     *
     */

   viewModel {
       ScreenViewModel()
   }


}
