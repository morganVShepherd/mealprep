# A Service to generate Meal's for the week
This is something I've been meaning to put together for awhile. Every week myself and my wife plan different 
dinners Mon - Sun and breakfasts for Sat and Sun.

This service will pull from a library of Recipes and generate the meal prep for the week as well as return a 
shopping list to be copied to the shopping list app of choice. 

Currently the service only needs to remove Meals for days that we have other plans for the next week.
It is intentional that meals cannot be replaced as one should cycle through all meals in the library before any can be 
used again.

### Contents
#### 1. Installing the service
#### 2. Running the service
#### 3. Design decisions
#### 4. Testing
#### 5. TODOs

## 1. Installing the service
The Service requires connectivity access to a relational DB, I used postgres.
you will need to update your details in the application.properties.

Once your DB is set up you should run the scripts in resources/extra/DBsetup.sql.
(my normal choice would be using flyway but I figured this way made it much easier to self contain.)

## 2. Running the service
The Project itself is Built using Maven and the Spring framework.leveraging Spring boot all you need to do
to get the service going is using your CLI client of choice navigate to the directory that the pom file lives in and
run 'mvn spring-boot:run'.

You will then need to use restful requests to Create at least 7 Dinners and 2 Breakfasts (otherwise there will not be
enough meals for preparation).

There are some saved Postman scripts in the resources/extras/postman folder which should help.

## 3. Design decisions

#### Dependency injection
Decided to use '@Autowired' annotation as its a habit, probably better off to use Constructor injection for Dependency 
Injection.

#### Controllers
All controllers inherit from a Root Controller. This is to ensure tah they all have sight and access to the Generic 
Exception handler for Bean validation on the incoming Data Transfer Objects (DTO's).

 - Using jakarta.validation for DTO's details.
 - In keeping with Single Use of Solid all a controller does is accept the request and its parameters and pass it down 
   to the service.
 - The endpoints themselves are self explanatory by name and  as to what they do and Stick to the rules behind each 
   method (get retrieves, Post creates, Delete removes, Patch updates).
 - All Endpoints catch a Custom Exception which all Services throw, more on that in Exception handling.

#### DTO's and Entities

 - Using Lombok to help deal with most of the boilerplate code.
 - Separate  objects for data Transferring and DB processing.
 - DTO's are using Jakarta Validation for basic rules.
 - Entities using Jakarta persistence for Column details/DB Mapping. 
 - Decided to Manually track the Bean hierarchy (DTO's have objects Entities only have ID's) because: 
   - Very simple hierarchical structure.
   - No need to lazy load on calls that may not need all the nested details.
   - saves processing power by helping prevent db overheads.
 - DTO's use a String for Id, Entities a Long more on that later when going through 'Utils' Package.
 - Because I am not utilizing the Eager and Lazy mapping methods and the beans are very simple I opted to bypass a 
   mapper and just overload the constructors to convert Entities to DTO's and visa-versa.

#### Exception

- POJO Error that is a variable in our Custom Exception, making it very easy to update what we want to store and
  communicate when handling exceptions.
- The Error constants file is also useful as it maps the 'Error code' generated from the DTO validation to a custom
  message making it easier to update them all in one go. in a bigger application these would actually pull from local 
  files for language changes.
- I generally like to Keep my Error Constants separate, saves from having a massive constants file.
- Custom RestException has a method that generates a Restful Response based on the error, keeps all our error responses
  consistent  and easy to manage should there be changes.
- Having all exceptions channel through our custom Exceptions makes it easier to have control on how and what is logged,
  sent to our logging application and or how we deal with errors (Kibana, Splunk, Email Notifications etc). 

#### POJO's and Repositories

 - Plain Java Objects.
 - Spring JPA repositories, another part of the spring framework that just makes dev a little easier.

#### Service Layer

- Services are initially set as interfaces for ease of testing as well as sticking to the Open Close principle and 
  polymorphism.
- Every Service implementation  method should catch generic Exception and convert it to the Custom Restful Exception 
  before sending back up the line to eventually be handled by the controller.
- I believe each repo layer should only be accessible  by a single service this enables the services to maintain the 
  single point of logic for its Entities via its DTO's.

#### Utils
- Every service's has some utility classes, the one I normally always put in but haven't as this app does not require
  much in the way of logging is a Logger Util, as much as I enjoy the Lombok simple logging annotations I like to have 
  more control over what my logs are going to do based on codes put into the Exceptions etc.
- There is an ID converter in the App that just converts from Long to String and back again, normally this would 
  hash/encrypt the ID's before they are in the top layer as that's important data that we wouldn't like malicious parties
  to easily own.
- The Metric Converter is on the "To do" list, as part of the shopping list we may need to convert cups to grams 
  etc, It will probably move to the Meal prep service as I can't see it being required by any other functionality
- Pretty Print is also for the Shopping list and at the moment all it does is return a generic String based off a simple
  template. Over time the user may want to get the response in Json or CSV or even a File?


## 4. Testing

Leveraging the @Tag annotation we can choose which tests we want to run during different stages of our pipelines
unit, contract or integration.

to run all tests via command line just use 'mvn test'

#### Unit testing
Unit testing is the backbone of all applications as it check that every single logical interaction is covered, I think 
a healthy application should not struggle to maintain 90% plus of test coverage.

To run just this group use the command 'mvn test -Dgroups=Unit-Test'

#### Contract Testing
Contract testing is validating our restful contracts are working as expected, this will target our controllers 
and validate our DTO's, since there could be 100's of iterations per contract we use mock mvc as we want these tests to 
be quick.
(P.S in the era of Microservices we should NEVER rely on the upstream applications to manage our validation that's just 
asking for future issues)

To run just this group use the command 'mvn test -Dgroups=Contract-Test'

#### Integration Testing
These are the heaviest and slowest of our tests, we use these tests by starting up an actual instance of our application
and sending a request to the top layer, letting it travel through all our lower layers into our DB and then check the 
response is what's expected, Often this will be done using Spock, Cucumber etc  
(This is todo as i'm trying to decide which to use)

Because these tests are very intensive I normally have them split into 2 groups. There is the "Happy Path" which will be 
run as part of the build before an artifact is made and then there is the intense Integration testing that would do as 
much as possible but would be used more on the staging environment as validation that all is working as expected.

To run just this group use the command 'mvn test -Dgroups=Integration-Test'

## 5. TODOs
 - When meals are confirmed on a list their Rotation is switched to out.
 - When there are not enough meals to generate the list update the existing rotations of out to in before throwing error
 - Update the shopping list so that it comes back in a format that can be imported by MS to do list
 - Keep Readme up to date
 - Use ingredient type in Ingredient for metric checkers on shopping list to make sure we don't get 10g red pepper, 
   1 whole red pepper etc
 - Add delete functionality to controllers
 - Add versioning and auth to the restful requests
 - Finish Integration tests
 - Default weekly breakfast recipe
