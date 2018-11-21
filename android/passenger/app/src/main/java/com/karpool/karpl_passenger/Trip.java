package com.karpool.karpl_passenger;

public class Trip {

    private String origin, destination, date, time, driver, seats, tripID, price, driverNumber;

    public Trip() {
    }

    public Trip(String origin, String destination, String date, String time, String driver, String driverNumber, String seats, String price, String tripID) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.driver = driver;
        this.price = price;
        this.tripID = tripID;
        this.driverNumber = driverNumber;
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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String number) {
        this.driverNumber = number;
    }



}


