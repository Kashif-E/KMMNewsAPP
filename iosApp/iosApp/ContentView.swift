import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            HomeTab()
                .tabItem {
                    Image(systemName: "house")
                    Text("Home")
                }
            SettingsTab()
                .tabItem {
                    Image(systemName: "gear")
                    Text("Settings")
                }
        }
    }
}

struct HomeTab: View {
    var body: some View {
        NavigationStack {
            VStack {
                Text("Hello, World! (Home)")
                    .font(.title2)
                    .padding()
                Spacer()
            }
            .navigationTitle("Minimal App")
        }
    }
}

struct SettingsTab: View {
    var body: some View {
        NavigationStack {
            VStack {
                Text("Hello, World! (Settings)")
                    .font(.title2)
                    .padding()
                Spacer()
            }
            .navigationTitle("Minimal App")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
