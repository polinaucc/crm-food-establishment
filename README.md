# CRM for Food Establishments

CRM is a pet project for Java Kindergarten initiative that is directed to help people to get to know processes
in a real team, to improve their technical and soft skills, learn new technologies.

It is a standalone project that may be used OOTB or in extended way by cafe, restaurants and other food establishments. 
This system is adapted for 1 specific food establishment and might be integrated by a customer's existed system.

This system can be used both by employees of food establishment and by its clients. There are 3 roles supported OOTB:
* ADMIN
* EMPLOYEE
* CLIENT

System contains the following functionality (**NOTE**: This list is now in progress and will be extensible):
* menu processing
* order processing
* tracking of online payments

More information regarding functional requirements can be found in 'Documentation' section

## Architecture and technical info

Project consists of 3 domain microservices:
* user-manager service (contains info about users, permissions)
* core service (used for processing of orders, menus)
* payment service (used for payments and bills)

Also there are gateway and registry services. The whole scheme of service-to-service communication can be found 
in 'Documentation' section.

All services are based on Java 21, Spring 6 and Hibernate 6.
We are supporting PostgreSQL DB. For DB migration Liquibase is used.

## Project Requirements

Java, Maven should be installed. PostgreSQL should be started either in Docker or explicitly.

## How to build

Each of the services should be built separately. To build one of the services the following command should be used:

`mvn clean install`

## How to run

To run the whole project locally, all services mentioned in a previous section should be run. Detailed steps how to run
each of them can be found in their own README file.

## Documentation

1. Product documentation: https://docs.google.com/document/d/1ukXwbbVWVgMsnx_iHVCGbTTp5z1j9K2WF8orbK0ez7E/edit?usp=sharing
2. Technical documentation: https://docs.google.com/document/d/1wnfXWRxNdMSoB173JtJNFRSd3XVxfZph8C1fQOcTKiY/edit?usp=sharing
3. Trello board: https://trello.com/b/Njf49mwz/javakingergartencrcrm