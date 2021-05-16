# Parc Ferme
Formula One stats app for Android

## Application Overview

Parc Ferme is a Formula One stats app for Android. Data is obtained from the [Ergast Developer API](https://ergast.com/mrd/), a web service which provides historical motor racing data.

There are 4 top level destinations which are navigated between using a bottom navigation menu. These are: drivers, constructors, races, and standings. There are 3 secondary destinations which are navigated to from the primary destinations. These are: driver details, constructor details, and race details. In Formula One *constructor* is synonymous with *team*.

### Drivers
The drivers destination displays a list of drivers filtered by season. The season can be changed using a chip. Each item in the list contains the drivers national flag, full name, and number if available. Selecting a driver will navigate to the driver detail destination.

### Driver Detail
The driver detail destination displays information about the driver along with a list of standings for each season the driver has competed in. The action bar contains the drivers full name and number if available. The header contains the driver's national flag, wins, championships, nationality, and date of birth. Each item in the standings list contains the season, position, constructor(s), points, and wins. Selecting a standing will navigate to the standings destination, filtered to show driver standings for the relevant season.

### Constructors
The constructors destination displays a list of constructors filtered by season. The season can be changed using a chip. Each item in the list contains the constructors national flag and name. Selecting a constructor will navigate to the constructor detail destination.

### Constructor Detail
The constructor detail destination displays information about the constructor along with a list of standings for each season the constructor has competed in. The action bar contains the constructor name. The header contains the constructor's national flag, wins, championships, and nationality. Each item in the standings list contains the season, position, wins, and points. Selecting a standing will navigate to the standings destination, filtered to show constructor standings for the relevant season.

### Races
The races destination displays a list of races filtered by season. This can include future races. The season can be changed using a chip. Each item in the list contains the race national flag, name, circuit name, and date.Selecting a race will navigate to the race detail destination.

### Race Detail
The race detail destination displays information about the race along with a list of results. The results can be switched between race or qualifying using chips. The action bar contains the race name. The header contains the race national flag, circuit name, date, and time (if available). If the race is in the future a reminder can be set or canceled using an icon in the action bar. A notification is sent when the race start time is reached. Each item in the list contains the drivers position, national flag, full name, constructor, time, and points (for race only). Selecting a result will navigate to the driver detail destination for the relevant driver.

### Standings
The standings destination displays a list of standings filtered by season. The season can be changed using a chip. The standings can be switched between driver and constructor standings using chips. Each item in the list contains the driver or constructor position, national flag, name, constructor (for driver), points, and wins. Selecting a standing will navigate to the relevant driver or constructor details destination.

The driver details, constructor details, and race details destinations all use animations which decrease the header size as the list scrolls down in order to maximise screen usage.

Data is fetched automatically as needed and cached in a database. Cached data is considered fresh for 24 hours and used in preference to requesting new API data. If for any reason data cannot be fetched an error message is displayed. All lists use swipe to refresh. Pulling down on a list will force a data refresh, bypassing the cache and requesting new API data. A menu item also exists for refreshing a list.


## Project Milestones

1. Create the design document.
2. Initial application implementation. Has top level destinations with navigation and uses data directly from API.
3. Add secondary destinations. Data is still used directly from API.
4. Add database for caching. Data is used via the cache (single source of truth).
5. Add polish to the user interface.
6. Add animation and notifications.
7. Test on a variety of devices and API versions. Fix any outstanding issues and add final polish to reach market-ready form.


## Rubric Requirements

### Build a navigable interface consisting of multiple screens of functionality and data.
* Application includes 7 distinct screens.
* Android Navigation Controller is used.
* Data is passed between primary and secondary level fragments.

### Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.
* ConstraintLayout is used throughout.
* Data collections are displayed effectively.
* Resources are stored appropriately.
* Every element within ConstraintLayout includes an id field and at least 1 vertical constraint.
* Data collections are displayed using RecyclerViews.

### Animate UI components to better utilize screen real estate and create engaging content.
* MotionLayout is used in the header of each of the details fragments.
* MotionScenes are used and contain Transition nodes and ConstraintSet blocks.
* Constraints are defined within the scenes and house all layout params for the animation.

### Connect to and consume data from a remote data source such as a RESTful API.
* Connects to external API using Retrofit2.
* Data is stored in local models, Gson is used to deserialize API data.
* Kotlin Coroutines are used for network requests.

### Load network resources, such as Bitmap Images, dynamically and on-demand.
* Remote data is loaded asynchronously and only when needed.
* Placeholder text is used while data is loaded. Failed network requests are handled, error messages are displayed when appropriate.
* All requests are performed asynchronously using Kotlin Coroutines.

### Store data locally on the device for use between application sessions and/or offline use.
* Room database is used as a data cache.
* Cached data is accessible across user sessions.
* Data storage operations use Kotlin Coroutines or LiveData.
* Data is structured with appropriate data types. Domain, database, and dto objects are used.

### Architect application functionality using MVVM.
* MVVM Pattern is used.

### Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle.
* Layout is optimized for orientation changes.
* State is saved and restored using ViewModels.
* Data is persisted where appropriate. Lifecycle events do not cause unexpected behaviour.
* Pending intent is used to launch the correct fragment within the application when notification is selected.
* Required permissions are present.

### Utilize system hardware to provide the user with advanced functionality and features.
* Race reminder notifications can be set and cancelled.
* Notification uses WorkManager and works correctly when the application is not in the foreground.
