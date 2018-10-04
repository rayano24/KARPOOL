package karpool.model;
import null.enumeration;
import null.string;
import null.integer;

import integer;
import string;
import enumeration;
import java.util.Set;
import java.util.HashSet;

public class User {
   private integer id;
   
   public void setId(integer value) {
      this.id = value;
   }
   
   public integer getId() {
      return this.id;
   }
   
   private string password;
   
   public void setPassword(string value) {
      this.password = value;
   }
   
   public string getPassword() {
      return this.password;
   }
   
   private enumeration rating;
   
   public void setRating(enumeration value) {
      this.rating = value;
   }
   
   public enumeration getRating() {
      return this.rating;
   }
   
   private string name;
   
   public void setName(string value) {
      this.name = value;
   }
   
   public string getName() {
      return this.name;
   }
   
   private string email;
   
   public void setEmail(string value) {
      this.email = value;
   }
   
   public string getEmail() {
      return this.email;
   }
   
   private string phone;
   
   public void setPhone(string value) {
      this.phone = value;
   }
   
   public string getPhone() {
      return this.phone;
   }
   
   /**
    * <pre>
    *           1..1     0..2
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
   
   private string location;
   
   public void setLocation(string value) {
      this.location = value;
   }
   
   public string getLocation() {
      return this.location;
   }
   
   }
