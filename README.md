# CountingApp
Here you have an android app written on kotlin

# How To use it?
You can use Android Studio or Intellij to work with this repository.
Use an emulator or android device with android version   23 <= API <= 29

# Features
* Add a named counter to a list of counters.
* Increment any of the counters.
* Decrement any of the counters.
* Delete a counter.
* Show a sum of all the counter values.
* Search counters.
* Enable sharing counters.
* Handle batch deletion.
* Persist data back to the server.
* States are important, consider handling each state transition properly.

# Instructions
Setup EndPoint of your service replace it on build.gradle(:app) as BASE_URL  (line 25)
for example if your endPoint is "https://dev-api.yourbackend.com/" let it as its on the project, but if is different you can
use '"https://10.0.2.2:3000/"'   local host with port 3000 for virtual device or the URL of your remote API service.

I Suggest use https://ngrok.com/ to  publish the service if is in localHost in a public https URL to test on many devices (emulator, real..)

# Demo
![alt text](https://github.com/aliceresponde/CountingApp/blob/master/info/counterDemo.gif)

# Architecture
![alt text](https://github.com/aliceresponde/CountingApp/blob/master/info/android_clean_repository_arch.png)

There the main layers:
data: in this module has all details Service and Database
repository : has sync server
domain: each use case performed by the user
presentation: This module has all android framework using MVVM

# Used Libraries
* [Android X Preference](https://developer.android.com/jetpack/androidx/releases/preference)
* [Data Binding](https://codelabs.developers.google.com/codelabs/android-databinding/index.html?index=..%2F..index#5)
* [Navigation Component](https://codelabs.developers.google.com/codelabs/android-navigation/index.html?index=..%2F..index#0)
* [Room](https://developer.android.com/jetpack/androidx/releases/room)
* [Retrofit2](https://square.github.io/retrofit/)
* [Coroutines](https://developer.android.com/kotlin/coroutines)

# Best Practices And References
[Dependency Inversion Principle (DIP)](https://martinfowler.com/articles/dipInTheWild.html)(without frameworks)
[Clean Code)](https://www.amazon.com/-/es/Robert-C-Martin/dp/0132350882)
