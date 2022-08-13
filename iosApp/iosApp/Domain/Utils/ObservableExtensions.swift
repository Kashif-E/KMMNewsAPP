//
//  ObservableExtensions.swift
//  iosApp
//
//  Created by Kashif Work on 13/08/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation


import shared



public extension HomeScreenViewModel {
    
    func asObserveableObject() -> HomeScreenViewModelObservableObject{
        return HomeScreenViewModelObservableObject(wrapped: self)
    }
    
}
