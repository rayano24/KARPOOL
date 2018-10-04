package karpool.model;

import javax.persistence.Entity;
import enumeration;
import java.util.Set;
import javax.persistence.OneToMany;

@Entity
public class User{
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
private enumeration rating;

public void setRating(enumeration value) {
    this.rating = value;
    }
public enumeration getRating() {
    return this.rating;
    }
private String name;

public void setName(String value) {
    this.name = value;
    }
public String getName() {
    return this.name;
    }
private String email;

public void setEmail(String value) {
    this.email = value;
    }
public String getEmail() {
    return this.email;
    }
private String phone;

public void setPhone(String value) {
    this.phone = value;
    }
public String getPhone() {
    return this.phone;
    }
private Set<UserRole> userRole;

@OneToMany(mappedBy="user" )
public Set<UserRole> getUserRole() {
   return this.userRole;
}

public void setUserRole(Set<UserRole> userRoles) {
   this.userRole = userRoles;
}

private String location;

public void setLocation(String value) {
    this.location = value;
    }
public String getLocation() {
    return this.location;
       }
   }
