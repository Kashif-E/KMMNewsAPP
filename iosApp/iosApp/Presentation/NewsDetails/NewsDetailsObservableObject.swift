//
//  NewsDetailsObservableObject.swift
//  iosApp
//
//  Created by Kashif Work on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//



import Foundation
import Combine
import shared
public class NewsDetailsObserveableObject : ObservableObject {
    
    var viewModel : NewsDetailsViewModel
    
   /**
    *
    * state flow acts as a state for swift ui here
    *
    */
    @Published private(set) var state: NewsDetailsScreenState
    
    /**
     **
     *fusing the .asObserveable extension funstion we get the wrapped viewmodel and the stateflow
     */
    init(wrapped: NewsDetailsViewModel) {

        viewModel = wrapped
        state = wrapped.state.value as! NewsDetailsScreenState
        (wrapped.state.asPublisher() as AnyPublisher<NewsDetailsScreenState, Never>)
            .receive(on: RunLoop.main)
            .assign(to: &$state)

    }
        
}


public extension NewsDetailsViewModel {
    
    func asObserveableObject() -> NewsDetailsObserveableObject{
        return NewsDetailsObserveableObject(wrapped: self)
    }
    
}
