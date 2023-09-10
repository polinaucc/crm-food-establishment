# crm-food-establishment

### PET Project of Java Kindergarten

The project is a CRM (customer relationship management) system for food establishments that allows restaurant owners and employees to solve key business problems they face in their daily work in a timely manner:
    
* Analysis of the company’s profitability

* Analysis of products available in warehouses

* Ordering products from suppliers

* Process online table reservations and takeaway orders

* Process the work schedule for each employee.

* Work statistics for each employee

* Preparing and invoicing (bills)

* Track feedback on service, food, prices.


The project architecture is based on microservices approach. It follows 'DB per the service' approach.
Communication between system components (services) are sync, REST is used.

[Full description of the project](https://docs.google.com/document/d/1ukXwbbVWVgMsnx_iHVCGbTTp5z1j9K2WF8orbK0ez7E/edit)

Model View Presenter Architecture. Is located here: 

[Architecture](https://docs.google.com/document/d/1wnfXWRxNdMSoB173JtJNFRSd3XVxfZph8C1fQOcTKiY/edit)

Product requirements (Functional and Non Functional) are located here:

[Functional](https://docs.google.com/document/d/1ukXwbbVWVgMsnx_iHVCGbTTp5z1j9K2WF8orbK0ez7E/edit)

[Non Functional](https://docs.google.com/document/d/1bnIqMXma340uexmaUAD9UcVyvBcUUq_fvoodJ5vfYnw/edit)

To running the service early, you need to take some steps:
1. Creating a project: you need to use Maven clean install to create a project. You can use the mvn archetype:generate command, which allows you to select a project archetype. Version http://maven.apache.org/POM/4.0.0

2. Eureka Setup: Use Eureka to register and configure it. Version 0.0.1-SNAPSHOT . Required parameters for configuration:

spring.application.name=eureka-server
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
3. Spring Boot version: We called org.springframework.boot. Version 3.1.2

4. MySQL database:


