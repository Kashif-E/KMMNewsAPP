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
    
    init(headline: HeadlineDomainModel) {
        self.headline = headline
    }
    
    var body: some View {
        ScrollView (.vertical, showsIndicators: false) {
            
          
                VStack{
                    VStack(alignment: HorizontalAlignment.center, spacing: 12){
                        
                        if #available(iOS 15.0, *) {
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
                        } else {
                            // Fallback on earlier versions
                        }
                        
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
                            HeadlinesWebView(url: headline.url).frame(height: 500)
                            
                        }
                      
                    }
                
                }
                
               
            
        
            
        }
    }
}

struct NewsDetails_Previews: PreviewProvider {
    static var previews: some View {
        NewsDetails(headline: HeadlineDomainModel(author: "s", content: "s", description: "s", publishedAt: "s", source: "s", title: "s", url: "s", urlToImage: "s"))
    }
}
