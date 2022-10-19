package com.kashif.kmmnewsapp.domain.di

import com.kashif.kmmnewsapp.data.local.dao.HeadlineDAO
import com.kashif.kmmnewsapp.data.local.service.AbstractRealmService
import com.kashif.kmmnewsapp.data.local.service.ImplRealmService
import com.kashif.kmmnewsapp.data.remote.service.AbstractKtorService
import com.kashif.kmmnewsapp.data.remote.service.ImplKtorService
import com.kashif.kmmnewsapp.data.repository.AbstractRepository
import com.kashif.kmmnewsapp.data.repository.ImplRepository
import com.kashif.kmmnewsapp.domain.usecase.newsdetails.AddToReadLaterUseCase
import com.kashif.kmmnewsapp.domain.usecase.home.GetHeadlinesUseCase
import com.kashif.kmmnewsapp.domain.usecase.readlater.GetReadLaterUseCase
import com.kashif.kmmnewsapp.platformModule
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(
    enableNetworkLogs: Boolean = false,
    baseUrl: String,
    appDeclaration: KoinAppDeclaration = {}
) =
    startKoin {
        appDeclaration()
        modules(commonModule(enableNetworkLogs = enableNetworkLogs, baseUrl))
    }

// called by iOS etc
fun initKoin(baseUrl: String) = initKoin(enableNetworkLogs = true, baseUrl) {}

fun commonModule(enableNetworkLogs: Boolean, baseUrl: String) =
    getUseCaseModule() + getDateModule(
        enableNetworkLogs,
        baseUrl
    ) + platformModule() + getHelperModule()

fun getHelperModule() = module {


}

fun getDateModule(enableNetworkLogs: Boolean, baseUrl: String) = module {

    single<AbstractRepository> {
        ImplRepository(
            get(),
            get()
        )
    }

    single<AbstractKtorService> {
        ImplKtorService(
            get(),
            baseUrl
        )
    }

    single<AbstractRealmService> {
        ImplRealmService(
            get()
        )
    }

    single {
        Realm.open(
            RealmConfiguration.Builder(schema = setOf(HeadlineDAO::class))

                .build()
        )
    }

    single { createJson() }

    single {
        createHttpClient(
            get(),
            get(),
            enableNetworkLogs = enableNetworkLogs
        )
    }


}

fun getUseCaseModule() = module {
    single {
        AddToReadLaterUseCase(get())
    }
    single {
        GetReadLaterUseCase(get())
    }
    single {
        GetHeadlinesUseCase(get())
    }
}


fun createHttpClient(
    httpClientEngine: HttpClientEngine,
    json: Json,
    enableNetworkLogs: Boolean
) =
    HttpClient(httpClientEngine) {


        install(ContentNegotiation) {
            json(json)
        }
        if (enableNetworkLogs) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

