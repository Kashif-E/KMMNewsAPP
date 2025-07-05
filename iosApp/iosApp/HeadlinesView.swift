import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

/**
 * Enhanced HeadlinesView with production-ready pagination features.
 *
 * Key improvements:
 * - Uses new PaginationState for comprehensive state management
 * - Supports accessibility with VoiceOver descriptions
 * - Implements proper error handling and retry mechanisms
 * - Provides smooth loading states and animations
 * - Memory-efficient composition and updates
 */
struct HeadlinesView: View {
    // Using the new clean get() method instead of @StateViewModel
    @StateObject private var viewModel: HeadlinesViewModel = get()
    
    // Enhanced pagination state
    @State private var headlines: [Headline] = []
    @State private var isInitialLoading: Bool = true
    @State private var isLoadingMore: Bool = false
    @State private var isRefreshing: Bool = false
    @State private var error: PaginationError?
    @State private var hasMore: Bool = true
    @State private var totalItems: Int = 0
    @State private var currentPage: Int = 1
    @State private var accessibilityDescription: String = "Loading headlines"
    @State private var loadingProgress: Float = 0.0
    
    var body: some View {
        NavigationView {
            Group {
                // Initial loading state
                if isInitialLoading && headlines.isEmpty {
                    InitialLoadingView(accessibilityDescription: accessibilityDescription)
                }
                // Error state with no data
                else if let error = error, headlines.isEmpty {
                    ErrorStateView(
                        error: error,
                        onRetry: {
                            viewModel.retryLastOperation()
                        }
                    )
                }
                // Empty state
                else if headlines.isEmpty {
                    EmptyStateView(
                        onRefresh: {
                            viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
                        }
                    )
                }
                // Content list
                else {
                    PaginatedHeadlinesList(
                        headlines: headlines,
                        isLoadingMore: isLoadingMore,
                        error: error,
                        hasMore: hasMore,
                        totalItems: totalItems,
                        loadingProgress: loadingProgress,
                        onLoadMore: {
                            viewModel.sendPaginationIntent(intent: PaginationIntentLoadMore())
                        },
                        onRetry: {
                            viewModel.retryLastOperation()
                        }
                    )
                }
            }
            .navigationTitle("Top Headlines")
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: {
                        viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
                    }) {
                        Image(systemName: "arrow.clockwise")
                    }
                    .accessibilityLabel("Refresh headlines")
                    .disabled(isRefreshing)
                }
            }
            .refreshable {
                viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
            }
        }
        .onAppear {
            observePaginationState()
        }
        .accessibilityElement(children: .contain)
        .accessibilityLabel(accessibilityDescription)
    }
    
    /**
     * Observes the enhanced pagination state from the ViewModel
     */
    private func observePaginationState() {
        Task {
            for await paginationState in viewModel.paginationState {
                await MainActor.run {
                    // Update state from PaginationState
                    headlines = paginationState.items as? [Headline] ?? []
                    isInitialLoading = paginationState.isInitialLoading
                    isLoadingMore = paginationState.isLoadingMore
                    isRefreshing = paginationState.isRefreshing
                    error = paginationState.error
                    hasMore = paginationState.hasMore
                    totalItems = Int(paginationState.totalItems)
                    currentPage = Int(paginationState.currentPage)
                    accessibilityDescription = paginationState.accessibilityDescription
                    loadingProgress = paginationState.loadingProgress
                }
            }
        }
    }
}

/**
 * Initial loading indicator with accessibility support
 */
struct InitialLoadingView: View {
    let accessibilityDescription: String
    
    var body: some View {
        VStack(spacing: 16) {
            ProgressView()
                .scaleEffect(1.2)
            
            Text("Loading headlines...")
                .font(.body)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .accessibilityElement(children: .combine)
        .accessibilityLabel(accessibilityDescription)
        .accessibilityAddTraits(.updatesFrequently)
    }
}

/**
 * Error state with retry functionality
 */
struct ErrorStateView: View {
    let error: PaginationError
    let onRetry: () -> Void
    
    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 60))
                .foregroundColor(.red)
            
            VStack(spacing: 8) {
                Text("Failed to load headlines")
                    .font(.headline)
                    .foregroundColor(.red)
                
                Text(error.message)
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }
            
            if error.isRecoverable {
                Button(action: onRetry) {
                    HStack {
                        Image(systemName: "arrow.clockwise")
                        Text("Retry")
                    }
                    .padding(.horizontal, 24)
                    .padding(.vertical, 12)
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(8)
                }
                .accessibilityLabel("Retry loading headlines")
            }
        }
        .padding(32)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Error loading headlines: \(error.message)\(error.isRecoverable ? ". Retry button available." : "")")
    }
}

/**
 * Empty state with refresh option
 */
struct EmptyStateView: View {
    let onRefresh: () -> Void
    
    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "doc.text")
                .font(.system(size: 60))
                .foregroundColor(.gray)
            
            VStack(spacing: 8) {
                Text("No headlines available")
                    .font(.headline)
                    .foregroundColor(.primary)
                
                Text("Pull to refresh or tap the refresh button")
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }
            
            Button(action: onRefresh) {
                HStack {
                    Image(systemName: "arrow.clockwise")
                    Text("Refresh")
                }
                .padding(.horizontal, 24)
                .padding(.vertical, 12)
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(8)
            }
            .accessibilityLabel("Refresh headlines")
        }
        .padding(32)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .accessibilityElement(children: .combine)
        .accessibilityLabel("No headlines available. Pull to refresh or use refresh button.")
    }
}

