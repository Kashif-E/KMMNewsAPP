import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

struct HeadlinesView: View {
    @StateObject private var viewModel: HeadlinesViewModel = get()
    @State private var selectedCategory: String = ""
    @State private var searchText: String = ""
    @State private var headlines: [Headline] = []
    @State private var isLoading: Bool = true
    @State private var errorMessage: String? = nil
    
    var body: some View {
        NavigationView {
            ZStack {
                Color(.systemGroupedBackground)
                    .ignoresSafeArea()
                ScrollView {
                    VStack(spacing: 24) {
                        WelcomeHeaderView()
                            .padding(.top, 8)
                        SearchBarView(
                            searchText: searchText,
                            onSearchTextChange: { searchText = $0 },
                            onSearchClick: { /* TODO: search action */ }
                        )
                        CategoryChipsView(
                            selectedCategory: selectedCategory,
                            onCategorySelected: { selectedCategory = $0 }
                        )
                        if isLoading {
                            VStack(spacing: 12) {
                                ProgressView()
                                    .progressViewStyle(CircularProgressViewStyle(tint: .accentColor))
                                    .scaleEffect(1.3)
                                Text("Loading headlines...")
                                    .font(.body)
                                    .foregroundColor(.secondary)
                            }
                            .frame(maxWidth: .infinity, minHeight: 200)
                            .transition(.opacity)
                        } else if let errorMessage = errorMessage {
                            VStack(spacing: 16) {
                                Image(systemName: "exclamationmark.triangle.fill")
                                    .font(.system(size: 48))
                                    .foregroundColor(.orange)
                                Text("Something went wrong")
                                    .font(.title3)
                                    .fontWeight(.semibold)
                                Text(errorMessage)
                                    .font(.body)
                                    .foregroundColor(.secondary)
                                Button(action: {
                                    errorMessage = nil
                                    isLoading = true
                                    viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
                                }) {
                                    Label("Retry", systemImage: "arrow.clockwise")
                                        .font(.body)
                                }
                                .buttonStyle(.borderedProminent)
                                .accessibilityLabel("Retry loading headlines")
                            }
                            .frame(maxWidth: .infinity, minHeight: 200)
                            .transition(.opacity)
                        } else if headlines.isEmpty {
                            VStack(spacing: 16) {
                                Image(systemName: "doc.text.magnifyingglass")
                                    .font(.system(size: 48))
                                    .foregroundColor(.secondary)
                                Text("No headlines found")
                                    .font(.title3)
                                    .fontWeight(.semibold)
                                Text("Try refreshing or changing your filters.")
                                    .font(.body)
                                    .foregroundColor(.secondary)
                                Button(action: {
                                    isLoading = true
                                    viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
                                }) {
                                    Label("Refresh", systemImage: "arrow.clockwise")
                                        .font(.body)
                                }
                                .buttonStyle(.bordered)
                                .accessibilityLabel("Refresh headlines")
                            }
                            .frame(maxWidth: .infinity, minHeight: 200)
                            .transition(.opacity)
                        } else {
                            SectionHeader(title: "Featured Articles", systemImage: "star.fill")
                                .padding(.top, 8)
                            if !headlines.isEmpty {
                                FeaturedArticlesSection(
                                    headlines: Array(headlines.prefix(10)),
                                    onHeadlineClick: { _ in }
                                )
                            }
                            SectionHeader(title: "Short For You", systemImage: "bolt.heart.fill")
                            ShortForYouSection(
                                headlines: Array(headlines.dropFirst(3).prefix(5)),
                                onViewAllClick: { /* TODO: view all action */ }
                            )
                            SectionHeader(title: "Latest Headlines", systemImage: "newspaper.fill")
                                .padding(.top, 8)
                            VStack(spacing: 20) {
                                ForEach(headlines, id: \.url) { headline in
                                    NewsCard(headline: headline)
                                        .padding(.horizontal, 20)
                                        .transition(.opacity.combined(with: .move(edge: .bottom)))
                                }
                            }
                        }
                    }
                    .padding(.vertical, 8)
                    .animation(.easeInOut, value: isLoading)
                    .animation(.easeInOut, value: errorMessage)
                    .animation(.easeInOut, value: headlines)
                }
            }
            .navigationTitle("Top Headlines")
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: {
                        errorMessage = nil
                        isLoading = true
                        viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
                    }) {
                        Image(systemName: "arrow.clockwise")
                    }
                    .accessibilityLabel("Refresh headlines")
                }
            }
            .refreshable {
                errorMessage = nil
                isLoading = true
                viewModel.sendPaginationIntent(intent: PaginationIntentRefresh())
            }
            .task {
                for await paginationState in viewModel.paginationState {
                    await MainActor.run {
                        print("Received paginationState: \(paginationState.items.count) items, error: \(String(describing: paginationState.error))")
                        headlines = paginationState.items as? [Headline] ?? []
                        isLoading = paginationState.isInitialLoading
                        if let error = paginationState.error {
                            errorMessage = error.message
                        } else {
                            errorMessage = nil
                        }
                    }
                }
            }
        }
    }
}

struct SectionHeader: View {
    let title: String
    let systemImage: String
    var body: some View {
        HStack(spacing: 8) {
            Image(systemName: systemImage)
                .foregroundColor(.accentColor)
                .font(.title3)
            Text(title)
                .font(.title3)
                .fontWeight(.semibold)
                .foregroundColor(.primary)
            Spacer()
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 4)
        .background(Color(.secondarySystemGroupedBackground).opacity(0.7))
        .cornerRadius(10)
        .accessibilityElement(children: .combine)
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

// MARK: - NewsCard
struct NewsCard: View {
    let headline: Headline
    @State private var imageLoaded = false
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            AsyncImage(url: URL(string: headline.imageUrl ?? "")) { phase in
                switch phase {
                case .empty:
                    Rectangle()
                        .fill(Color.gray.opacity(0.3))
                        .overlay(
                            Image(systemName: "photo")
                                .foregroundColor(.gray)
                        )
                        .frame(height: 160)
                        .cornerRadius(16)
                        .opacity(0.7)
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(height: 160)
                        .cornerRadius(16)
                        .clipped()
                        .opacity(imageLoaded ? 1 : 0)
                        .onAppear {
                            withAnimation(.easeIn(duration: 0.3)) { imageLoaded = true }
                        }
                case .failure:
                    Rectangle()
                        .fill(Color.red.opacity(0.1))
                        .overlay(
                            Image(systemName: "exclamationmark.triangle")
                                .foregroundColor(.red)
                        )
                        .frame(height: 160)
                        .cornerRadius(16)
                        .opacity(0.7)
                @unknown default:
                    EmptyView()
                }
            }
            VStack(alignment: .leading, spacing: 8) {
                Text(headline.title)
                    .font(.title3)
                    .fontWeight(.semibold)
                    .foregroundColor(.primary)
                    .lineLimit(2)
                    .fixedSize(horizontal: false, vertical: true)
                if !headline.description.isEmpty {
                    Text(headline.description)
                        .font(.body)
                        .foregroundColor(.secondary)
                        .lineLimit(3)
                        .fixedSize(horizontal: false, vertical: true)
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
            .padding(16)
            .padding(.bottom, 8)
        }
        .background(Color(.systemBackground))
        .cornerRadius(16)
        .shadow(color: Color(.black).opacity(0.16), radius: 10, x: 0, y: 6)
        .padding(.vertical, 8)
        .accessibilityElement(children: .combine)
        .accessibilityLabel(Text(headline.title))
    }
}
