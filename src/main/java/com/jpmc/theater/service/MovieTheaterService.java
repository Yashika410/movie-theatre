package com.jpmc.theater.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.theater.Customer;
import com.jpmc.theater.Movie;
import com.jpmc.theater.Reservation;
import com.jpmc.theater.Showing;
import com.jpmc.theater.exception.MovieSequenceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MovieTheaterService {

    private static int MOVIE_CODE_SPECIAL = 1;
    private List<Showing> schedule = populateShowingList(LocalDate.now());
    Logger log = LoggerFactory.getLogger(MovieTheaterService.class);

    public double reserve(Customer customer, int sequence, int howManyTickets) {
        Showing showing;
        try {
            showing = schedule.get(sequence - 1);
            log.info("Reserving movie " + showing.getMovie().getTitle()+ " for " +howManyTickets + " people");
            Reservation reservation = new Reservation(customer, showing, howManyTickets);
            return calculateTicketPrice(reservation.getShowing(), howManyTickets);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new MovieSequenceNotFoundException("Not able to find any showing for given sequence " + sequence);
        }
    }

    public String printSchedule() {
        StringBuilder sb = new StringBuilder();
        sb.append("===================================================");
        schedule.forEach(s ->
                sb.append(s.getSequenceOfTheDay() + ": " + s.getShowStartTime() + " " + s.getMovie().getTitle() + " " + humanReadableFormat(s.getMovie().getRunningTime()) + " $" + s.getMovie().getTicketPrice())
        );
        sb.append("===================================================");
        return sb.toString();
    }

    public String printScheduleInJson() {
        try {
            StringBuilder sb = new StringBuilder();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            sb.append(objectMapper.writeValueAsString(schedule));
            return sb.toString();
        } catch (JsonProcessingException jse) {
            jse.printStackTrace();
        }
        return null;
    }

    private String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());
        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(long value) {
        if (value == 1) return "";
        else return "s";
    }

    private List<Showing> populateShowingList(LocalDate date) {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), 11, 0);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), 9, 0);
        return new ArrayList<>(Arrays.asList(
                new Showing(turningRed, 1, LocalDateTime.of(date, LocalTime.of(9, 0))),
                new Showing(spiderMan, 2, LocalDateTime.of(date, LocalTime.of(11, 0))),
                new Showing(theBatMan, 3, LocalDateTime.of(date, LocalTime.of(12, 50))),
                new Showing(turningRed, 4, LocalDateTime.of(date, LocalTime.of(14, 30))),
                new Showing(spiderMan, 5, LocalDateTime.of(date, LocalTime.of(16, 10))),
                new Showing(theBatMan, 6, LocalDateTime.of(date, LocalTime.of(17, 50))),
                new Showing(turningRed, 7, LocalDateTime.of(date, LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(date, LocalTime.of(21, 10))),
                new Showing(theBatMan, 9, LocalDateTime.of(date, LocalTime.of(23, 0)))
        ));
    }

    public double calculateTicketPrice(Showing showing, int audienceCount) {
        //calculate the ticket price of the movie based on the audience count
        double totalTicketPrice = showing.getMovie().getTicketPrice() * audienceCount;
        return totalTicketPrice - getDiscount(showing, totalTicketPrice);
    }

    private double getDiscount(Showing showing, double totalTicketPrice) {
        double maxDiscount;
        double specialDiscount = 0.0;
        if (MOVIE_CODE_SPECIAL == showing.getMovie().getSpecialCode()) {
            specialDiscount = totalTicketPrice * 0.2;  // 20% discount for special movie
        }
        double sequenceDiscount = 0.0;
        if (showing.getSequenceOfTheDay() == 1) {
            sequenceDiscount = 3; // $3 discount for 1st show
        } else if (showing.getSequenceOfTheDay() == 2) {
            sequenceDiscount = 2; // $2 discount for 2nd show
        }
        double additionalDiscount = 0.0;
        // 25% discount for movies screening between 11-4 pm
        ChronoLocalDateTime discountStartTime = LocalDateTime.of(showing.getShowStartTime().toLocalDate(), LocalTime.of(11, 0));
        ChronoLocalDateTime discountEndTime = LocalDateTime.of(showing.getShowStartTime().toLocalDate(), LocalTime.of(16, 0));
        if(showing.getShowStartTime().isAfter(discountStartTime) && showing.getShowEndTime().isBefore(discountEndTime)) {
            additionalDiscount = totalTicketPrice * 0.25;
        }
        // 1$ discount for movies screening on 7th of every month
        if(showing.getShowStartTime().getDayOfMonth() == 7) {
            additionalDiscount = Math.max(additionalDiscount, 1);
        }
        // biggest discount wins
        maxDiscount = Math.max(Math.max(specialDiscount, sequenceDiscount), additionalDiscount);
        log.info("Discount applied " + maxDiscount + "$");
        return maxDiscount;
    }
}
