import SwiftUI

struct BottomNavigationView: View {
    @Binding var selectedTab: Int
    
    var body: some View {
        HStack {
            TabBarItem(
                icon: "house",
                title: "Home",
                isSelected: selectedTab == 0,
                hasNotificationBadge: false
            ) {
                selectedTab = 0
            }
            
            Spacer()
            
            TabBarItem(
                icon: "heart",
                title: "Saved",
                isSelected: selectedTab == 1,
                hasNotificationBadge: false
            ) {
                selectedTab = 1
            }
            
            Spacer()
            
            TabBarItem(
                icon: "bell",
                title: "Notifications",
                isSelected: selectedTab == 2,
                hasNotificationBadge: true
            ) {
                selectedTab = 2
            }
            
            Spacer()
            
            TabBarItem(
                icon: "person",
                title: "Profile",
                isSelected: selectedTab == 3,
                hasNotificationBadge: false
            ) {
                selectedTab = 3
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
        .background(Color(.systemBackground))
        .shadow(radius: 8)
    }
}

struct TabBarItem: View {
    let icon: String
    let title: String
    let isSelected: Bool
    let hasNotificationBadge: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack(spacing: 4) {
                ZStack {
                    Image(systemName: isSelected ? "\(icon).fill" : icon)
                        .font(.system(size: 20))
                        .foregroundColor(isSelected ? .blue : .gray)
                    
                    if hasNotificationBadge {
                        Circle()
                            .fill(Color.red)
                            .frame(width: 8, height: 8)
                            .offset(x: 12, y: -12)
                    }
                }
                
                Text(title)
                    .font(.caption2)
                    .foregroundColor(isSelected ? .blue : .gray)
            }
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct BottomNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        BottomNavigationView(selectedTab: .constant(0))
    }
}