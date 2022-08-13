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
    var body: some View {
        
        ZStack(){
            
           
            switch true {
                
            case viewmodel.state.error.isError:
                
                
                
                Text(viewmodel.state.error.errorMessage)
                
                
                
                
                
            case viewmodel.state.isSuccess:
                
                
                List{
                    
                    ForEach(viewmodel.state.headlines , id: \.title){ item in
                        HeadlineRow( headline: item,onclick: {
                            
                            print("==> ")
                            
                        })
                    }

                }.frame( maxWidth: .infinity)
                    .edgesIgnoringSafeArea(.all)
                    .listStyle(PlainListStyle())
                
                
                
            case viewmodel.state.isLoading:
             
                ProgressView()
                
            default:
                Text("default")
                
                
                
            }
        }.onAppear(perform: {
            viewmodel.viewModel.onIntent(intent: HomeScreenSideEffects.GetHeadlines())
        })
      
        
        
    }
}

struct Screen_Previews: PreviewProvider {
    static var previews: some View {
        Screen()
    }
}
