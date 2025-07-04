import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

struct HeadlinesView: View {
    @StateViewModel var viewModel = HeadlinesViewModel()
    @State private var headlines: [Headline] = []
    @State private var isLoading: Bool = true
    @State private var error: String?

    var body: some View {
        NavigationView {
            Group {
                if isLoading {
                    ProgressView("Loading headlines...")
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
            
                } else if let error = error {
                    Text(error)
                        .foregroundColor(.red)
                        .multilineTextAlignment(.center)
                        .padding()
                } else {
                    List(headlines, id: \.id) { headline in
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
                    }
                }
            }
            .navigationTitle("Headlines")
        }
        .onAppear {
            observeHeadlinesState()
        }
    }

    /// Observes state changes from the ViewModel and updates UI accordingly
    /// Runs on background thread but UI updates are dispatched to MainActor
    private func observeHeadlinesState() {
        Task {
            for await state in viewModel.state {
                await MainActor.run {
                    switch state {
                    case is HeadlinesStateLoading:
                        isLoading = true
                        error = nil
                    case let success as HeadlinesStateSuccess:
                        isLoading = false
                        error = nil
                        headlines = success.headlines
                    case let err as HeadlinesStateError:
                        isLoading = false
                        error = err.message
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
