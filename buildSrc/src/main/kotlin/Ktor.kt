/*
object Ktor {
    private const val ktorVersion = "2.0.1"
    const val iosLogging = "io.ktor:ktor-client-logging-native:${ktorVersion}"
    const val ktorLogging= "io.ktor:ktor-client-logging:${ktorVersion}"
    const val androidLogging= "io.ktor:ktor-client-logging-jvm:${ktorVersion}"
  //  const val clientSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"
    const val core = "io.ktor:ktor-client-core:${ktorVersion}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${ktorVersion}"
    const val android = "io.ktor:ktor-client-android:${ktorVersion}"
    const val ios = "io.ktor:ktor-client-ios:${ktorVersion}"
    const val okhttp = "io.ktor:ktor-client-okhttp:${ktorVersion}"



}
*/

object Ktor {
    private const val ktorVersion = "2.1.0"
    const val clientCore = "io.ktor:ktor-client-core:${ktorVersion}"
    const val clientJson = "io.ktor:ktor-client-json:${ktorVersion}"
    const val clientLogging = "io.ktor:ktor-client-logging:${ktorVersion}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${ktorVersion}"
    const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${ktorVersion}"
    const val json = "io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}"

    const val clientAndroid = "io.ktor:ktor-client-android:${ktorVersion}"

    const val clientIos = "io.ktor:ktor-client-ios:${ktorVersion}"
}