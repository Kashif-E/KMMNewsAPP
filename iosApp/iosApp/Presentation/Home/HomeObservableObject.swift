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
public class HomeObservableObject : ObservableObject {
    
    var viewModel : HomeScreenViewModel
    
   /**
    *
    * state flow acts as a state for swift ui here
    *
    */
    @Published private(set) var state: HomeScreenState
    
    /**
     **
     *fusing the .asObserveable extension funstion we get the wrapped viewmodel and the stateflow
     */
    init(wrapped: HomeScreenViewModel) {

        viewModel = wrapped
        state = wrapped.state.value as! HomeScreenState
        (wrapped.state.asPublisher() as AnyPublisher<HomeScreenState, Never>)
            .receive(on: RunLoop.main)
            .assign(to: &$state)


    }
    

    
}





public extension HomeScreenViewModel {
    
    func asObserveableObject() -> HomeObservableObject{
        return HomeObservableObject(wrapped: self)
    }
    
}
