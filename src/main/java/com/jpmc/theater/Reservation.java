package com.jpmc.theater;

public class Reservation {
    private Customer customer;
    private Showing showing;
    private int audienceCount;

    public Reservation(com.jpmc.theater.Customer customer, com.jpmc.theater.Showing showing, int audienceCount) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Showing getShowing() {
        return showing;
    }

    public int getAudienceCount() {
        return audienceCount;
    }
}