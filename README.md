# Moo's Marvelous Meal Prep
#### Video Demo:  Will be added

# A Service to generate Meal's for the week

This is something I've been meaning to put together for awhile. Every week myself and my wife plan different
dinners Mon - Sun based of recipes we know we like

This service will pull from a library of Recipes and generate the meal prep for the week as well as return a
shopping list to be copied to the shopping list app of choice.

The service is very basic at present as it will just pull a list of meals that haven't been used in the current cycle and always show the latest "Meal prep"

It wil also allow us to download a shopping list in CSV as well as copy text to paste to Whatsapp (we have a group to track the weeks meals) 

If you examine the current meal's you are also able to open todays meal and use it to get the required ingredients and steps to cook.

### Contents

#### 1. Installing the service

#### 2. Running the service

#### 3. Design decisions

#### 4. Testing

#### 5. Front End

#### 6. TODOs

## 1. Installing the service

The Service requires connectivity access to a relational DB, I used postgres.
you will need to update your details in the application.properties.


````
spring.datasource.url=jdbc:postgresql://localhost:5432/mealprep
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.hikari.schema=meals
````

Once your DB is set up you should run the scripts in resources/extra/01-DBsetup.sql.
(I have also added a 02-DBDump.sql file to create some generic recipes for testing)


## 2. Running the service

The Project itself is Built using Maven and the Spring framework.leveraging Spring boot all you need to do
to get the service going is using your CLI client of choice navigate to the directory that the pom file lives in and
run '


'.

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
- The endpoints themselves are self explanatory by name and as to what they do and Stick to the rules behind each
  method (get retrieves, Post creates etc).
- All Endpoints catch a Custom Exception which all Services throw, more on that in Exception handling.

#### DTO's and Entities

- Using Lombok to help deal with most of the boilerplate code.
- Separate objects for data Transferring and DB processing.
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
  consistent and easy to manage should there be changes.
- Having all exceptions channel through our custom Exceptions makes it easier to have control on how and what is logged,
  sent to our logging application and or how we deal with errors (Kibana, Splunk, Email Notifications etc).

#### POJO's and Repositories

- Plain Java Objects.
- Spring JPA repositories, another part of the spring framework that just makes dev a little easier.

#### Service Layer

- Services are initially set as interfaces for ease of testing as well as sticking to the Open Close principle and
  polymorphism.
- Every Service implementation method should catch generic Exception and convert it to the Custom Restful Exception
  before sending back up the line to eventually be handled by the controller.
- I believe each repo layer should only be accessible by a single service this enables the services to maintain the
  single point of logic for its Entities via its DTO's.

#### Utils

- Every service's has some utility classes, the one I normally always put in but haven't as this app does not require
  much in the way of logging is a Logger Util, as much as I enjoy the Lombok simple logging annotations I like to have
  more control over what my logs are going to do based on codes put into the Exceptions etc.
- There is an ID converter in the App that just converts from Long to String and back again, normally this would
  hash/encrypt the ID's before they are in the top layer as that's important data that we wouldn't like malicious
  parties
  to easily own.
- The Metric Converter is on the "To do" list, as part of the shopping list we may need to convert cups to grams
  etc, It will probably move to the Meal prep service as I can't see it being required by any other functionality

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

## 5. Front End

- I was busy building an Angular front end while learning moe about angular, then changed jobs and started focusing on
  React while learning that, then found that the job is more Next.js as a frame wor on top of React, so decided to just
  go good old fashion html, Css and Js to get the app completed
- Once the app has started up the home page sits at [project root]/mealprep/src/main/resources/web/recipes.html

### Home
- Shopping list - button redirect to shopping list panel
- Plane for the Week - button redirect to weekly plan panel
- Recipes - button redirect to Recipes panel

### Shoping list
- Home - button redirect to home panel
- List of the current shopping list
- Download - downloads a CSV of the current shopping list

### Shopping list
- Home - button redirect to home panel
- List of the Planned meals with a view button to display the meals details including ingredients and steps
- WhatsApp - Displays a list of the meals to be copied to whats app 
- Prepare Weekly Meals -> Generate ss new list of weekly meals

### Recipes
- Home - button redirect to home panel
- List of all the recipes
- Add Recipe - Opens up a modal to create a new recipe
