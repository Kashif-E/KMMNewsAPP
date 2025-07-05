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
    // Add state for last sync time
    @State private var lastSyncTime: Int64? = nil
    
    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // Last Sync Time View
                if let lastSync = lastSyncTime {
                    let formatted = formatDate(lastSync)
                    Text("Last synced: \(formatted)")
                        .font(.caption)
                        .foregroundColor(.secondary)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding([.top, .horizontal], 12)
                }
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
            observeLastSyncTime()
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
    // Observe lastSyncTime from the KMP ViewModel
    private func observeLastSyncTime() {
        Task {
            for await syncTime in viewModel.lastSyncTime {
                await MainActor.run {
                    lastSyncTime = syncTime
                }
            }
        }
    }
    // Helper to format the timestamp to a readable string
    private func formatDate(_ timestamp: Int64) -> String {
        let date = Date(timeIntervalSince1970: TimeInterval(timestamp) / 1000)
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        return formatter.string(from: date)
    }
}

// ... rest of the file unchanged ...
