package ca.mcgill.ecse321.karpool.application.model;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="end_user")
public class EndUser {
	
	private String name;

	public void setName(String value) {
		this.name = value;
	}

	@Id
//	@Column(name="name")
	public String getName() {
		return this.name;
	}

	private String email;

	public void setEmail(String value) {
		this.email = value;
	}

//	@Column(name="email")
	public String getEmail() {
		return this.email;
	}

	private String phoneNumber;

	public void setPhoneNumber(String value) {
		this.phoneNumber = value;
	}

//	@Column(name="phone_number")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	private String password;

	public void setPassword(String value) {
		this.password = value;
	}

//	@Column(name="passord")
	public String getPassword() {
		return this.password;
	}
	
	private Rating rating;


	public void setRating(Rating value) {
		this.rating = value;
	}

//	@Column(name="rating")
	public Rating getRating() {
		return this.rating;
	}

	private boolean criminalRecord;

//	@Column(name="record")
	public boolean getRecord() {
		return this.criminalRecord;
	}

	public void setRecord(boolean criminalRecord) {
		this.criminalRecord = criminalRecord;
	}
}
