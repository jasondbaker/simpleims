<h1>SimpleIMS</h1>
**An Open Source Incident Management System built on the Java Play framework.**

## OVERVIEW

SimpleIMS is a Business to Business (B2B) solution for documenting and managing service 
requests. IMS’s are used in many businesses to handle requests by customers and to improve the efficiency of 
customer service organizations. 

A company representative uses an IMS to document, prioritize, and assign a customer service issue to a staff 
member who can manage the customer request. For example, a customer might call a software company to report that 
the software application running on her desktop computer is displaying an error message. A customer service 
representative from the software company would use the IMS to document and prioritize the problem. Additionally, 
other company support staff members would perform actions to resolve the customer support issue. 

An IMS also typically provides reports showing the number of incidents handled each month, customer representative
performance, and general incident trends. Companies can use these metrics to improve customer satisfaction and better understand their resource 
needs.

## INSTALLATION

Requirements:

- Play 2 framework: http://www.playframework.com
- Java IDE (Eclipse, etc)

Installation steps:
1. Create new Play IMS project: *play new ims*
2. If using Eclipse, install the project files using: *play eclipse*
3. Install git in the ims directory: *git init*
4. Issue a pull request from the project repository to update the files.

## ARCHITECTURE

The SimpleIMS system includes both a RESTful API and a Javascript-based client application. The API and the client
application both used separate model-view-controller (MVC) frameworks. The API is built around the [Java Play 2
framework](http://www.playframework.com) and the client application is built around the [AngularJS framework](http://www.angularjs.org). The API currently uses the Java
[H2 database engine](http://www.h2database.com) for data persistence -- although this database could easily be switched out for any relational database
supported by the [EBean ORM](http://www.avaje.org/). 

A RESTful API was developed to facilitate data transfer, in JSON format, between the client application and the 
core IMS system.  JSON was chosen because it is a lightweight object serialization format, and it works well with both 
Java and Javascript-based application frameworks. REST represents a modern approach to designing pragmatic web 
API’s where application endpoints are defined by objects acting as nouns and called by HTTP methods acting as 
verbs. By supporting dozens of useful API endpoints, the system provides an API vocabulary that allows a wide 
variety of client applications to communicate with the system. 

Interestingly, both the presentation and domain logic application tiers utilize separate MVC frameworks. In the 
case of the domain logic tier (API), the system is primarily using the Model and Controller portions of the framework. 
The View capabilities of the Play 2 framework are only used to bootstrap the AngularJS application into the 
user’s web browser. This architecture reinforces a clear separation of concerns. The Play 2 framework is 
focused on supporting the API, whereas the AngularJS framework supports the client web interface. 

SimpleIMS can support any kind of client interface as long as it can communicate with the RESTful API and
understand [JSON](http://www.json.org). The existing AngularJS-based client interface represents a prototype of what a client
interface could look like. A client interface could just as easily have been built on a mobile platform. In fact,
that's a good idea for a future project.

## API

**AGENTS**

METHOD | ENDPOINT | ACTION
------- | ---------------- | -------------------------
GET	| /agent | Get currently logged in agent	
GET	| /agents | Get complete list of active agents	
GET	| /agents/{username} | Get information for specific agent	
GET	| /agents/{username}/incidents | Get incidents associated with agent	
GET	| /logout | Logout agent	
POST | /login | Login agent	
			
**COMPANIES**

METHOD | ENDPOINT | ACTION
------- | ---------------- | -------------------------
GET	| /companies[?search=<name>] | Get complete list of active companies	
GET	| /companies/{id} | Get information for specific company	
GET	| /companies/{id}/incidents	| Get list of incidents associated with a company	
GET	| /companies/{id}/contacts | Get list of contacts associated with a company	
GET	| /companies/{id}/addresses | Get list of addresses associated with a company	
POST | /companies | Create a new company	
POST | /companies/{id} | Update an existing company	
POST | /companies/{id}/contacts	| Add a contact to an existing company	
POST | /companies/{id}/addresses/{id2} | Add an address to an existing company	
DELETE	| /companies/{id} | Delete an existing company	
			
**CONTACTS**

METHOD | ENDPOINT | ACTION
------- | ---------------- | -------------------------
GET	| /contacts[?search=<fullname>]	| Get complete list of active contacts	
GET	| /contacts/{id} | Get information for a specific contact	
GET	| /contacts/{id}/incidents | Get list of incidents associated with a contact	
GET	| /contacts/{id}/companies | Get company associated with a contact	
POST | /contacts/{id} | Update an existing contact	
DELETE | /contacts/{id} | Delete an existing contact	
			
**INCIDENTS**

METHOD | ENDPOINT | ACTION
------- | ---------------- | -------------------------
GET | /incidents[?status=<status>] | Get a list of incidents associated with current Agent	
GET	| /incidents/{id} | Get information for a specific incident	
GET	| /incidents/{id}/actions | Get list of actions associated with a specific incident	
POST | /incidents/{id}/actions | Add an action to an existing incident	
POST | /incidents/{id}/reopen | Reopen a closed incident	
POST | /incidents/{id}/close | Close an existing incident	
POST | /incidents | Create a new incident	
POST | /incidents/{id}	| Update an existing incident	
DELETE | /incidents/{id} | Delete an existing incident	
			
**CATEGORIES**

METHOD | ENDPOINT | ACTION
------- | ---------------- | -------------------------
GET	| /categories | Get complete list of categories	
GET	| /categories/{id} | Get information for a specific category	

## HISTORY

SimpleIMS was started by Jason Baker in the spring of 2014 as part of a software engineering graduate class
(SEIS 626 Software Analysis and Design) offered at the University of St. Thomas. Baker used his prior experience
within the telcom and webhosting industries to design the basic models and architecture for the application.

## TODO

1. Better error handling needs to be implemented throughout the application.
2. UI needs to support pagination.
3. Email notifications and alerts.
4. Customer accounts and interface for generating and reviewing requests.
5. OAuth and SSO would be nice.
6. More report types and options.
7. Admin portal and related functionality.
8. User password reset

## LICENSE

The MIT License (MIT)

Copyright (c) 2014 Jason Baker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.