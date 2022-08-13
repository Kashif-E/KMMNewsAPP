# KMMNewsAPP




There are two branches
1. Main
2. News App 


## Main

The main branch is a complete template that you can clone and use to build the awesome app that you were thinking about when you opened this repository.
## What's shared?

Everything, except the UI. 
* Android UI is in Compose
* IOS ui is in SwiftUI

What does it have?

* A complete skeleton of a kmm project that will help you follow clean architecture.
* Dependency injection is set up using "The Mighty KOIN". You just have to add you dependencies.
connected with both ios and android.
* Shared ViewModel with stateflow for managing states in jetpack compose and swift ui


Dont forget to enable the new memory model in you gradle properties

```groovy
kotlin.native.binary.memoryModel=experimental
```

## Main Template

<img width="1652" alt="Screenshot 2022-08-13 at 8 58 01 PM" src="https://user-images.githubusercontent.com/61690178/184502988-b0a7f50c-364a-4426-b619-05a260756e74.png">

## News APP
In the news app branch, its a sample app built using the template in the main app. 

I have used newsapi.org (News Api) to build the app to demonstrate how everything shoudl be layed out in order to build the app.

Here's a screenshot of how it looks. You can alwasy clone and check by yourself.

<img width="1506" alt="Screenshot 2022-08-14 at 3 36 36 AM" src="https://user-images.githubusercontent.com/61690178/184515682-89cf8dee-6f90-45eb-bb7a-e8ca80a3a28e.png">


## KMM is Awesome <3 and continuously improving. If you found an issue or want to contribute feel free to contact <3 

```kotlin

operator fun invoke() = "KMM is Awesome"

```




