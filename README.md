# KMMNewsAPP

<a href="https://www.buymeacoffee.com/kashifmehmood"><img src="https://img.buymeacoffee.com/button-api/?text=Buy me a coffee&emoji=&slug=kashifmehmood&button_colour=FFDD00&font_colour=000000&font_family=Cookie&outline_colour=000000&coffee_colour=ffffff" /></a>


Here's a video of how it looks. You can alwasy clone and check by yourself.

[Untitled.webm](https://user-images.githubusercontent.com/61690178/197214466-5e16ea33-abcc-4ed1-81fd-a9a74fe32065.webm)

## What's shared?

Everything, except the UI. 
* Android UI is in Compose
* IOS ui is in SwiftUI

What does it have?

* A complete skeleton of a kmm project that will help you follow clean architecture.
* Dependency injection is set up using "The Mighty KOIN". You just have to add you dependencies.
connected with both ios and android.
* Shared ViewModel with stateflow for managing states in jetpack compose and swift ui
* Realm for local storage and caching
* Naterial 3 for android UI theming


Dont forget to enable the new memory model in you gradle properties

```groovy
kotlin.native.binary.memoryModel=experimental
```


## News APP


I have used newsapi.org (News Api) to build the app to demonstrate how everything should be layed out in order to build the app.
You can communicate with the API to get news and also use Realm to add a news to readlater an available offline.

## KMM is Awesome <3 and continuously improving. If you found an issue or want to contribute feel free to contact <3 

```kotlin

operator fun invoke() = "KMM is Awesome"

```




