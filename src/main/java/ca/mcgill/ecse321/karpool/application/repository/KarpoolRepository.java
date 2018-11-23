package ca.mcgill.ecse321.karpool.application.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////                                                                   /////////////////
	/////////////////                     DRIVER REPOSITORY                             /////////////////
	/////////////////                                                                   /////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Transactional
	public Driver createDriver(String name, String email, String password, String phoneNumber, boolean criminalRecord){
		Driver driver = new Driver();
		driver.setName(name);
		driver.setEmail(email);
		driver.setPassword(password);
		driver.setPhoneNumber(phoneNumber);
		driver.setRecord(criminalRecord);
		driver.setTrips(new HashSet<Trip>());
		driver.setRatings(new ArrayList<Double>());
		entityManager.persist(driver);
		return driver;
	}
	
	@Transactional
	public void addDriverRating(Driver driver, double rating) 
	{
		if (rating <= 5 && rating > 0) {
			driver.addRating(rating);
			entityManager.merge(driver);
		}
	}
	
	@Transactional
	public Driver getDriver(String name) {
		Driver driver = entityManager.find(Driver.class, name);
		return driver;
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
	public List<Integer> getTripForDriver(String name)
	{
		Driver d = entityManager.find(Driver.class, name);
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip WHERE driver= :driver");
		q.setParameter("driver", d);
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public Set<Trip> getTripForDriver(Driver d)
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip WHERE driver= :driver");
		q.setParameter("driver", d);
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		Set<Trip> tripList = new HashSet<Trip>();
		for(int i: trips)
		{
			tripList.add(getSpecificTrip(i));
		}
		return tripList;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////                                                                   /////////////////
	/////////////////                    PASSENGER REPOSITORY                           /////////////////
	/////////////////                                                                   /////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Transactional
	public Passenger createPassenger(String name, String email, String password, String phoneNumber, boolean criminalRecord){
		Passenger pass = new Passenger();
		pass.setName(name);
		pass.setEmail(email);
		pass.setPassword(password);
		pass.setPhoneNumber(phoneNumber);
		pass.setRecord(criminalRecord);
		pass.setTrips(new HashSet<Trip>());
		entityManager.persist(pass);
		return pass;
	}
	
	@Transactional
	public Passenger getPassenger(String name) {
		Passenger passenger = entityManager.find(Passenger.class, name);
		return passenger;
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
	public Set<Trip> getTripsForPassenger(String name)
	{
		Passenger p = entityManager.find(Passenger.class, name);
		Set<Trip> t = p.getTrips();
		return t;
	}
	
	@Transactional
	public Set<Trip> getTripsForPassenger(Passenger p)
	{
		Set<Trip> t = p.getTrips();
		return t;
	}
	
	@Transactional
	public Trip addPassengerToTrip(Passenger p, Trip t)
	{
		t.addPassenger(p);
		t.setSeatAvailable(t.getSeatAvailable()-1);
		p.addTrip(t);
		entityManager.merge(p);
		entityManager.merge(t);
		return t;
	}
	
	@Transactional
	public void removePassengerFromTrip(Passenger p, Trip t)
	{
		t.removePassenger(p);
		t.setSeatAvailable(t.getSeatAvailable()+1);
		p.removeTrip(t);
		entityManager.merge(p);
		entityManager.merge(t);
	}

	@Transactional
	public boolean checkPassengerInTrip(Trip t, Passenger p) 
	{
		if(t.getPassenger().contains(p))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Transactional
	public Set<Passenger> getPassengersInTrip(Trip t)
	{
		Set<Passenger> p = t.getPassenger();
		return p;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////                                                                   /////////////////
	/////////////////                      TRIPS REPOSITORY                             /////////////////
	/////////////////                                                                   /////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Transactional
	public Trip createTrip(Driver driver, String destination, String departureTime, String departureDate, String departureLocation, int seatAvailable, int price) {
		Trip trip = new Trip();
		trip.setDriver(driver);
		trip.setDestination(destination);
		trip.setDepartureTime(departureTime);
		trip.setDepartureDate(departureDate);
		trip.setDepartureLocation(departureLocation);
		trip.setSeatAvailable(seatAvailable);
		trip.setTripComplete(false);
		trip.setPrice(price);
		trip.setPassenger(new HashSet<Passenger>());
		entityManager.persist(trip);
		return trip;
	}
	
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
	public void modifyTripPrice(Trip trip, int price) {
		
		trip.setPrice(price);
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
	public Trip getSpecificTrip(int trip_id)
	{
		Trip t = entityManager.find(Trip.class, trip_id);
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
	public List<Integer> getSortedTripsPrice(String start, String finish) 
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip WHERE departure_location= :departure AND destination= :destination ORDER BY price");
		q.setParameter("departure", start);
		q.setParameter("destination", finish);
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public List<String> getFrequentDestinations() 
	{
		Query q = entityManager.createNativeQuery("SELECT destination FROM trip ORDER BY destination");
		@SuppressWarnings("unchecked")
		List<String> trips = q.getResultList();
		return trips;
	}

	@Transactional
	public void closeTrip(int tripID)
	{
		Trip trip = entityManager.find(Trip.class, tripID);
		String tripDate = trip.getDepartureDate(); 
		
		Date date = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dateInput = null;
		try {
			dateInput = sdf.parse(tripDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String date1 = sdf.format(dateInput);        
		String date2 = sdf.format(date);
		
		if (date1.compareTo(date2) > 0) {
			System.out.println("Cannot close a trip that has not yet occured");
		}
		
		else if (date1.compareTo(date2) <= 0) {
			
		
		trip.setTripComplete(true);
		entityManager.merge(trip);
		}
	}

	@Transactional
	public void deleteTrip(int tripID) throws ParseException 
	{
		Trip t = entityManager.find(Trip.class, tripID);
		String tripDate = t.getDepartureDate();
		
		Date date = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dateInput = sdf.parse(tripDate);
		String date1 = sdf.format(dateInput);        
		String date2 = sdf.format(date);
		
		if ((date1.compareTo(date2)) <= 0) {
			System.out.println("Cannot delete a trip on the day of the trip");
		
		}
		
		else if (t.isTripComplete() == true) {
			System.out.println("Cannot delete a trip that has been completed");
			
		}
		else {
		entityManager.remove(t);
		}
	}

}
