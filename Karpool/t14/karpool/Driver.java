package t14.karpool;
import null.List;

import List;

public class Driver extends UserRole {
   private List citiesWillingToStop;
   
   public void setCitiesWillingToStop(List value) {
      this.citiesWillingToStop = value;
   }
   
   public List getCitiesWillingToStop() {
      return this.citiesWillingToStop;
   }
   
   /**
    * <pre>
    *           1..*     1..1
    * Driver ------------------------- Car
    *           driver        &lt;       car
    * </pre>
    */
   private Car car;
   
   public void setCar(Car value) {
      this.car = value;
   }
   
   public Car getCar() {
      return this.car;
   }
   
   }
