# Read Me First
* The application is called ecb-exchange-service and it calls an ECB endpoint(EXTERNAL) during system startup returning an XML form
* The application also exposes other endpoints which can be consumed by a frontend application. 
* [Swagger UI](http://localhost:9000/swagger-ui.html) - If running in docker replace localhost with ip address assigned by docker

## This application has 
1) ONE External API to ECB for fetching current rates
2) FIVE Internal API's for using the conversions

## How to Run in local/Docker
Simply start com.capital.scalable.LaunchMe

### User Stories
As a user, who accesses this service through a user interface, The user

1. Can retrieve the ECB reference rate for a currency pair, e.g. USD/EUR or HUF/EUR with API signature as <font color="orange">/euro-rate/{currency}</font>
2. Can retrieve an exchange rate for any other pairs, e.g. HUF/USD with <font color="orange">/exchange-rate/{from}/{to}</font> Note !! Also supports EURO here as well
3. Can retrieve a list of supported currencies with <font color="orange">/all-currencies</font>
4. Can convert an amount in a given currency to another with <font color="orange">/convert/{from}/{to}/{amount}</font>
5. Can retrieve a link to a public website showing an interactive chart for a given currency pair.

### Caching
1.  For any internal api call, the external API is checked for any rate changes from ECB. 
2.  For optimization application uses <font color="darkorange">HttpHeaders.IF_MODIFIED_SINCE</font> so that the data processing is minimized and data is not matched on every internal API call
3.  The data is fetched from ECB on application start up and further changes are updated only when ECB publishes new rates (200 OK, NOT 304)

### Storage layer
We need 2 Maps
1) To store rates for different currency pairs.
2) To know how many times the currency pair was requested in our API

### Exceptional condition - use cases

