import SwiftUI

import shared
@main
struct iOSApp: App {
    /**
     you can also initialise koin using an app delegate
     */
    init(){
        KoinModuleKt.doInitKoin(baseUrl: "https://newsapi.org/v2")
    }
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
