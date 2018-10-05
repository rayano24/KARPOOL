package t14.karpool;


public abstract class UserRole {
   /**
    * <pre>
    *           1..2     1..1
    * UserRole ------------------------- User
    *           userRole        &lt;       user
    * </pre>
    */
   private User user;
   
   public void setUser(User value) {
      this.user = value;
   }
   
   public User getUser() {
      return this.user;
   }
   
   }
