package com.karpool.karpl_driver;

public class Trip {

    private String origin, destination, date, time;

    public Trip() {
    }

    public Trip(String origin, String destination, String date, String time) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.time = time;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String location) {
        this.origin = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String location) {
        this.destination = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String tripDate) {
        this.date = tripDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String tripTime) {
        this.time = tripTime;
    }
}

