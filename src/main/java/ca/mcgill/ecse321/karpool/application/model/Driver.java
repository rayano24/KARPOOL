package ca.mcgill.ecse321.karpool.application.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ca.mcgill.ecse321.karpool.application.model.Trip;

@Entity
@Table(name="driver")
public class Driver
{
	private Set<Trip> trips;

	public void setTrips(Set<Trip> trips) {
		this.trips = trips;
	}

	public void addTrip(Trip trip) {
		this.trips.add(trip);
		trip.setDriver(this);
	}
	
	public void removeTrip(Trip trip) {
        this.trips.remove(trip);
        trip.setDriver(null);
    }

	@Transient
	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
	public Set<Trip> getTrips() {
		if (this.trips == null) {
			this.trips = new HashSet<Trip>();
		}
		return this.trips;
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
	
	private List<Double> ratings;


	public void setRatings(List<Double> ratings) {
		this.ratings = ratings;
	}

	public void addRating(Double value) {
		this.ratings.add(value);
	}

	@ElementCollection(targetClass=Double.class)
	public List<Double> getRatings() {
		if (this.ratings == null) {
			this.ratings = new ArrayList<Double>();
		}
		return this.ratings;
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
