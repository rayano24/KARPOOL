package karpool.model;
import null.arrayList;

import arrayList;

public class Driver extends UserRole {
   private arrayList citiesWillingToStop;
   
   public void setCitiesWillingToStop(arrayList value) {
      this.citiesWillingToStop = value;
   }
   
   public arrayList getCitiesWillingToStop() {
      return this.citiesWillingToStop;
   }
   
   /**
    * <pre>
    *           1..1     1..1
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
