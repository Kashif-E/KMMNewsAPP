//
//  ReadLaterObservableObject.swift
//  iosApp
//
//  Created by Kashif Work on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Combine
import shared
public class ReadLaterObsservableObject: ObservableObject {
    
    var viewModel : ReadLaterViewModel
    
   /**
    *
    * state flow acts as a state for swift ui here
    *
    */
    @Published private(set) var state: ReadLaterState
    
    /**
     **
     *fusing the .asObserveable extension funstion we get the wrapped viewmodel and the stateflow
     */
    init(wrapped: ReadLaterViewModel) {

        viewModel = wrapped
        state = wrapped.state.value as! ReadLaterState
        (wrapped.state.asPublisher() as AnyPublisher<ReadLaterState, Never>)
            .receive(on: RunLoop.main)
            .assign(to: &$state)

    }
        
}


public extension ReadLaterViewModel {
    
    func asObserveableObject() -> ReadLaterObsservableObject{
        return ReadLaterObsservableObject(wrapped: self)
    }
    
}
