package ca.mcgill.ecse321.karpool.application;

import javax.persistence.Entity;
import javax.persistence.Id;

import ca.mcgill.ecse321.karpool.model.UserRole;
import ca.mcgill.ecse321.karpool.model.Trip;

@Entity
public class Passenger extends UserRole{
   private Trip trip;

public void setTrip(Trip value) {
    this.trip = value;
}
@Id
public Trip getTrip() {
    return this.trip;
}
private int passengerId;

public void setPassengerId(int value) {
    this.passengerId = value;
}
@Id
public int getPassengerId() {
    return this.passengerId;
}
}
