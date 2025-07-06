import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

struct HomeScreenView: View {
    @StateObject private var viewModel: HeadlinesViewModel = get()
    
    @State private var headlines: [Headline] = []
    @State private var isLoading: Bool = true
    @State private var error: PaginationError?
    @State private var selectedCategory: String = ""
    @State private var searchText: String = ""
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                WelcomeHeaderView()
                
                SearchBarView(
                    searchText: searchText,
                    onSearchTextChange: { text in
                        searchText = text
                    },
                    onSearchClick: {
                        // Handle search
                    }
                )
                
                CategoryChipsView(
                    selectedCategory: selectedCategory,
                    onCategorySelected: { category in
                        selectedCategory = category
                    }
                )
                
                if !headlines.isEmpty {
                    FeaturedArticlesSection(headlines: Array(headlines.prefix(3)), onHeadlineClick: { _ in })
                    
                    ShortForYouSection(
                        headlines: Array(headlines.dropFirst(3).prefix(5)),
                        onViewAllClick: {
                            // Handle view all
                        }
                    )
                }
                
                if isLoading && headlines.isEmpty {
                    ProgressView("Loading headlines...")
                        .padding()
                }
                
                if let error = error, headlines.isEmpty {
                    VStack(spacing: 16) {
                        Text("Error loading headlines")
                            .font(.headline)
                            .foregroundColor(.red)
                        
                        Button("Retry") {
                            loadHeadlines()
                        }
                        .buttonStyle(.borderedProminent)
                    }
                    .padding()
                }
            }
        }
        .onAppear {
            loadHeadlines()
            observePaginationState()
        }
    }
    
    private func loadHeadlines() {
        viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
    }
    
    private func observePaginationState() {
        Task {
            for await paginationState in viewModel.paginationState {
                await MainActor.run {
                    headlines = paginationState.items as? [Headline] ?? []
                    isLoading = paginationState.isInitialLoading
                    error = paginationState.error
                }
            }
        }
    }
}

struct HomeScreenView_Previews: PreviewProvider {
    static var previews: some View {
        HomeScreenView()
    }
}
