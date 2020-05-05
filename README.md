# Breezy

A weather app that displays current weather for a hard-coded location (for now, since I plan on including geo-location as well as reverse geo-coding capabilities in due time) at the time of request. It has a swipe-to-refresh functionality that allows the user to refresh weather data at will. Additionally, the app supports a weather auto-refresh feature that the user has control over, and fires a notification when the weather is refreshed in the background.
It’s a pretty simple app, in all honesty, but it tries to use best programming practices and android Architecture components, which are part of the [Android Jetpack](https://www.youtube.com/watch?v=LmkKFCfmnhQ), as recommended by Google.
Also, built entirely on Java.

**Please note, that the app is currently under heavy development and restructuring, but most of the working concepts if any, will remain the same.**

**Functionality yet to implement:**
- [x] ~~Inflating views with Observable live-date using data-binding~~
- [ ] *Adding a Preferences and an Info activity*
- [ ] *Adding the logic to handle user preferences on weather updates*
- [ ] *Geo-location and reverse geo-coding to access user's current location*

# TOOLS
### 1.	[Room](https://www.youtube.com/watch?v=SKWh4ckvFPM)

A layer over SQL. For this project, Room is used to Cache weather data locally on the app

### 2.	[Retrofit](https://www.youtube.com/watch?v=4JGvDUlfk7Y&list=PLrnPJCHvNZuCbuD3xpfKzQWOj3AXybSaM)

To manage the HTTP requests, as well as seamless parsing of the weather data from the server.

### 3.	[JobIntentService](https://www.youtube.com/watch?v=B4gFbWnNpac)

It’s an upgrade for the old Service class, that enables processes to run on a separate thread from the UI thread. It is the service on which the auto-refresh request is enqueued, in my use-case.

### 4.	[RxJava](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=3&cad=rja&uact=8&ved=2ahUKEwjZ3-a-vZrpAhUQkxQKHYWkBjoQFjACegQIAhAB&url=https%3A%2F%2Fgithub.com%2FReactiveX%2FRxAndroid&usg=AOvVaw3R1elAynpxKgfVUYOvIguC)

The app uses a touch of this multithreading API, to insert new weather Data into the Room database, on an IO thread.

### 5.	[Workmanager](https://www.youtube.com/watch?v=pe_yqM16hPQ)

Intelligently manages the background process of hard-refreshing and auto-refreshing the weather data, while considering backward compatibility, network connectivity state as well as battery life.

### 6.	[Data binding](https://www.youtube.com/watch?v=T-nQP9fidKU&t=30s)

Binds UI elements in the app to observable data (in this case, weather properties which are LiveData)

### 7.	[LiveData](https://www.youtube.com/watch?v=OMcDk2_4LSk&t=33s)

Notifies UI components for changes in Weather data properties, and prompts them to change.

### 8.	[ViewModel component](https://www.youtube.com/watch?v=5qlIPTDE274)

Manages UI-related data in a lifecycle-conscious way


### 9.	[SharedPreferences](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=11&cad=rja&uact=8&ved=2ahUKEwiI697wuZrpAhUKdxoKHXb5BcgQFjAKegQIAxAB&url=https%3A%2F%2Fdeveloper.android.com%2Ftraining%2Fdata-storage%2Fshared-preferences&usg=AOvVaw1pvPHLCKq3V7wVe7Md_iyT)

A class in android used to saving small packets of data. In this case, I used it to save user preferences regarding the frequency of weather auto-refresh.

### 10.	[DarkSky API](https://darksky.net/dev)

The Web service from which the app requests weather data


 
