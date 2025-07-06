import SwiftUI

struct CategoryChipsView: View {
    var selectedCategory: String
    var onCategorySelected: (String) -> Void
    
    private let categories = ["Health", "Music", "Technology", "Sports"]
    
    init(selectedCategory: String = "", onCategorySelected: @escaping (String) -> Void = { _ in }) {
        self.selectedCategory = selectedCategory
        self.onCategorySelected = onCategorySelected
    }
    
    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 8) {
                ForEach(categories, id: \.self) { category in
                    Button(action: {
                        onCategorySelected(category)
                    }) {
                        Text("#\(category)")
                            .font(.body)
                            .foregroundColor(selectedCategory == category ? .white : .primary)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 8)
                            .background(
                                selectedCategory == category ? 
                                Color.blue : 
                                Color.gray.opacity(0.2)
                            )
                            .cornerRadius(20)
                    }
                }
            }
            .padding(.horizontal, 16)
        }
    }
}

struct CategoryChipsView_Previews: PreviewProvider {
    static var previews: some View {
        CategoryChipsView(selectedCategory: "Technology")
    }
}