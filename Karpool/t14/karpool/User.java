package t14.karpool;
import null.enum;

import enum;
import java.util.Set;
import java.util.HashSet;

public class User {
   private int id;
   
   public void setId(int value) {
      this.id = value;
   }
   
   public int getId() {
      return this.id;
   }
   
   private String password;
   
   public void setPassword(String value) {
      this.password = value;
   }
   
   public String getPassword() {
      return this.password;
   }
   
   private enum rating;
   
   public void setRating(enum value) {
      this.rating = value;
   }
   
   public enum getRating() {
      return this.rating;
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
   
   /**
    * <pre>
    *           1..1     1..2
    * User ------------------------- UserRole
    *           user        &gt;       userRole
    * </pre>
    */
   private Set<UserRole> userRole;
   
   public Set<UserRole> getUserRole() {
      if (this.userRole == null) {
         this.userRole = new HashSet<UserRole>();
      }
      return this.userRole;
   }
   
   }
