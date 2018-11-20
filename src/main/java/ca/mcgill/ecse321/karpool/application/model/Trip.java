package ca.mcgill.ecse321.karpool.application.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.Set;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;


@Entity
@Table(name="trip")
public class Trip 
{
	private boolean tripComplete;

	public boolean isTripComplete() {
		return tripComplete;
	}

	public void setTripComplete(boolean tripComplete) {
		this.tripComplete = tripComplete;
	}

	private int tripId;

	public void setTripId(int value) {
		this.tripId = value;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public int getTripId() {
		return this.tripId;
	}

	private int seatAvailable;

	private int price;

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return this.price;
	}

	public void setSeatAvailable(int value) {
		this.seatAvailable = value;
	}


	public int getSeatAvailable() {
		return this.seatAvailable;
	}

	private String destination;

	public void setDestination(String value) {
		this.destination = value;
	}

	public String getDestination() {
		return this.destination;
	}

	private String departureTime;

	public void setDepartureTime(String value) {
		this.departureTime = value;
	}

	public String getDepartureTime() {
		return this.departureTime;
	}

	private String departureDate;

	public void setDepartureDate(String value) {
		this.departureDate = value;
	}

	public String getDepartureDate() {
		return this.departureDate;
	}

	private String departureLocation;

	public void setDepartureLocation(String value) {
		this.departureLocation = value;
	}

	public String getDepartureLocation() {
		return this.departureLocation;
	}

	private int distance;

	public void setDistance(int value) {
		this.distance = value;
	}

	public int getDistance() {
		return this.distance;
	}

	private Driver driver;

	public void setDriver(Driver value) 
	{
		this.driver = value;
	}

	@ManyToOne
	@JoinColumn(name = "driver")
	public Driver getDriver() {
		return this.driver;
	}

	/**
	 * <pre>
	 *           1..1     0..*
	 * Trip ------------------------- Passenger
	 *           trip        &lt;       passenger
	 * </pre>
	 */

	private Set<Passenger> passenger;

	public void setPassenger(Set<Passenger> passenger) {
		this.passenger = passenger;
	}

	public void addPassenger(Passenger passenger) {
		this.passenger.add(passenger);
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trip_passenger", joinColumns = {@JoinColumn(name = "trip_id")},
			inverseJoinColumns = {@JoinColumn(name = "name")})
	@JsonManagedReference
	public Set<Passenger> getPassenger() {
		if (this.passenger == null) {
			this.passenger = new HashSet<Passenger>();
		}
		return this.passenger;
	}

}
