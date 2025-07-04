package com.kashif.kmmnewsapp


import io.ktor.client.engine.darwin.*
import org.koin.core.component.KoinComponent
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        Darwin.create()
    }

}


actual interface CommonParcelable
