import SwiftUI

struct WelcomeHeaderView: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Welcome Back!")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.primary)
            
            Text(getCurrentDateFormatted())
                .font(.body)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 16)
        .padding(.vertical, 24)
    }
    
    private func getCurrentDateFormatted() -> String {
        let formatter = DateFormatter()
        formatter.dateStyle = .full
        return formatter.string(from: Date())
    }
}

struct WelcomeHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeHeaderView()
    }
}