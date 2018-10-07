package ca.mcgill.ecse321.karpool.application.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.karpool.application.Rating;
import ca.mcgill.ecse321.karpool.model.Trip;
import ca.mcgill.ecse321.karpool.model.User;

public class KarpoolRepository {
	
	@Autowired 
	EntityManager entityManager;
	
	@Transactional
	public User createUser(String name, String email, String password, String phoneNumber, Rating rating ) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setPhoneNumber(phoneNumber);
		user.setRating(rating);
		entityManager.persist(user);
		return user;
	}
	
	@Transactional
	public User getUser(String email) {
		User user = entityManager.find(User.class, email);
		return user;
	}
	
	@Transactional
	public Trip createTrip(String destination, String departureTime, String departureLocation) {
		Trip trip = new Trip();
		trip.setDestination(destination);
		trip.setDepartureTime(departureTime);
		trip.setDepartureLocation(departureLocation);		
		return trip; 	
	}
	
	

}
