Bux Assignment
===========================================================

This is an application implemented for Bux technical assignemnt. Th application uses Android Architecture Components with Dagger 2.

Introduction
-------------

### Functionality
The app is composed of 2 main screens.
#### ProductListFragment
Allows you to retrieve products from Bux beta backend.
Each search product is kept in the database in `ProductEntity`, `ProductPriceEntity` and `ProductRangeEntity` tables where
info about product, its price and daily-yearly range is stored.

A swipe to refresh layout is used to refresh products. In case of unsuccessful response, message is displayed via 
`Snackbar`.

#### ProductDetailFragment
This fragment displays the details of the selected product. User can subscribe/unsubscribe to product in order receive real time updates.
When user subscribes to a product, any real time price change is reflected to current price text box.

#### ProductSubscriptionService
A service that listens socket connection. This service observes `ProductSubscriptionEntity` table. In case of any update in the table,
sends updates via socket connection. Any incoming data via socket is handled here and gets written to `ProductSubscriptionEntity` table.

### Building
You can open the project in Android studio and press run.
### Testing
The project uses both instrumentation tests that run on the device
and local unit tests that run on your computer.

#### Device Tests
##### UI Tests
The projects uses Espresso for UI testing. Since each fragment
is limited to a ViewModel, each test mocks related ViewModel to
run the tests.
##### Database Tests
The project creates an in memory database for each database test but still
runs them on the device.

#### Local Unit Tests
##### ViewModel Tests
Each ViewModel is tested using local unit tests with mock Repository
implementations.
##### Repository Tests
Each Repository is tested using local unit tests with mock web service and
mock database.
##### Webservice Tests
The project uses [MockWebServer][mockwebserver] project to test REST api interactions.


### Libraries
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Android Data Binding][data-binding]
* [Dagger 2][dagger2] for dependency injection
* [Retrofit][retrofit] for REST api communication
* [espresso][espresso] for UI tests
* [mockito][mockito] for mocking in tests


[mockwebserver]: https://github.com/square/okhttp/tree/master/mockwebserver
[support-lib]: https://developer.android.com/topic/libraries/support-library/index.html
[arch]: https://developer.android.com/arch
[data-binding]: https://developer.android.com/topic/libraries/data-binding/index.html
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
[dagger2]: https://google.github.io/dagger
[retrofit]: http://square.github.io/retrofit
[mockito]: http://site.mockito.org