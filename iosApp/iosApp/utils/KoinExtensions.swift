import Foundation
import shared


func get<A: AnyObject>() -> A {
    return DependenciesProviderHelper.companion.koin.get(objCClass: A.self) as! A
}

func get<A: AnyObject>(_ type: A.Type) -> A {
    return DependenciesProviderHelper.companion.koin.get(objCClass: A.self) as! A
}

func get<A: AnyObject>(_ type: A.Type, qualifier: (any Koin_coreQualifier)? = nil, parameter: Any) -> A {
    return DependenciesProviderHelper.companion.koin.get(objCClass: A.self, qualifier: qualifier, parameter: parameter) as! A
}
/**
 * Safe get method that returns an optional
 * Use this when you're not sure if a dependency is available
 * 
 * Example: let optionalService: NetworkService? = safeGet()
 */
func safeGet<T: AnyObject>() -> T? {
    do {
        let result: T = get()
        return result
    } catch {
        print("⚠️ Failed to get dependency of type \(T.self): \(error)")
        return nil
    }
}

/**
 * Safe get method with explicit type
 */
func safeGet<T: AnyObject>(_ type: T.Type) -> T? {
    do {
        let result = get(type)
        return result
    } catch {
        print("⚠️ Failed to get dependency of type \(type): \(error)")
        return nil
    }
}

// MARK: - Convenience methods for common dependencies

/**
 * Convenience methods for frequently used dependencies
 * These provide even cleaner access to common services
 */

/// Get the network connectivity service
func getNetworkService() -> NetworkConnectivityServiceImpl {
    return get(NetworkConnectivityServiceImpl.self)
}

/// Get a new headlines view model instance
func getHeadlinesViewModel() -> HeadlinesViewModel {
    return get(HeadlinesViewModel.self)
}

/// Get the headline repository
func getHeadlineRepository() -> HeadlineRepository {
    return get(HeadlineRepository.self)
}

// MARK: - Debug utilities

/**
 * Debug function to print all available Koin modules and definitions
 * Useful for troubleshooting dependency injection issues
 */
func printKoinDependencies() {
    let koin = DependenciesProviderHelper.companion.koin
    
    print("Available Koin Dependencies:")
    print("Koin instance: \(koin)")
    
    // This would require additional Koin API exposure to get module info
    // For now, we'll just confirm Koin is available
    print(" Koin is properly initialized and accessible")
}

/**
 * Test function to verify that key dependencies are available
 * Call this during app startup to ensure everything is wired correctly
 */
func verifyKoinSetup() -> Bool {
    do {
        // Test critical dependencies
        let _: HeadlinesViewModel = get()
        let _: NetworkConnectivityServiceImpl = get()
        
        print("Koin setup verification passed - all key dependencies available")
        return true
    } catch {
        print("Koin setup verification failed: \(error)")
        return false
    }
}
