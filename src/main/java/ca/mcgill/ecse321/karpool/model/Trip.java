package ca.mcgill.ecse321.karpool.model;

import ca.mcgill.ecse321.karpool.application.Driver;
import java.util.Set;
import java.util.HashSet;
import ca.mcgill.ecse321.karpool.application.Passenger;

public class Trip {
   private int seatAvailable;
   
   public void setSeatAvailable(int value) {
      this.seatAvailable = value;
   }
   
   public int getSeatAvailable() {
      return this.seatAvailable;
   }
   
   private String destination;
   
   public void setDestination(String value) {
      this.destination = value;
   }
   
   public String getDestination() {
      return this.destination;
   }
   
   private String departureTime;
   
   public void setDepartureTime(String value) {
      this.departureTime = value;
   }
   
   public String getDepartureTime() {
      return this.departureTime;
   }
   
   private String departureLocation;
   
   public void setDepartureLocation(String value) {
      this.departureLocation = value;
   }
   
   public String getDepartureLocation() {
      return this.departureLocation;
   }
   
   private int distance;
   
   public void setDistance(int value) {
      this.distance = value;
   }
   
   public int getDistance() {
      return this.distance;
   }
   
   /**
    * <pre>
    *           1..1     0..*
    * Trip ------------------------- Driver
    *           trip        &lt;       driver
    * </pre>
    */
   private Set<Driver> driver;
   
   public Set<Driver> getDriver() {
      if (this.driver == null) {
         this.driver = new HashSet<Driver>();
      }
      return this.driver;
   }
   
   /**
    * <pre>
    *           1..1     0..*
    * Trip ------------------------- Passenger
    *           trip        &lt;       passenger
    * </pre>
    */
   private Set<Passenger> passenger;
   
   public Set<Passenger> getPassenger() {
      if (this.passenger == null) {
         this.passenger = new HashSet<Passenger>();
      }
      return this.passenger;
   }
   
   }
