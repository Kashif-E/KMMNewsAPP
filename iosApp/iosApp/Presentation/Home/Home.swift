//
//  Screen.swift
//  iosApp
//
//  Created by Kashif Work on 13/08/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct Screen: View {
    
    @ObservedObject var viewmodel = ViewModels().getHomeViewModel().asObserveableObject()
    
    @State var news: HeadlineDomainModel = HeadlineDomainModel(author: "", content: "", description: "", publishedAt: "", source: "", title: "", url: "", urlToImage: "")
    
    @State var moveToWebView = false
    var body: some View {
        
        VStack{
            
           
            switch true {
                
            case viewmodel.state.error.isError:
                
                
                
                Text(viewmodel.state.error.errorMessage)
                
                
                
                
                
            case viewmodel.state.isSuccess:
                
                
                List{
                    
                    ForEach(viewmodel.state.headlines , id: \.title){ item in
                        HeadlineRow( headline: item,onclick: {
                            
                            self.news = item
                            moveToWebView.toggle()
                            
                        })
                    }

                }.frame( maxWidth: .infinity)
                    .listStyle(PlainListStyle())
                
                
                
            case viewmodel.state.isLoading:
             
                ProgressView()
                
            default:
                Text("default")
                
                
                
            }
        }.onAppear(perform: {
            viewmodel.viewModel.onIntent(intent: HomeScreenSideEffects.GetHeadlines())
        }).navigate(to: NewsDetails(headline: news), when: $moveToWebView, title: "Headlines", bar: false, navigationTitle: news.source )
      
        
        
    }
}

struct Screen_Previews: PreviewProvider {
    static var previews: some View {
        Screen()
    }
}
