package com.jpmc.theater.controller;

import com.jpmc.theater.Customer;
import com.jpmc.theater.exception.MovieSequenceNotFoundException;
import com.jpmc.theater.exception.TypeNotSupportedException;
import com.jpmc.theater.service.MovieTheaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieTheaterController {

    @Autowired
    MovieTheaterService movieTheaterService;

    Logger log = LoggerFactory.getLogger(MovieTheaterController.class);

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    // Sample call - http://localhost:8080/reserve/1/Jane/2/2
    @GetMapping("/reserve/{id}/{name}/{sequence}/{numberOfTickets}")
    public ResponseEntity<Object> reserveTickets(@PathVariable int id , @PathVariable String name, @PathVariable int sequence, @PathVariable int numberOfTickets)  {
        double totalPrice;
        try{
            Customer c = new Customer(id, name);
            totalPrice = movieTheaterService.reserve(c, sequence, numberOfTickets);
            log.info("Reservation done successfully, total price is "+totalPrice +"$");
        } catch(Exception e) {
            throw new MovieSequenceNotFoundException("Not able to find any showing for given sequence");
        }
        return new ResponseEntity<>("Total Price of the movie is " + totalPrice, HttpStatus.OK);
    }

    //Sample call - http://localhost:8080/printSchedule/text
    @GetMapping(value="/printSchedule/{type}")
    public ResponseEntity<Object> printSchedule(@PathVariable String type) {
        String schedule;
        log.info("Printing movie schedule in " + type);
        if (type.equals("text")) {
            schedule = movieTheaterService.printSchedule();
        } else if(type.equals("json")) {
            schedule = movieTheaterService.printScheduleInJson();
        } else {
            throw new TypeNotSupportedException();
        }
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @ExceptionHandler(value = MovieSequenceNotFoundException.class)
    public ResponseEntity<Object> exception(MovieSequenceNotFoundException exception) {
        return new ResponseEntity<>("Movie sequence number not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TypeNotSupportedException.class)
    public ResponseEntity<Object> exception(TypeNotSupportedException exception) {
        return new ResponseEntity<>("Type not supported. Use Text or Json", HttpStatus.NOT_FOUND);
    }
}
