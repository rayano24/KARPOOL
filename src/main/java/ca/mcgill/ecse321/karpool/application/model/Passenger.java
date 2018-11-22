package ca.mcgill.ecse321.karpool.application.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.mcgill.ecse321.karpool.application.model.Trip;

@Entity
@Table(name="passenger")
public class Passenger
{
	private Set<Trip> trips;

	public void setTrips(Set<Trip> trips) {
		this.trips = trips;
	}

	public void addTrip(Trip value) {
		this.trips.add(value);
	}
	
	@ManyToMany(mappedBy = "passenger")
	@JsonBackReference
	public Set<Trip> getTrips() {
		if (this.trips == null) {
			this.trips = new HashSet<Trip>();
		}
		return this.trips;
	}
	
	public boolean removeTrip(Trip value ) {
		if(this.trips.contains(value))
		{
			this.trips.remove(value);
			return true;
		}
		return false;
	}
	
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

	private boolean criminalRecord;

	public boolean getRecord() {
		return this.criminalRecord;
	}

	public void setRecord(boolean criminalRecord) {
		this.criminalRecord = criminalRecord;
	}
	
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
