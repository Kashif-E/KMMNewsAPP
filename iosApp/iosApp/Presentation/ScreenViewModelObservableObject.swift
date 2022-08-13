//
//  ScreenViewModelObservableObject.swift
//  iosApp
//
//  Created by Kashif Work on 13/08/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Combine
import shared
public class ScreenViewModelObservableObject : ObservableObject {
    
    var viewModel : ScreenViewModel
   /**
    *
    * state flow acts as a state for swift ui here
    *
    */
    @Published private(set) var state: ScreenState
    
    /**
     **
     *fusing the .asObserveable extension funstion we get the wrapped viewmodel and the stateflow
     */
    init(wrapped: ScreenViewModel) {

        viewModel = wrapped
        state = wrapped.state.value as! ScreenState
        (wrapped.state.asPublisher() as AnyPublisher<ScreenState, Never>)
            .receive(on: RunLoop.main)
            .assign(to: &$state)


    }
    

    
}
