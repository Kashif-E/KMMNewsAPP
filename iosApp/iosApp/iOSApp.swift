import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        
        let dependenciesHelper = DependenciesProviderHelper()
        dependenciesHelper.doInitKoinIos(
            enableNetworkLogs: true,
            baseUrl: "https://newsapi.org/v2"
        )
        
       
        let setupSuccessful = verifyKoinSetup()
        if !setupSuccessful {
            print(" Warning: Koin setup verification failed!")
        }
        
     
        let networkService: NetworkConnectivityServiceImpl = get()
        networkService.startMonitoring()
        print("Initial connectivity: \(networkService.connectivityState.value)")
        
       
    }
    
    var body: some Scene {
        WindowGroup {
          
             HeadlinesView()
            
            }
        
    }
}
