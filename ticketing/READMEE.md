# Prerequisites

1. Java 8
2. Maven 3
3. Postgresql (if you run prod configuration)
4. SMTP Mail Server
5. npm
6. bower
7. gulp

# Prerequisites Installation for MacOS
It is possible to install prerequisites in MacOs using brew. 
brew is a package manager for MacOs just like apt for Ubuntu. 

If you don't have brew installed, install it using following command:

ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

Now, you can install prerequisites using following commands: 

- brew install caskroom/cask/brew-cask
- brew cask install java
- brew install postgresql
- brew install npm
- npm install -g yo
- npm install -g generator-jhipster
- npm install bower gulp

# Database Configuration
Following file contains, database connection properties:

src/main/resources/config/application-dev.yml

Database connection properties are located under "spring/datasource" section of application-dev.yml. 
The system is configured to H2 in-memory database in development mode. 

# Database Creation
The application uses H2 in-memory database. 
Because of this you don't have to take any actions to set up database.

# Database Initialization
You don't have to make any manual database initialization.
The application uses Liquibase to setup database schema. 
The application also contains initial data for following entities:

* Country
* City
* Airport
* Aiplane
* Airplane Seats
* Flight
* Flight Seats

# Database Client
The system provides a built-in H2 Console:
 * Login using "admin"/"admin".
 * Click Administration/Database

# Run the project
* Unzip file and issue following commands:

    cd ticketing
    
    mvn spring-boot:run

* Open your browser and go to http://localhost:8080

# Deploy to CloudFoundry
Follow following steps to deploy to CloudFoundry:

* Sign up from https://account.run.pivotal.io/sign-up
* Download cloudfoundry's cf command line interface from 
https://github.com/cloudfoundry/cli/releases

* Run following command to login to CloudFoundry:

    cf login -a https://api.run.pivotal.io
    
* Run following command to deploy to CloudFoundry:
yo jhipster:cloudfoundry

CloudFoundry starts an instance with 1GB. This is fairly low for the application.
If your application crashes during startup, try increasing memory to 2 GB.

# Social Login
You need to update src/main/resources/config/application.yml to enable Social Login.
You also have make configurations from providers' following web sites: 

## Google
Refer to https://developers.google.com/identity/sign-in/web/server-side-flow#step_1_create_a_client_id_and_client_secret
to enable Google login.

## Twitter
Refer to https://apps.twitter.com/app/
to enable Twitter login.

## Facebook
Refer to https://developers.facebook.com/docs/facebook-login/v2.2 
to enable Facebook login

# Social Login Test
The application is deployed to http://ticketing999.cfapps.io
and Google Sign is configured for testing purposes. 
You can login to the application using your Google account. 

# Developer's Guide
For each entity, you will need: 
 * A database table
 * A Liquibase change set
 * A JPA Entity
 * A Spring Data JPA Repository
 * A Spring MVC REST Controller, which has the basic CRUD operations
 * An AngularJS router, a controller and a service
 * An HTML view
 * Integration tests, to validate everything works as expected
 * Performance tests, to see if everything works smoothly

If you have several entities, you will likely want to have relationships between them. For this, you will need: 
 * A database foreign key
 * Specific JavaScript and HTML code for managing this relationship

The "entity" sub-generator will create all the necessary files, and provide a CRUD front-end for each entity. 
The sub generator can be invoked by running yo jhipster:entity <entityName> --[options]. 
Reference for those options can be found by typing yo jhipster:entity --help 

The application shows framework behaviour. 
If you want to develop highly customized views rather than CRUD screens, do not start from scratch:
 * For frontend development start by cloning src/main/webapp/app/entities/flight folder.
 * For backend development, start by cloning FlightRepository, FlightResource classes.
 
## Ehcache
Ehcache is disabled to decrease memory requirements of the application. 
Since CloudFoundry provides 2GB memory for free, it is important to decrease memory consumption.
It can be enabled when switched to paid plans.
You can enable Ehcache by updating com.ticketing.config.CacheConfiguration class
 
##AngularJS Coding Standards
Following style guide is applied during development of this project: 

https://github.com/johnpapa/angular-styleguide/blob/master/a1/README.md

## Initial Data
Liquibase is used to create initial data. 
Following migration file persists initial data.

src/main/resources/config/liquibase/changelog/20160907130000_added_data.xml

## Testing Framework
Cucumber is used as testing framework.

# Assumptions
* There is only 1 airplane model and thus 1 seating model. 
With this assumption, I got rid of developing a seating plan designer.
* The system lacks some form validations. It is assumed that,
the users of the system are smart enough to provide correct data to input forms.
* It is assumed that thread synchronization issues are not faced in this system. 
In other words same seat is never sold to multiple clients.

# Requirements Not Covered in the Project
* Send email to public user, upon ticket cancellation
* Send email to all public users of that flight, upon flight cancellation 
* Payment integration does not exist.
* Online checkin not implemented.
* Several validations