 # Introduction

This is a poorly written application, and we're expecting the candidate to greatly improve this code base.

## Instructions
* **Consider this to be your project! Feel free to make any changes**
* There are several deliberate design, code quality and test issues in the current code, they should be identified and resolved
* Implement the "New Requirements" below
* Keep it mind that code quality is very important
* Focus on testing, and feel free to bring in any testing strategies/frameworks you'd like to implement
* You're welcome to spend as much time as you like, however, we're expecting that this should take no more than 2 hours

## `movie-theater`

### Current Features
* Customer can make a reservation for the movie
  * And, system can calculate the ticket fee for customer's reservation
* Theater have a following discount rules
  * 20% discount for the special movie
  * $3 discount for the movie showing 1st of the day
  * $2 discount for the movie showing 2nd of the day
* System can display movie schedule with simple text format

## New Requirements
* New discount rules; In addition to current rules
  * Any movies showing starting between 11AM ~ 4pm, you'll get 25% discount
  * Any movies showing on 7th, you'll get 1$ discount
  * The discount amount applied only one if met multiple rules; biggest amount one
* We want to print the movie schedule with simple text & json format

## Things added/changed -

* Converted application into spring boot based microservice
* Added rest controller, add two GET endpoints - one to reserve ticket with all applicable discounts and another to print schedule in text/json. This in future will make it more extensible.
* Moved/added all the business logic to new service MovieTheaterService. Additional logic was added to achieve new requirements.
* Added custom exception and exception handler to notify user of any exceptions that occur in backend
* Updated all the object classes to have private members and getter/setters
* Did code cleanup. For e.g removed Theater class
* Added logging to debug in case of any issues
* Added Mockito framework to do testing using mock objects
* Additional tests were added to test new functionality

## How to run - 

* Start the spring boot application by running MovieTheaterApplication class
* Once started, access rest endpoints using below links -
  - To reserve ticket - Parameters are (UserId/UserName/SequenceNumber/NumberOfTickets) 
  1) Sample call - http://localhost:8080/reserve/1/Jane/2/2   
    Positive case output - Total Price of the movie is 40.0
  2) Sample call - http://localhost:8080/reserve/1/Jane/10/2
    Negative case output - Movie sequence number not found
  - To print movie schedule - (Type - it can be text or json)
  1) Sample call - http://localhost:8080/printSchedule/text 
  2) Sample call - http://localhost:8080/printSchedule/csv 
    Negative case output - Type not supported. Use Text or Json

## Additional things to consider 

* Swagger ui can be added to make rest calls more presentable and easy to use
* Some intelligent feature like when user types incorrect sequence, a schedule pops up to help pick the right sequence number