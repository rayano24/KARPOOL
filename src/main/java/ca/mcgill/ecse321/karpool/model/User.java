package ca.mcgill.ecse321.karpool.model;
import javax.persistence.Column;
import javax.persistence.Entity;

import ca.mcgill.ecse321.karpool.application.Rating;


import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "USERS")
public class User {
	@Id
	@Column(name = "NAME")
	private String name;

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return this.name;
	}

	@Id
	@Column(name = "EMAIL")
	private String email;

	public void setEmail(String value) {
		this.email = value;
	}


	public String getEmail() {
		return this.email;
	}

	@Id
	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	public void setPhoneNumber(String value) {
		this.phoneNumber = value;
	}


	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@Id
	@Column(name = "PASSWORD")
	private String password;

	public void setPassword(String value) {
		this.password = value;
	}


	public String getPassword() {
		return this.password;
	}

	@Id
	@Column(name = "RATING")
	private Rating rating;


	public void setRating(Rating value) {
		this.rating = value;
	}


	public Rating getRating() {
		return this.rating;
	}

	@Id
	@Column(name = "RECORD")
	private boolean criminalRecord;


	public boolean getRecord() {
		return this.criminalRecord;
	}

	public void setRecord(boolean criminalRecord) {
		this.criminalRecord = criminalRecord;
	}

}
