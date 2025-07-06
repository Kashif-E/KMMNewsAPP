import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

struct ContentView: View {
    @State private var selectedTab: Int = 0
    
    var body: some View {
        VStack(spacing: 0) {
            // Main content based on selected tab
            Group {
                switch selectedTab {
                case 0:
                    HomeScreenView()
                case 1:
                    SavedView()
                case 2:
                    NotificationsView()
                case 3:
                    ProfileView()
                default:
                    HomeScreenView()
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            
            // Bottom navigation
            BottomNavigationView(selectedTab: $selectedTab)
        }
        .background(Color(.systemBackground))
        .ignoresSafeArea(.container, edges: .bottom)
    }
}

// Placeholder views for other tabs
struct SavedView: View {
    var body: some View {
        VStack {
            Text("Saved Articles")
                .font(.title2)
                .fontWeight(.bold)
            Text("Your saved articles will appear here")
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
    }
}

struct NotificationsView: View {
    var body: some View {
        VStack {
            Text("Notifications")
                .font(.title2)
                .fontWeight(.bold)
            Text("Your notifications will appear here")
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
    }
}

struct ProfileView: View {
    var body: some View {
        VStack {
            Text("Profile")
                .font(.title2)
                .fontWeight(.bold)
            Text("Your profile information will appear here")
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
