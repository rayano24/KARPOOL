package ca.mcgill.ecse321.karpool.application;

import javax.persistence.Entity;
import ca.mcgill.ecse321.karpool.model.UserRole;
import ca.mcgill.ecse321.karpool.model.Car;
import javax.persistence.OneToOne;
import ca.mcgill.ecse321.karpool.model.Trip;
import javax.persistence.ManyToOne;

@Entity
public class Driver extends UserRole{
   private Car car;
   
   @OneToOne(optional=false)
   public Car getCar() {
      return this.car;
   }
   
   public void setCar(Car car) {
      this.car = car;
   }
   
   private Trip trip;
   
   @ManyToOne(optional=false)
   public Trip getTrip() {
      return this.trip;
   }
   
   public void setTrip(Trip trip) {
      this.trip = trip;
   }
   
   }
