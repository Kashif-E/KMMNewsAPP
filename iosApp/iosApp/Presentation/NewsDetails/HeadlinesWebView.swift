//
//  WebView.swift
//  iosApp
//
//  Created by Kashif Work on 20/09/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

import Foundation
import SwiftUI
import WebKit
struct HeadlinesWebView: View {
    let url: String
    
    var body: some View {
        WebView(urlPath: url)
    }
}
struct WebView: UIViewRepresentable {
    var urlPath: String?
    func makeUIView(context: Context) -> WKWebView {
        return WKWebView()
    }
    

    
    func updateUIView(_ uiView : WKWebView , context : Context) {
        DispatchQueue.main.async {
        if let response = urlPath {
            if let url = URL(string: response){
                let request = URLRequest(url: url)
                
                // Make sure you're on the main thread here
                    uiView.load(request)
                        }
               
                 
            }
        }
    }
}
struct DetailsView_Previews: PreviewProvider {
    static var previews: some View {
        HeadlinesWebView(url: "https://google.com")
    }
}
