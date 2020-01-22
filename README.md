# WeatherApplication
> A simple weather android app that offers you the ability to search for the weather of the cities and detects forecast weather for 5 days 3 hours in your current location automatically. There are many information in weather application which includes weather condition, wind speed, minimum and maximum temperature.

# Requirements
 - Build with Android Studio 3.5.3 [Android Studio](https://developer.android.com/studio/)
 
# Features
 - MVVM with clean architecture
 - Retrofit2
 - RxJava
 - Mockito for unit test
 - Google Material Design
 - Viewpager 2 
 - Glide 
 - Android Navigation
 - Androidx compatibility
 - Android KTX
 - Fetching location using Google play services
 - Simple and clean 

# Clone and building project

Clone the project from Git repo and apply the two steps below:
- Sync Gradle
- Rebuild Project

# Run and install the app

- Congfigure an android emulator or an android device
- Run the app using the run button on the connected emulator/device

# Run the unit test

- Right click the CitiesWeatherViewModelTest.java and click Run 'CitiesWeatherViewModelTest'
- Right click the CurrentWeatherViewModelTest.java and click Run 'CurrentWeatherViewModelTest'

# Unit test

The unit test of CitiesWeatherViewModelTest do the below job :
- Success Test : Call API to get the weather of city named chicago and compare the result that should be obtained
- Failed Test : throw an error and compare the result that should be obtained

The unit test of CurrentWeatherViewModelTest do the below job :
- Success Test : Call API to get the current weather of city having the longitude and latitude and compare the result that should be obtained like temp maximum and minimum
- Failed Test : throw an error and compare the result that should be obtained


# Generate a coverage report

Run below command to generate coverage report in Terminal window of Android Studio:

```
./gradlew createDebugCoverageReport
```

# Author

Sarah Geara
