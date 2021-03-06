# Breezy

- Displays current weather for a hard-coded location (for now) at the time of request.
- Has a swipe-to-refresh functionality that allows the user to refresh weather data at will. 
- Supports a weather auto-refresh feature that the user has control over
- Fires a notification when the weather is refreshed in the background.

It’s a pretty simple app, in all honesty, but it tries to use best programming practices and android Architecture components, which are part of the [Android Jetpack](https://www.youtube.com/watch?v=LmkKFCfmnhQ), as recommended by Google. **Also, contributions are most welcome.**

**Functionality yet to implement:**
- [x] ~~Inflating views with Observable live-date using data-binding~~
- [x] ~~Adding a Preferences and an Info activity~~
- [x] ~~Logic to handle user preferences on weather updates~~
- [x] ~~Splash Screen~~
- [ ] *Geo-location and reverse geo-coding to access user's current location*
- [ ] *Update weather based on User's location*

## Screenshots

|<img src='shots/1.png' width='200'/>|<img src='shots/2.png' width='200'/>|<img src='shots/3.png' width='200'/>|
|:--:|:--:|:--:|
|Splash Screen|Empty Weather|Main Activity|

|<img src='shots/4.png' width='200'/>|<img src='shots/5.png' width='200'/>|<img src='shots/6.png' width='200'/>|
|:--:|:--:|:--:|
|Info Activity|Preferences Activity|Notification|

## Tools
#### 1.	[Room](https://www.youtube.com/watch?v=SKWh4ckvFPM)

A layer over SQL. For this project, Room is used to Cache weather data locally on the app

#### 2.	[Retrofit](https://www.youtube.com/watch?v=4JGvDUlfk7Y&list=PLrnPJCHvNZuCbuD3xpfKzQWOj3AXybSaM)

To manage the HTTP requests, as well as seamless parsing of the weather data from the server.

#### 3.	[JobIntentService](https://www.youtube.com/watch?v=B4gFbWnNpac)

It’s an upgrade for the old Service class, that enables processes to run on a separate thread from the UI thread. It is the service on which the auto-refresh request is enqueued, in my use-case.

#### 4.	[RxJava](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=3&cad=rja&uact=8&ved=2ahUKEwjZ3-a-vZrpAhUQkxQKHYWkBjoQFjACegQIAhAB&url=https%3A%2F%2Fgithub.com%2FReactiveX%2FRxAndroid&usg=AOvVaw3R1elAynpxKgfVUYOvIguC)

The app uses this multithreading API to perform database operations, on an IO thread.

#### 5.	[Workmanager](https://www.youtube.com/watch?v=pe_yqM16hPQ)

Intelligently manages the background process of hard-refreshing and auto-refreshing the weather data, while considering backward compatibility, network connectivity state as well as battery life.

#### 6.	[Data binding](https://www.youtube.com/watch?v=T-nQP9fidKU&t=30s)

Binds UI elements in the app to observable data (in this case, weather properties which are LiveData)

#### 7.	[LiveData](https://www.youtube.com/watch?v=OMcDk2_4LSk&t=33s)

Notifies UI components for changes in Weather data properties, and prompts them to change.

#### 8.	[ViewModel component](https://www.youtube.com/watch?v=5qlIPTDE274)

Manages UI-related data in a lifecycle-conscious way


#### 9.	[SharedPreferences](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=11&cad=rja&uact=8&ved=2ahUKEwiI697wuZrpAhUKdxoKHXb5BcgQFjAKegQIAxAB&url=https%3A%2F%2Fdeveloper.android.com%2Ftraining%2Fdata-storage%2Fshared-preferences&usg=AOvVaw1pvPHLCKq3V7wVe7Md_iyT)

A class in android used to saving small packets of data. In this case, it was used to save user preferences regarding the frequency of weather auto-refresh.

#### 10.	[DarkSky API](https://darksky.net/dev)

The Web service from which the app requests weather data


 
