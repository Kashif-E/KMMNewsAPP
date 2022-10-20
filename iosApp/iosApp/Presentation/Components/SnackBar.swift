//
//  SnackBar.swift
//  iosApp
//
//  Created by Kashif Work on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

import Foundation
import SwiftUI

struct SnackBar: ViewModifier {
    
    
    @Binding var message: String
    @Binding var show: Bool
    
    
    @State var task: DispatchWorkItem?
    
    func body(content: Content) -> some View {
        ZStack {
            if show {
                VStack {
                    HStack {
                        Text(message)
                            .foregroundColor(.white)
                            .padding(12)
                            .font(Font.system(size: 16, weight: .semibold, design: .default))
                    }
                    .background(Color.black)
                    .cornerRadius(8)
                    .shadow(radius: 20)
                }
                .transition(AnyTransition.move(edge: .bottom).combined(with: .opacity))
                .onTapGesture {
                    withAnimation {
                        self.show = false
                    }
                }.onAppear {
                    self.task = DispatchWorkItem {
                        withAnimation {
                            self.show = false
                        }
                    }
                    // Auto dismiss after 5 seconds, and cancel the task if view disappear before the auto dismiss
                    DispatchQueue.main.asyncAfter(deadline: .now() + 3, execute: self.task!)
                }
                .onDisappear {
                    self.task?.cancel()
                }
            }
            content
        }
    }
}

extension View {
    func snackBar(message: Binding<String> , show: Binding<Bool>) -> some View {
        self.modifier(SnackBar(message: message, show: show))
    }
}
