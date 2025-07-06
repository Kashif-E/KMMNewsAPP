import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

struct HeadlinesView: View {
    @StateObject private var viewModel: HeadlinesViewModel = get()
    @State private var selectedCategory: String = ""
    @State private var searchText: String = ""
    @State private var headlines: [Headline] = []
    @State private var isLoading = true
    @State private var errorMessage: String? = nil

    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                ScrollView {
                    VStack(spacing: 16) {
                        WelcomeHeaderView()
                        SearchBarView(
                            searchText: searchText,
                            onSearchTextChange: { searchText = $0 },
                            onSearchClick: { /* TODO: implement search */ }
                        )
                        CategoryChipsView(
                            selectedCategory: selectedCategory,
                            onCategorySelected: { selectedCategory = $0 }
                        )
                        contentBody
                    }
                    .padding(.top, 8)
                }
            }
            .navigationTitle("Top Headlines")
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: refreshHeadlines) {
                        Image(systemName: "arrow.clockwise")
                    }
                    .accessibilityLabel("Refresh headlines")
                }
            }
            .task(id: selectedCategory) {
                await observePagination()
            }
        }
    }

    @ViewBuilder
    private var contentBody: some View {
        if isLoading {
            ProgressView("Loading headlines…")
                .frame(maxWidth: .infinity, alignment: .center)
        } else if let error = errorMessage {
            ErrorStateView(message: error, retryAction: refreshHeadlines)
        } else if headlines.isEmpty {
            Text("No headlines found.")
                .foregroundColor(.secondary)
                .frame(maxWidth: .infinity, alignment: .center)
        } else {
            contentSections
        }
    }

    @ViewBuilder
    private var contentSections: some View {
        FeaturedArticlesSection(
            headlines: Array(headlines.prefix(10)),
            onHeadlineClick: { _ in }
        )
        ShortForYouSection(
            headlines: Array(headlines.dropFirst(3).prefix(5)),
            onViewAllClick: { /* TODO: implement view all */ }
        )
        Text("Latest Headlines")
            .font(.title2)
            .fontWeight(.bold)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 16)
            .padding(.top, 8)
        VStack(spacing: 16) {
            ForEach(headlines, id: \.url) { headline in
                NewsCard(headline: headline)
            }
        }
    }

    private func refreshHeadlines() {
        errorMessage = nil
        isLoading = true
        viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
    }

    private func observePagination() async {
        for await paginationState in viewModel.paginationState {
            await MainActor.run {
                headlines = paginationState.items as? [Headline] ?? []
                isLoading = paginationState.isInitialLoading
                errorMessage = paginationState.error?.message
            }
        }
    }
}

// MARK: - ErrorStateView
struct ErrorStateView: View {
    let message: String
    let retryAction: () -> Void

    var body: some View {
        VStack(spacing: 12) {
            Image(systemName: "exclamationmark.triangle")
                .foregroundColor(.orange)
                .font(.system(size: 40, weight: .bold))
            Text(message)
                .foregroundColor(.red)
                .multilineTextAlignment(.center)
            Button("Retry", action: retryAction)
                .buttonStyle(.borderedProminent)
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .center)
    }
}

// MARK: - FeaturedArticlesSection
struct FeaturedArticlesSection: View {
    let headlines: [Headline]
    var onHeadlineClick: (Headline) -> Void
    
    var body: some View {
        if !headlines.isEmpty {
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 12) {
                    ForEach(headlines, id: \.url) { headline in
                        FeaturedArticleCard(headline: headline, onClick: { onHeadlineClick(headline) })
                    }
                }
                .padding(.horizontal, 16)
            }
        }
    }
}

struct NewsCard: View {
    let headline: Headline

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            AsyncImage(url: URL(string: headline.imageUrl ?? "")) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                default:
                    Rectangle()
                        .fill(Color.gray.opacity(0.3))
                        .overlay(
                            Image(systemName: "photo")
                                .foregroundColor(.gray)
                        )
                }
            }
            .frame(height: 160)
            .clipped()
            .cornerRadius(16)

            Text(headline.title)
                .font(.title3)
                .fontWeight(.semibold)
                .foregroundColor(.primary)
                .lineLimit(2)
                .padding(.top, 8)

            if !headline.description.isEmpty {
                Text(headline.description)
                    .font(.body)
                    .foregroundColor(.secondary)
                    .lineLimit(3)
                    .padding(.top, 4)
            }

            Divider()
                .padding(.vertical, 8)

            HStack {
                Text(headline.source)
                    .font(.caption)
                    .foregroundColor(.accentColor)
                Spacer()
                Text(headline.publishedAt)
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
        }
        .padding()
        .background(Color(.systemBackground))
        .cornerRadius(16)
        .shadow(color: Color(.black).opacity(0.05), radius: 2, x: 0, y: 1)
    }
}
