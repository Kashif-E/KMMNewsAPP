package com.kashif.kmmnewsapp.core.di

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import co.touchlab.skie.configuration.annotations.FunctionInterop
import com.kashif.kmmnewsapp.platformModule
import com.kashif.kmmnewsapp.core.database.DatabaseProvider
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Initialize Koin with optimal Swift interoperability
 *
 * This function is optimized for Swift usage with:
 * - Native default arguments support
 * - Enhanced function naming
 * - File scope conversion for easy access
 */
@DefaultArgumentInterop.Enabled
@FunctionInterop.FileScopeConversion.Enabled
fun initKoin(
    enableNetworkLogs: Boolean = false,
    baseUrl: String,
    appDeclaration: KoinAppDeclaration = {}
) =
    startKoin {
        appDeclaration()
        modules(
            listOf(
                dataModule(enableNetworkLogs, baseUrl),
                databaseModule(),
                platformModule()
            )
        )
    }

/**
 * Simplified iOS-specific initializer
 * Optimized for Swift call site ergonomics
 */
@FunctionInterop.FileScopeConversion.Enabled
fun initKoin(baseUrl: String) = initKoin(enableNetworkLogs = true, baseUrl) {}

@FunctionInterop.FileScopeConversion.Enabled
fun commonModule(enableNetworkLogs: Boolean, baseUrl: String): List<Module> =
    listOf(
        dataModule(enableNetworkLogs, baseUrl),
        databaseModule(),
        platformModule()
    )

@FunctionInterop.FileScopeConversion.Enabled
fun dataModule(enableNetworkLogs: Boolean, baseUrl: String) = module {
    single { createJson() }

    single {
        createHttpClient(
            get(),
            get(),
            enableNetworkLogs = enableNetworkLogs
        )
    }
}

@FunctionInterop.FileScopeConversion.Enabled
fun databaseModule() = module {
    single { DatabaseProvider() }
}

@FunctionInterop.FileScopeConversion.Enabled
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

@FunctionInterop.FileScopeConversion.Enabled
fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

