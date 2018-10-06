package ca.mcgill.ecse321.karpool.model;

import ca.mcgill.ecse321.karpool.application.Rating;

public class User {
   private String name;
   
   public void setName(String value) {
      this.name = value;
   }
   
   public String getName() {
      return this.name;
   }
   
   private int id;
   
   public void setId(int value) {
      this.id = value;
   }
   
   public int getId() {
      return this.id;
   }
   
   private String email;
   
   public void setEmail(String value) {
      this.email = value;
   }
   
   public String getEmail() {
      return this.email;
   }
   
   private String phoneNumber;
   
   public void setPhoneNumber(String value) {
      this.phoneNumber = value;
   }
   
   public String getPhoneNumber() {
      return this.phoneNumber;
   }
   
   private String password;
   
   public void setPassword(String value) {
      this.password = value;
   }
   
   public String getPassword() {
      return this.password;
   }
   
   private Rating rating;
   
   public void setRating(Rating value) {
      this.rating = value;
   }
   
   public Rating getRating() {
      return this.rating;
   }
   
   }
