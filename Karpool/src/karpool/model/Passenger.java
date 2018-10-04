package karpool.model;

import javax.persistence.Entity;

@Entity
public class Passenger extends UserRole{
   private int relationshipStatus;
   
   public void setRelationshipStatus(int value) {
    this.relationshipStatus = value;
    }
public int getRelationshipStatus() {
    return this.relationshipStatus;
       }
   }
