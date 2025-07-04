import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

struct HeadlinesView: View {
    @StateViewModel var viewModel = HeadlinesViewModel()
    @State private var headlines: [Headline] = []
    @State private var isLoading: Bool = true
    @State private var error: String?
    @State private var isLoadingNextPage: Bool = false
    @State private var hasMore: Bool = true
    @State private var currentPage: Int = 1

    var body: some View {
        NavigationView {
            Group {
                if isLoading && headlines.isEmpty {
                    ProgressView("Loading headlines...")
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if let error = error {
                    Text(error)
                        .foregroundColor(.red)
                        .multilineTextAlignment(.center)
                        .padding()
                } else {
                    List {
                        ForEach(headlines, id: \.id) { headline in
                            VStack(alignment: .leading, spacing: 8) {
                                Text(headline.title)
                                    .font(.headline)
                                if !headline.description.isEmpty {
                                    Text(headline.description)
                                        .font(.subheadline)
                                        .foregroundColor(.secondary)
                                }
                                Text(headline.source)
                                    .font(.caption)
                                    .foregroundColor(.blue)
                                Text(headline.publishedAt)
                                    .font(.caption2)
                                    .foregroundColor(.gray)
                            }
                            .padding(.vertical, 4)
                            .onAppear {
                                // Trigger next page load when near the end
                                if hasMore && !isLoadingNextPage && headline.id == headlines.last?.id {
                                    viewModel.send(intent: HeadlinesIntentLoadNextPage())
                                }
                            }
                        }
                        if isLoadingNextPage {
                            HStack {
                                Spacer()
                                ProgressView()
                                Spacer()
                            }
                        }
                    }
                }
            }
            .navigationTitle("Headlines")
        }
        .onAppear {
            observeHeadlinesState()
        }
    }

    private func observeHeadlinesState() {
        Task {
            for await state in viewModel.state {
                await MainActor.run {
                    switch state {
                    case let s as HeadlinesState:
                        isLoading = s.isLoading && s.headlines.isEmpty
                        isLoadingNextPage = s.isLoadingNextPage
                        error = s.error
                        hasMore = s.hasMore
                        currentPage = Int(s.currentPage)
                        headlines = s.headlines
                    default:
                        break
                    }
                }
            }
        }
    }
}

struct HeadlinesView_Previews: PreviewProvider {
    static var previews: some View {
        HeadlinesView()
    }
}