/**
 * Paginated headlines list with enhanced features
 */
struct PaginatedHeadlinesList: View {
    let headlines: [Headline]
    let isLoadingMore: Bool
    let error: PaginationError?
    let hasMore: Bool
    let totalItems: Int
    let loadingProgress: Float
    let onLoadMore: () -> Void
    let onRetry: () -> Void
    
    var body: some View {
        List {
            // Headlines content
            ForEach(Array(headlines.enumerated()), id: \.element.id) { index, headline in
                HeadlineCardView(headline: headline)
                    .onAppear {
                        // Trigger load more when near the end (3 item buffer)
                        if hasMore && !isLoadingMore && index >= headlines.count - 3 {
                            onLoadMore()
                        }
                    }
            }
            
            // Loading more indicator
            if isLoadingMore {
                LoadMoreIndicatorView(
                    progress: loadingProgress,
                    accessibilityLabel: "Loading more headlines"
                )
            }
            
            // Error footer for pagination errors
            if let error = error, !headlines.isEmpty {
                ErrorFooterView(
                    error: error,
                    onRetry: onRetry
                )
            }
            
            // End of list indicator
            if !hasMore && !headlines.isEmpty {
                EndOfListIndicatorView(totalItems: totalItems)
            }
        }
        .listStyle(PlainListStyle())
        .accessibilityElement(children: .contain)
        .accessibilityLabel("\(headlines.count) of \(totalItems) headlines loaded")
    }
}

/**
 * Individual headline card with accessibility support
 */
struct HeadlineCardView: View {
    let headline: Headline
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Headline image if available
            if let imageUrl = headline.imageUrl, !imageUrl.isEmpty {
                AsyncImage(url: URL(string: imageUrl)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color.gray.opacity(0.3))
                        .frame(height: 180)
                }
                .frame(height: 180)
                .clipShape(RoundedRectangle(cornerRadius: 8))
                .accessibilityLabel("Image for headline: \(headline.title)")
            }
            
            // Title
            Text(headline.title)
                .font(.headline)
                .fontWeight(.bold)
                .lineLimit(nil)
                .accessibilityAddTraits(.isHeader)
            
            // Description
            if !headline.description.isEmpty {
                Text(headline.description)
                    .font(.body)
                    .foregroundColor(.secondary)
                    .lineLimit(3)
            }
            
            // Source and date
            HStack {
                Text(headline.source)
                    .font(.caption)
                    .foregroundColor(.blue)
                
                Spacer()
                
                Text(headline.publishedAt)
                    .font(.caption)
                    .foregroundColor(.gray)
            }
        }
        .padding(.vertical, 8)
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Headline: \(headline.title). Source: \(headline.source). Published: \(headline.publishedAt)")
        .accessibilityAddTraits(.isButton)
    }
}

/**
 * Load more indicator with progress
 */
struct LoadMoreIndicatorView: View {
    let progress: Float
    let accessibilityLabel: String
    
    var body: some View {
        HStack {
            Spacer()
            
            VStack(spacing: 8) {
                if progress > 0 {
                    ProgressView(value: progress)
                        .frame(width: 200)
                    
                    Text("Loading \(Int(progress * 100))%")
                        .font(.caption)
                        .foregroundColor(.secondary)
                } else {
                    ProgressView()
                    
                    Text("Loading more headlines...")
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }
            
            Spacer()
        }
        .padding()
        .accessibilityElement(children: .combine)
        .accessibilityLabel(accessibilityLabel)
        .accessibilityAddTraits(.updatesFrequently)
    }
}

/**
 * Error footer for pagination errors
 */
struct ErrorFooterView: View {
    let error: PaginationError
    let onRetry: () -> Void
    
    var body: some View {
        VStack(spacing: 12) {
            HStack {
                Image(systemName: "exclamationmark.triangle")
                    .foregroundColor(.red)
                
                Text("Failed to load more headlines")
                    .font(.body)
                    .foregroundColor(.red)
            }
            
            Text(error.message)
                .font(.caption)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            
            if error.isRecoverable {
                Button(action: onRetry) {
                    HStack {
                        Image(systemName: "arrow.clockwise")
                        Text("Retry")
                    }
                    .font(.caption)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 8)
                    .background(Color.blue.opacity(0.1))
                    .foregroundColor(.blue)
                    .cornerRadius(6)
                }
                .accessibilityLabel("Retry loading more headlines")
            }
        }
        .padding()
        .background(Color.red.opacity(0.1))
        .cornerRadius(8)
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Error loading more headlines: \(error.message)\(error.isRecoverable ? ". Retry button available." : "")")
    }
}

/**
 * End of list indicator
 */
struct EndOfListIndicatorView: View {
    let totalItems: Int
    
    var body: some View {
        HStack {
            Spacer()
            
            Text("All \(totalItems) headlines loaded")
                .font(.caption)
                .foregroundColor(.secondary)
            
            Spacer()
        }
        .padding()
        .accessibilityLabel("End of headlines. \(totalItems) total items loaded.")
    }
}

