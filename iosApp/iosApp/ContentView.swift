import SwiftUI
import shared
import KMPObservableViewModelSwiftUI

struct ContentView: View {
    @StateViewModel var viewModel = SampleObservableViewModel()
    @State private var counter: Int = 0
    @State private var doubled: Int = 0
    @State private var error: String?

    var body: some View {
        VStack(spacing: 24) {
            Text("SKIE-Optimized KMP Counter")
                .font(.title2)
                .padding(.top, 32)

            Text("Counter: \(counter)")
                .font(.title)
                .foregroundColor(.primary)

            Text("Doubled: \(doubled)")
                .font(.body)
                .foregroundColor(.secondary)

            HStack(spacing: 16) {
                Button("Decrement") {
                    viewModel.decrement()
                }
                .buttonStyle(.bordered)

                Button("Increment") {
                    viewModel.increment()
                }
                .buttonStyle(.borderedProminent)

                Button("Reset") {
                    // Uses SKIE default argument optimization
                    viewModel.reset()
                }
                .buttonStyle(.bordered)
            }

            if let error = error {
                Text("Error: \(error)")
                    .foregroundColor(.red)
                    .padding()
                    .background(Color.red.opacity(0.1))
                    .cornerRadius(8)
            }

            Spacer()
        }
        .padding()
        .onAppear {
            observeFlowsWithSKIE()
        }
    }

    // SKIE-optimized Flow observation
    private func observeFlowsWithSKIE() {
        // SKIE automatically converts StateFlow to AsyncSequence
        Task {
            do {
                // Direct AsyncSequence usage - SKIE magic!
                for await value in viewModel.counter {
                    await MainActor.run {
                        counter = Int(truncating: value)
                    }
                }
            } catch {
                await MainActor.run {
                    self.error = "Counter error: \(error.localizedDescription)"
                }
            }
        }

        Task {
            do {
                // Another SKIE-optimized Flow observation
                for await value in viewModel.doubled {
                    await MainActor.run {
                        doubled = Int(truncating: value)
                    }
                }
            } catch {
                await MainActor.run {
                    self.error = "Doubled error: \(error.localizedDescription)"
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
