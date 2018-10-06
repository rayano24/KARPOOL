package ca.mcgill.ecse321.karpool.model;
import javax.persistence.Entity;

import ca.mcgill.ecse321.karpool.application.Driver;
import javax.persistence.Id;

import org.hibernate.annotations.Entity;

@Entity
public class Car {
   private String make;
   
   public void setMake(String value) {
      this.make = value;
   }
   
   @Id
   public String getMake() {
      return this.make;
   }
   
   private int seat;
   
   public void setSeat(int value) {
      this.seat = value;
   }
   
   @Id
   public int getSeat() {
      return this.seat;
   }
   
   private String licensePlate;
   
   public void setLicensePlate(String value) {
      this.licensePlate = value;
   }
   
   @Id
   public String getLicensePlate() {
      return this.licensePlate;
   }
   
   private String fuelEfficiency;
   
   public void setFuelEfficiency(String value) {
      this.fuelEfficiency = value;
   }
   
   @Id
   public String getFuelEfficiency() {
      return this.fuelEfficiency;
   }
   
   /**
    * <pre>
    *           1..1     1..1
    * Car ------------------------- Driver
    *           car        &gt;       driver
    * </pre>
    */
   private Driver driver;
   
   public void setDriver(Driver value) {
      this.driver = value;
   }
   
   @Id
   public Driver getDriver() {
      return this.driver;
   }
   
   }
