package karpool.model;

import string;

public class Car {
   private string make;
   
   public void setMake(string value) {
      this.make = value;
   }
   
   public string getMake() {
      return this.make;
   }
   
   private int seat;
   
   public void setSeat(int value) {
      this.seat = value;
   }
   
   public int getSeat() {
      return this.seat;
   }
   
   private string liscensePlate;
   
   public void setLiscensePlate(string value) {
      this.liscensePlate = value;
   }
   
   public string getLiscensePlate() {
      return this.liscensePlate;
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
   
   public Driver getDriver() {
      return this.driver;
   }
   
   }
