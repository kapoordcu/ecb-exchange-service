# Read Me First
* The application is called ecb-exchange-service and it calls an ECB endpoint(EXTERNAL) during system startup returning an XML form
* The application also exposes other endpoints which can be consumed by a frontend application. 
* [Swagger UI](http://localhost:9000/swagger-ui.html) - If running in docker replace port with assigned by docker

## This application supports 
1) ONE External API to ECB for fetching current rates
2) FIVE Internal API's for using the conversions
3) Currencies in lowerCase or upperCase

## How to Run in local
Simply start com.capital.scalable.LaunchMe

### User Stories
As a user, who accesses this service through a user interface, The user

1. Can retrieve the ECB reference rate for a currency pair, e.g. USD/EUR or HUF/EUR with API signature as <font color="orange">/euro-rate/{currency}</font>

    
    Sample Response:
    
    {
      "fromCurrency": "EUR",
      "toCurrency": "jpy",
      "fromAmount": 1,
      "toAmount": 124.58
    }
    
2. Can retrieve an exchange rate for any other pairs, e.g. HUF/USD with <font color="orange">/exchange-rate/{from}/{to}</font> Note !! Also supports EURO here as well


    Sample Response:
    
    {
      "fromCurrency": "inr",
      "toCurrency": "jpy",
      "fromAmount": 1,
      "toAmount": 1.44
    }
    
3. Can retrieve a list of supported currencies with <font color="orange">/all-currencies</font>. Call this endpoint to also find out the frequency of currency conversion calls


    Sample Response:
    
    [
        {"currency":"CHF","accessCount":0},
        {"currency":"HRK","accessCount":0},
        {"currency":"MXN","accessCount":0},
        {"currency":"ZAR","accessCount":0},
        {"currency":"INR","accessCount":4},
        {"currency":"CNY","accessCount":0},
        {"currency":"THB","accessCount":0},
        {"currency":"AUD","accessCount":1},
        {"currency":"ILS","accessCount":0},
        {"currency":"KRW","accessCount":0},
        {"currency":"JPY","accessCount":2},
        {"currency":"PLN","accessCount":0},
        {"currency":"GBP","accessCount":0},
        {"currency":"IDR","accessCount":0},
        {"currency":"HUF","accessCount":0},
        {"currency":"PHP","accessCount":0},
        {"currency":"TRY","accessCount":0},
        {"currency":"RUB","accessCount":0},
        {"currency":"ISK","accessCount":0},
        {"currency":"HKD","accessCount":0},
        {"currency":"EUR","accessCount":4},
        {"currency":"DKK","accessCount":0},
        {"currency":"USD","accessCount":1},
        {"currency":"CAD","accessCount":0},
        {"currency":"MYR","accessCount":0},
        {"currency":"BGN","accessCount":0},
        {"currency":"NOK","accessCount":0},
        {"currency":"RON","accessCount":0},
        {"currency":"SGD","accessCount":0},
        {"currency":"CZK","accessCount":0},
        {"currency":"SEK","accessCount":0},
        {"currency":"NZD","accessCount":0},
        {"currency":"BRL","accessCount":0}
    ]
    
4. Can convert an amount in a given currency to another with <font color="orange">/convert/{from}/{to}/{amount}</font>


    Sample Response:
    
    {
      "fromCurrency": "cny",
      "toCurrency": "thb",
      "fromAmount": 10,
      "toAmount": 45.93
    }
    
5. Can retrieve a link to a public website showing an interactive chart for a given currency pair. <font color="orange">/trends/{currency}</font>
    
    
    This endpoint opens the graphic chart from ECB website, depending on the currency you pass (uppercase/lowercase both supported)

### Caching
1.  For any internal api call, the external API is checked for any updates from ECB. 
2.  For optimization application uses <font color="darkorange">HttpHeaders.IF_MODIFIED_SINCE</font> so that the data processing is minimized and data is not processed on every internal API call
3.  The data is fetched from ECB on application start up and further changes are updated only when ECB publishes new rates (200 OK, NOT 304)

### Storage layer
We need 2 Maps
1) To store rates for different currency pairs.
2) To know how many times the currency was requested by our API, Every API call maintains the frequency of conversion for each currency in the pair.  Endpoint for this <font color="orange">/all-currencies</font>


### Exceptional condition - other use cases
1)  If you enter a currency whose keyword is not identified by ECB, for example typing Euro (not Eur), then you will get 404, To see list of currency supported check endpoint <font color="orange">/all-currencies</font>
2)  From and To currency fields can be same
3)  usD, USD, Usd ---> all are treated as USD
4)  The graphical chart only shows the currency graph against Euro, Since ECB assumes euro as base currency

# DOCKER COMPOSE
The docker command to run in A or B is same, only docker file differs

    i) docker-compose up
    ii) docker-compose ps --> will give the IP address assigner by docker to your application
    ii) Access the application on http://localhost:32768/swagger-ui.html
    
### A (Make sure artifact exists in your local machine under target/)
a)  Use your Local machine to build the application (using mvn package)
b)  Use Docker to run the application

Dockerfile for A

    FROM openjdk:10-jre-slim
    COPY ./target/ecb-exchange-service-0.0.1-SNAPSHOT.jar /usr/src/ecb.jar
    WORKDIR /usr/src
    EXPOSE 50590
    CMD ["java", "-jar", "ecb.jar"]
    

### B

a)  Use Docker machine to build the application 
b)  Use Docker to run the application

Dockerfile for B

    FROM maven:3.5.4-jdk-10-slim
    WORKDIR /usr/src/ecb
    COPY . /usr/src/ecb
    RUN mvn package
    
    WORKDIR /usr/src/ecb
    RUN cp /usr/src/ecb/target/*.jar ./ecb.jar
    EXPOSE 8080
    CMD ["java", "-jar", "ecb.jar"]
    
    
.- To remove all docker installation (including prior dockers) do (in terminal):
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker rmi -f $(docker images -q)
docker volume prune

### TESTING
Due to time limit, simple unit tests for internal APIs are added ONLY