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
    
    @ObservedObject var viewmodel = ViewModels().getScreenViewModel().asObservableObject()
    var body: some View {
        Text(viewmodel.state.message).font(.largeTitle).fontWeight(.semibold).foregroundColor(Color.blue).multilineTextAlignment(.center)
    }
}

struct Screen_Previews: PreviewProvider {
    static var previews: some View {
        Screen()
    }
}
