//
//  HeadlineRow.swift
//  iosApp
//
//  Created by Kashif Work on 14/08/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared


struct  HeadlineRow: View {
  
    var headline :  HeadlineDomainModel
    var onclick:  () -> ()
    
    
    init( headline : HeadlineDomainModel, onclick: @escaping () -> ()){
        

        self.headline = headline
        self.onclick = onclick
        
       
    }
    var body: some View {
                
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
                                           
                                           
                                           
                                           
                                       }.frame(width:.infinity, height: 190).cornerRadius(12).padding(0)
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
                        Spacer()
                               
                                   
                               
                                   
                               }
                }.background(
                    RoundedRectangle(cornerRadius: 12, style: .continuous)
                        .fill(.white).shadow(radius:  2)
                ).onTapGesture {
                    onclick()
                }
            
        }
    }





