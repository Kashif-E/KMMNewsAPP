package com.kashif.kmmnewsapp


import com.kashif.kmmnewsapp.core.di.initKoin
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import kotlin.experimental.ExperimentalObjCRefinement

/**
 * DependenciesProviderHelper
 *
 * This class provides a clean way to initialize Koin and access instances in iOS.
 * It eliminates the need for manually creating provider functions for each dependency.
 *
 * Based on the solution from: https://medium.com/@kashifmehmood/effortless-dependency-injection-streamlining-koin-get-in-ios-for-kotlin-multiplatform-8c8ccec5ae7d
 */
class DependenciesProviderHelper {

    /**
     * Initializes Koin with all necessary modules
     * Should be called from the iOS App class
     */
    fun initKoinIos(
        enableNetworkLogs: Boolean = true,
        baseUrl: String = "https://newsapi.org/v2"
    ) {
        val koinApp = initKoin(baseUrl)

        // Store the Koin instance for global access
        koin = koinApp.koin
    }

    companion object {
        @OptIn(ExperimentalObjCRefinement::class)
        lateinit var koin: Koin
            private set
    }
}



@OptIn(BetaInteropApi::class)
fun Koin.get(objCClass: ObjCClass): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null, null)
}

@OptIn(BetaInteropApi::class)
fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?, parameter: Any): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier) { parametersOf(parameter) }
}