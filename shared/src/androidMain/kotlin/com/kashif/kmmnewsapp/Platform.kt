package com.kashif.kmmnewsapp


import android.os.Parcelable
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * shared implementation of parcelable
 */
actual typealias CommonParcelize = Parcelize

actual typealias CommonParcelable = Parcelable


actual fun platformModule() = module {


    single {
        OkHttp.create()
    }

}
