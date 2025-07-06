import SwiftUI
import shared

struct ShortForYouSection: View {
    let headlines: [Headline]
    var onViewAllClick: () -> Void
    
    init(headlines: [Headline], onViewAllClick: @escaping () -> Void = {}) {
        self.headlines = headlines
        self.onViewAllClick = onViewAllClick
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            HStack {
                Text("Short For You")
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundColor(.primary)
                
                Spacer()
                
                Button(action: onViewAllClick) {
                    Label("View All", systemImage: "chevron.right")
                        .font(.body)
                        .foregroundColor(.accentColor)
                }
                .buttonStyle(.plain)
            }
            .padding(.horizontal, 16)
            
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 12) {
                    ForEach(headlines, id: \.url) { headline in
                        ShortArticleCard(headline: headline)
                            .frame(width: 200)
                    }
                }
                .padding(.horizontal, 16)
            }
        }
    }
}

struct ShortArticleCard: View {
    let headline: Headline
    var onClick: () -> Void
    
    init(headline: Headline, onClick: @escaping () -> Void = {}) {
        self.headline = headline
        self.onClick = onClick
    }
    
    var body: some View {
        Button(action: onClick) {
            VStack(alignment: .leading, spacing: 0) {
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
                .frame(height: 100)
                .clipped()
                
                VStack(alignment: .leading, spacing: 8) {
                    Text(headline.title)
                        .font(.body)
                        .fontWeight(.medium)
                        .foregroundColor(.primary)
                        .lineLimit(2)
                        .multilineTextAlignment(.leading)
                    
                    HStack(spacing: 4) {
                        Image(systemName: "eye")
                            .foregroundColor(.secondary)
                            .frame(width: 16, height: 16)
                        
                        Text(generateViewCount())
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                }
                .padding(12)
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
        .background(Color(.systemBackground))
        .cornerRadius(12)
        .shadow(color: Color(.black).opacity(0.08), radius: 2, x: 0, y: 1)
        .buttonStyle(.plain)
        .accessibilityElement(children: .combine)
        .accessibilityLabel(Text(headline.title))
    }
    
    private func generateViewCount() -> String {
        let views = Int.random(in: 10000...99999)
        return "\(views / 1000)k"
    }
}

struct ShortForYouSection_Previews: PreviewProvider {
    static var previews: some View {
        ShortForYouSection(headlines: [
            Headline(
                id: "1",
                title: "Sample Article Title",
                description: "Sample description",
                url: "https://example.com",
                imageUrl: "https://example.com/image.jpg",
                publishedAt: "2022-09-09T12:00:00Z",
                source: "Sample Source"
            )
        ])
    }
}
