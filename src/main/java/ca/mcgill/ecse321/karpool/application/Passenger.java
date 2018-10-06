package ca.mcgill.ecse321.karpool.application;

import javax.persistence.Entity;
import ca.mcgill.ecse321.karpool.model.UserRole;
import ca.mcgill.ecse321.karpool.model.Trip;
import javax.persistence.ManyToOne;

@Entity
public class Passenger extends UserRole{
   private Trip trip;
   
   @ManyToOne(optional=false)
   public Trip getTrip() {
      return this.trip;
   }
   
   public void setTrip(Trip trip) {
      this.trip = trip;
   }
   
   }
