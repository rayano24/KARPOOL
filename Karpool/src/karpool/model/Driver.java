package karpool.model;

import javax.persistence.Entity;
import ArrayList;
import javax.persistence.OneToOne;

@Entity
public class Driver extends UserRole{
   private ArrayList citiesWillingToStop;
   
   public void setCitiesWillingToStop(ArrayList value) {
    this.citiesWillingToStop = value;
    }
public ArrayList getCitiesWillingToStop() {
    return this.citiesWillingToStop;
       }
   private Car car;
   
   @OneToOne(optional=false)
   public Car getCar() {
      return this.car;
   }
   
   public void setCar(Car car) {
      this.car = car;
   }
   
   }
