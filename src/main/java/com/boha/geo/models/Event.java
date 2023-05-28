package com.boha.geo.models;

import org.springframework.data.annotation.Id;

public class Event {
    @Id
    private String eventId;
    private String date;
    private CityPlace cityPlace;

    private User user;
    private double amount;
    private int rating;
    private long longDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getLongDate() {
        return longDate;
    }

    public void setLongDate(long longDate) {
        this.longDate = longDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CityPlace getCityPlace() {
        return cityPlace;
    }

    public void setCityPlace(CityPlace cityPlace) {
        this.cityPlace = cityPlace;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
