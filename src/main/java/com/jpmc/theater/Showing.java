package com.jpmc.theater;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Showing {
    private Movie movie;
    private int sequenceOfTheDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime showStartTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime showEndTime;

    public Showing(Movie movie, int sequenceOfTheDay, LocalDateTime showStartTime) {
        this.movie = movie;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.showStartTime = showStartTime;
        this.showEndTime = calculateShowEndTime();
    }

    //End time is calculate by adding the movie run time to show start time
    private LocalDateTime calculateShowEndTime() {
        return showStartTime.plus(movie.getRunningTime());
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getShowStartTime() {
        return showStartTime;
    }

    public int getSequenceOfTheDay() {
        return sequenceOfTheDay;
    }

    public LocalDateTime getShowEndTime() {
        return showEndTime;
    }
}
