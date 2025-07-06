import SwiftUI

struct SearchBarView: View {
    @State private var text: String = ""
    var searchText: String
    var onSearchTextChange: (String) -> Void
    var onSearchClick: () -> Void
    
    init(searchText: String = "", onSearchTextChange: @escaping (String) -> Void = { _ in }, onSearchClick: @escaping () -> Void = {}) {
        self.searchText = searchText
        self.onSearchTextChange = onSearchTextChange
        self.onSearchClick = onSearchClick
        self._text = State(initialValue: searchText)
    }
    
    var body: some View {
        HStack(spacing: 8) {
            TextField("Search for articles...", text: $text)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .onChange(of: text) { newValue in
                    onSearchTextChange(newValue)
                }
            
            Button(action: onSearchClick) {
                Image(systemName: "magnifyingglass")
                    .foregroundColor(.white)
                    .frame(width: 20, height: 20)
            }
            .frame(width: 48, height: 48)
            .background(Color.blue)
            .clipShape(Circle())
        }
        .padding(.horizontal, 16)
    }
}

struct SearchBarView_Previews: PreviewProvider {
    static var previews: some View {
        SearchBarView()
    }
}