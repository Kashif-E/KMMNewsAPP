import SwiftUI
import shared

struct FeaturedArticleCard: View {
    let headline: Headline
    var onClick: () -> Void
    
    init(headline: Headline, onClick: @escaping () -> Void = {}) {
        self.headline = headline
        self.onClick = onClick
    }
    
    var body: some View {
        Button(action: onClick) {
            ZStack {
                // Background image
                AsyncImage(url: URL(string: headline.imageUrl ?? "")) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Rectangle()
                        .fill(Color.gray.opacity(0.3))
                        .overlay(
                            Image(systemName: "photo")
                                .foregroundColor(.gray)
                        )
                }
                .frame(width: 250, height: 200)
                .clipped()
                
                // Dark overlay gradient
                LinearGradient(
                    gradient: Gradient(colors: [Color.clear, Color.black.opacity(0.7)]),
                    startPoint: .top,
                    endPoint: .bottom
                )
                
                // Content overlay
                VStack(alignment: .leading, spacing: 8) {
                    Spacer()
                    
                    Text(headline.title)
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .lineLimit(2)
                        .multilineTextAlignment(.leading)
                    
                    HStack {
                        HStack(spacing: 8) {
                            // Author avatar placeholder
                            Circle()
                                .fill(Color.gray.opacity(0.6))
                                .frame(width: 24, height: 24)
                            
                            VStack(alignment: .leading, spacing: 2) {
                                Text(headline.source)
                                    .font(.caption)
                                    .fontWeight(.medium)
                                    .foregroundColor(.white)
                                
                                Text(formatDate(headline.publishedAt))
                                    .font(.caption2)
                                    .foregroundColor(.white.opacity(0.8))
                            }
                        }
                        
                        Spacer()
                        
                        Button(action: {
                            // Share action
                        }) {
                            Image(systemName: "square.and.arrow.up")
                                .foregroundColor(.white)
                                .frame(width: 20, height: 20)
                        }
                        .frame(width: 32, height: 32)
                    }
                }
                .padding(16)
            }
        }
        .frame(width: 250, height: 200)
        .cornerRadius(16)
        .shadow(radius: 4)
        .buttonStyle(PlainButtonStyle())
    }
    
    private func formatDate(_ dateString: String) -> String {
        // Simple date formatting - in a real app, you'd use proper date formatting
        return "Sep 9, 2022"
    }
}

struct FeaturedArticleCard_Previews: PreviewProvider {
    static var previews: some View {
        FeaturedArticleCard(headline: Headline(
            id: "1",
            title: "Sample Article Title",
            description: "Sample description",
            url: "https://example.com",
            imageUrl: "https://example.com/image.jpg",
            publishedAt: "2022-09-09T12:00:00Z",
            source: "Sample Source"
        ))
    }
}
