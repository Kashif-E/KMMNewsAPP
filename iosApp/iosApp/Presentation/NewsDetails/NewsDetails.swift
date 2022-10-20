//
//  NewsDetails.swift
//  iosApp
//
//  Created by Kashif Work on 20/09/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
struct NewsDetails: View {
    
    var headline : HeadlineDomainModel
    
    @State var  showSnackBar : Bool =  true
    
    init(headline: HeadlineDomainModel) {
        self.headline = headline
    }
    @ObservedObject var state = ViewModels().getNewsDetailsViewModel().asObserveableObject()
    
    var body: some View {
        ScrollView (.vertical, showsIndicators: false) {
            
            ZStack{
                VStack(alignment: HorizontalAlignment.center, spacing: 12){
                    
                  
                        AsyncImage(url: URL(string: headline.urlToImage),
                                   transaction: Transaction(
                                    animation:.easeIn
                                   )
                        ){phase in
                            
                            
                            switch phase{
                                
                            case .success(let image):
                                
                                image.imageModifier()
                            case .failure(_):
                                Image(systemName:"ant.circle.fill")
                                    .iconModifier()
                            case .empty:
                                Image(systemName:"photo.circle.fill")
                                    .iconModifier()
                                
                            @unknown default:
                                ProgressView()
                            }
                            
                            
                            
                            
                        }.frame(width:.infinity, height: 190)
                
                    
                    Spacer()
                    
                    VStack(alignment: HorizontalAlignment.center, spacing: 16){
                        
                        Text(" \(headline.title )").foregroundColor(Color("Blue"))
                            .font(.system(size: 16))
                            .fontWeight(.bold)
                            .frame(maxWidth: .infinity, alignment: .leading).padding(.init(top: 2, leading: 16, bottom: 2, trailing: 16))
                        
                        HStack(alignment: VerticalAlignment.center, spacing: 16){
                            Text(" \(headline.author)").foregroundColor(Color("Blue"))
                                .font(.system(size: 8))
                                .fontWeight(.regular)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            
                            
                            Text(" \(headline.source )").foregroundColor(Color.green)
                                .font(.system(size: 8))
                                .fontWeight(.regular)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            
                            
                            Text(" \(headline.publishedAt )").foregroundColor(Color("Blue"))
                                .font(.system(size: 8))
                                .fontWeight(.regular)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }.padding(.init(top: 2, leading: 16, bottom: 2, trailing: 16))
                        
                        Text(headline.description_).foregroundColor(Color("Blue"))
                            .font(.system(size: 12))
                            .frame(maxWidth: .infinity, alignment: .leading).padding(.init(top: 2, leading: 16, bottom: 2, trailing: 16))
                        
                        
                    }
                    ZStack{
                        HeadlinesWebView(url: headline.url).frame(height: 400)
                        
                    }
                    
                }
                VStack {
                    Spacer()
                    HStack {
                        Spacer()
                        Button(action: {
                            state.viewModel.onIntent(intent: NewsDetailsScreenEventSaveForLater(headlineDomainModel: headline))
                        }, label: {
                            Image(systemName: "bookmark.circle.fill")
                                .font(.system(.largeTitle))
                                .frame(width: 77, height: 70)
                                .foregroundColor(Color.white)
                               
                        })
                        .background(Color.mint)
                        .cornerRadius(38.5)
                        .padding()
                        .shadow(color: Color.black.opacity(0.3),
                                radius: 3,
                                x: 3,
                                y: 3)
                    }
                }
                
                switch state.state {
                case is NewsDetailsScreenStateIdle:
                    EmptyView()
                    
                case is NewsDetailsScreenStateSuccess:
                   
                    EmptyView().snackBar(message: .constant("Added to read later"), show: $showSnackBar).onDisappear {
                        showSnackBar = true
                    }
                    
                case is NewsDetailsScreenStateSavingForLater:
                    
                    EmptyView().snackBar(message: .constant("Adding to read later"), show: $showSnackBar).onDisappear {
                        showSnackBar = true
                    }
                default:
                    EmptyView()
                    
                }
                
                
            }.padding(.vertical, 16)
        }
    }
}
struct NewsDetails_Previews: PreviewProvider {
    static var previews: some View {
        NewsDetails(headline: HeadlineDomainModel(author: "s", content: "s", description: "s", publishedAt: "s", source: "s", title: "s", url: "s", urlToImage: "s"))
    }
}
