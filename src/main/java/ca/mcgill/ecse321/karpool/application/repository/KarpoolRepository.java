package ca.mcgill.ecse321.karpool.application.repository;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.karpool.application.model.*;

@Repository
public class KarpoolRepository 
{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Trip createTrip(Driver driver, String destination, String departureTime, String departureDate, String departureLocation, int seatAvailable, int price) {
		Trip trip = new Trip();
		trip.setDriver(driver);
		trip.setDestination(destination);
		trip.setDepartureTime(departureTime);
		trip.setDepartureDate(departureDate);
		trip.setDepartureLocation(departureLocation);
		trip.setSeatAvailable(seatAvailable);
		trip.setPrice(price);
		entityManager.persist(trip);
		return trip;
	}

	@Transactional
	public Driver createDriver(String name, String email, String password, String phoneNumber, boolean criminalRecord){
		Driver driver = new Driver();
		driver.setName(name);
		driver.setEmail(email);
		driver.setPassword(password);
		driver.setPhoneNumber(phoneNumber);
		driver.setRating(Rating.NONE);
		driver.setRecord(criminalRecord);
		entityManager.persist(driver);
		return driver;
	}
	
	@Transactional
	public Passenger createPassenger(String name, String email, String password, String phoneNumber, boolean criminalRecord){
		Passenger pass = new Passenger();
		pass.setName(name);
		pass.setEmail(email);
		pass.setPassword(password);
		pass.setPhoneNumber(phoneNumber);
		pass.setRating(Rating.NONE);
		pass.setRecord(criminalRecord);
		entityManager.persist(pass);
		return pass;
	}
	
	//Modification methods start
	@Transactional
	public void modifyTripLocation(Trip trip, String location) {
		
		trip.setDepartureLocation(location);
		entityManager.merge(trip);
		
	}
	@Transactional
	public void modifyTripDestination(Trip trip, String destination) {
		
		trip.setDestination(destination);
		entityManager.merge(trip);
		
	}
	@Transactional
	public void modifyDepartureTime(Trip trip, String departureTime) {
		
		trip.setDepartureTime(departureTime);
		entityManager.merge(trip);
		
	}
	@Transactional
	public void modifyDepartureDate(Trip trip, String departureDate) {
		
		trip.setDepartureDate(departureDate);
		entityManager.merge(trip);
		
	}
	@Transactional
	public void modifySeatAvailable(Trip trip, int seatAvailable) {
		
		trip.setSeatAvailable(seatAvailable);
		entityManager.merge(trip);
		
	}
	//modification methods end
	
	@Transactional
	public void setPassengerRating(Passenger passenger, Rating rating) {
		
		passenger.setRating(rating);
		entityManager.merge(passenger);
		
	}
	@Transactional
	public void setDriverRating(Driver driver, Rating rating) {
		
		driver.setRating(rating);
		entityManager.merge(driver);
		
	}
	
	
	@Transactional
	public Driver getDriver(String name) {
		Driver driver = entityManager.find(Driver.class, name);
		return driver;
	}
	
	@Transactional
	public Passenger getPassenger(String name) {
		Passenger passenger = entityManager.find(Passenger.class, name);
		return passenger;
	}
	
	@Transactional
	public List<String> getAllDrivers() 
	{
		Query q = entityManager.createNativeQuery("SELECT name FROM driver");
		@SuppressWarnings("unchecked")
		List<String> drivers = q.getResultList();
		return drivers;
	}
	
	@Transactional
	public List<String> getAllPassengers() 
	{
		Query q = entityManager.createNativeQuery("SELECT name FROM passenger");
		@SuppressWarnings("unchecked")
		List<String> passengers = q.getResultList();
		return passengers;
	}


	
	@Transactional
	public List<Integer> getTrips(String depart, String dest)
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip WHERE departure_location= :departure AND destination= :destination");
		q.setParameter("departure", depart);
		q.setParameter("destination", dest);
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public List<Integer> getAllTrips() 
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip");
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public Trip getSpecificTrip(int id)
	{
		Trip t = entityManager.find(Trip.class, id);
		return t;
	}
	
	@Transactional
	public List<Integer> getSortedTripsTime(String start, String finish) 
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip WHERE departure_location= :departure AND destination= :destination ORDER BY departure_date");
		q.setParameter("departure", start);
		q.setParameter("destination", finish);
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public Trip getTripForPassenger(String name)
	{
		Passenger p = entityManager.find(Passenger.class, name);
		Trip t = p.getTrip();
		return t;
	}
	
	@Transactional
	public Set<Trip> getTripForDriver(String name)
	{
		Driver d = entityManager.find(Driver.class, name);
		Set <Trip> t = d.getTrip();
		return t;
	}
	
	@Transactional
	public void closeTrip(int tripID)
	{
		Trip trip = entityManager.find(Trip.class, tripID);
		trip.setDestination(null);
		trip.setDepartureTime(null);
		trip.setDepartureLocation(null);
		trip.setSeatAvailable(0);
		entityManager.persist(trip);
	}
	
	@Transactional
	public Trip addPassenger(Passenger p, Trip t)
	{
		t.addPassenger(p);;
		t.setSeatAvailable(t.getSeatAvailable()-1);
//		entityManager.merge(t);
//		entityManager.merge(p);
		return t;
	}

}
