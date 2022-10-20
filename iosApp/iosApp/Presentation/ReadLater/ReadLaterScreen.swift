//
//  ReadLaterScreen.swift
//  iosApp
//
//  Created by Kashif Work on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ReadLaterScreen: View {
    
    @ObservedObject var state = ViewModels().getReadLaterViewModel().asObserveableObject()
    
    @State var news: HeadlineDomainModel = HeadlineDomainModel(author: "", content: "", description: "", publishedAt: "", source: "", title: "", url: "", urlToImage: "")
    
    @State var moveToWebView = false
    
    var body: some View {
        
            
            
            VStack{
               
                switch state.state {
                    
                case is ReadLaterStateError:
                    
                    
                    
                    Text((state.state as! ReadLaterStateError).message)
                    
                    
                    
                    
                    
                case is ReadLaterStateSuccess:
                    
                    List{
                        
                        ForEach((state.state as! ReadLaterStateSuccess).headlines , id: \.title){ item in
                            HeadlineRow( headline: item,onclick: {
                                 self.news = item
                                 moveToWebView.toggle()
                            }).background( NavigationLink(destination: NewsDetails(headline: item), isActive: $moveToWebView) {
                                EmptyView()
                            }.buttonStyle(PlainButtonStyle()))
                           
                        }
                        
                    }.frame( maxWidth: .infinity)
                        .listStyle(PlainListStyle())
                    
                    
                    
                case is ReadLaterStateLoading:
                    
                    ProgressView()
                    
                default:
                    Text("default")
                    
                    
                    
                }
            }
        }
    }


struct ReadLaterScreen_Previews: PreviewProvider {
    static var previews: some View {
        ReadLaterScreen()
    }
}
