package t14.karpool;

import java.util.Set;
import java.util.HashSet;

public class Car {
   /**
    * <pre>
    *           1..1     1..*
    * Car ------------------------- Driver
    *           car        &gt;       driver
    * </pre>
    */
   private Set<Driver> driver;
   
   public Set<Driver> getDriver() {
      if (this.driver == null) {
         this.driver = new HashSet<Driver>();
      }
      return this.driver;
   }
   
   private String make;
   
   public void setMake(String value) {
      this.make = value;
   }
   
   public String getMake() {
      return this.make;
   }
   
   private int seat;
   
   public void setSeat(int value) {
      this.seat = value;
   }
   
   public int getSeat() {
      return this.seat;
   }
   
   private String licensePlate;
   
   public void setLicensePlate(String value) {
      this.licensePlate = value;
   }
   
   public String getLicensePlate() {
      return this.licensePlate;
   }
   
   }
