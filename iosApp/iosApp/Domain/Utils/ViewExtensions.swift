//
//  ViewExtensions.swift
//  iosApp
//
//  Created by Kashif Work on 14/08/2022.
//  Copyright © 2022 orgName. All rights reserved.
//


import SwiftUI

extension View {
    /// Navigate to a new view.
    /// - Parameters:
    ///   - view: View to navigate to.
    ///   - binding: Only navigates when this condition is `true`.
    func navigate<NewView: View>(to view: NewView, when binding: Binding<Bool>, title: String = "Headlines" , bar : Bool = true , navigationTitle: String = "") -> some View {
        NavigationView {
            ZStack {
                self
                    .navigationBarTitle(title)
                    .navigationBarHidden(false)

                NavigationLink(
                    destination: view
                        .navigationBarTitle(navigationTitle)
                        .navigationBarTitleDisplayMode(.automatic)
                        .navigationBarHidden(bar).foregroundColor(Color.black),
                    isActive: binding
                ) {
                    EmptyView()
                }
            }
        }
        .navigationViewStyle(.automatic)
    }
}
extension Image {
    
    func imageModifier() -> some View{
        self.resizable().scaledToFill()
            
            //.transition(.move(edge: .bottom))
    }
    
    func iconModifier() -> some View{
        self.imageModifier().frame(maxWidth:128)
            .foregroundColor(.green)
            .opacity(0.5)
    }
}



