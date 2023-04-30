package com.jpmc.theater.service;

import com.jpmc.theater.Customer;
import com.jpmc.theater.Movie;
import com.jpmc.theater.Reservation;
import com.jpmc.theater.Showing;
import com.jpmc.theater.exception.MovieSequenceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MovieTheaterServiceTest {

    @InjectMocks
    private MovieTheaterService movieTheaterService;

    @Test
    void movieWithSpecialDiscountCode() {
        Movie movie = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 1);
        Showing showing = new Showing(movie, 5, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        assertEquals(10, movieTheaterService.calculateTicketPrice(showing, 1));
    }

    //Any movie showing starting between 11AM ~ 4pm, 25% discount will be applied
    @Test
    void movieWith25PercentDiscount() {
        Movie movie = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 1);
        Showing showing = new Showing(movie, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)));
        assertEquals(9.375, movieTheaterService.calculateTicketPrice(showing,1));
        assertEquals(46.875, movieTheaterService.calculateTicketPrice(showing,5));
    }

    @Test
    void movieRunningOn7thDay() {
        Movie movie = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 0);
        Showing showing = new Showing(movie, 5, LocalDateTime.of(LocalDate.of(2023, 05, 07), LocalTime.of(10, 0)));
        assertEquals(11.5, movieTheaterService.calculateTicketPrice(showing,1));
        assertEquals(61.5, movieTheaterService.calculateTicketPrice(showing,5));
    }

    //Two discounts are applicable here - one with special code and another one 25% as movie time is between 11-4. The bigger discount of 25% is applied
    @Test
    void movieWithBiggestPercentDiscount() {
        Movie movie = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 1);
        Showing showing = new Showing(movie, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)));
        assertEquals(9.375, movieTheaterService.calculateTicketPrice(showing,1));
        assertEquals(46.875, movieTheaterService.calculateTicketPrice(showing,5));
    }

    @Test
    void movieWithNoDiscount() {
        Movie movie = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 0);
        Showing showing = new Showing(movie, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        assertEquals(12.5, movieTheaterService.calculateTicketPrice(showing,1));
        assertEquals(62.5, movieTheaterService.calculateTicketPrice(showing,5));
    }

    @Test
    void totalTicketPriceOnReservation() {
        Customer customer = new Customer(1, "John Doe");
        assertTrue(movieTheaterService.reserve(customer, 1, 3) == 30.0);
    }

    @Test
    void reserveNonExistingMovieShowing() {
        Customer customer = new Customer(1, "John Doe");
        Exception exception = assertThrows(MovieSequenceNotFoundException.class, () -> {
            movieTheaterService.reserve(customer, 10, 3);
        });
    }
}
