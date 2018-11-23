package com.karpool.karpl_driver;

/**
 * Constructor for the passenger class used in the passenger recycler view
 */
public class Passenger {

    private String name, number;

    public Passenger() {
    }

    public Passenger(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}


