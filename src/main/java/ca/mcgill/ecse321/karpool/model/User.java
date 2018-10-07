package ca.mcgill.ecse321.karpool.model;
import javax.persistence.Entity;

import ca.mcgill.ecse321.karpool.application.Rating;


import javax.persistence.Id;


@Entity
public class User {
   private String name;
   
   public void setName(String value) {
      this.name = value;
   }
   @Id
   public String getName() {
      return this.name;
   }
   
   private String email;
   
   public void setEmail(String value) {
      this.email = value;
   }
   
   @Id
   public String getEmail() {
      return this.email;
   }
   
   private String phoneNumber;
   
   public void setPhoneNumber(String value) {
      this.phoneNumber = value;
   }
   
   @Id
   public String getPhoneNumber() {
      return this.phoneNumber;
   }
   
   private String password;
   
   public void setPassword(String value) {
      this.password = value;
   }
   
   @Id
   public String getPassword() {
      return this.password;
   }
   
   private Rating rating;

   
   public void setRating(Rating value) {
      this.rating = value;
   }
   
   @Id
   public Rating getRating() {
      return this.rating;
   }
   
   
   private boolean criminalRecord;
   @Id
   public boolean getBackgroundCheck() {
	   return this.criminalRecord;
   }
   
   public void setRecord(boolean criminalRecord) {
	   this.criminalRecord = criminalRecord;
   }
   
   }
