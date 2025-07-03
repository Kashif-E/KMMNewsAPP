import SwiftUI
import shared
import KMPNativeCoroutinesAsync
import KMPObservableViewModelSwiftUI
import KMPObservableViewModelSwiftUI

struct ContentView: View {
    @StateViewModel var viewModel = SampleObservableViewModel()
    @State private var counter: Int = 0
    @State private var doubled: Int = 0
    @State private var error: String?

    var body: some View {
        VStack(spacing: 24) {
            Text("KMP Observable Counter")
                .font(.title2)
                .padding(.top, 32)
            Text("Counter: \(counter)")
                .font(.title)
            Text("Doubled: \(doubled)")
                .font(.body)
            Button("Increment") {
                viewModel.increment()
            }
            if let error = error {
                Text(error).foregroundColor(.red)
            }
            Spacer()
        }
        .onAppear {
            observeFlows()
        }
    }

    private func observeFlows() {
        Task {
            do {
                for try await value in asyncSequence(for: viewModel.counterFlow) {
                    counter = Int(truncating: value)
                }
            } catch {
                self.error = error.localizedDescription
            }
        }
        Task {
            do {
                for try await value in asyncSequence(for: viewModel.doubledFlow) {
                    doubled = Int(truncating: value)
                }
            } catch {
                self.error = error.localizedDescription
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
