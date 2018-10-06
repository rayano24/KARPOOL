package ca.mcgill.ecse321.karpool.application.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.karpool.model.User;

public class KarpoolRepository {
	
	@Autowired 
	EntityManager entityManager;
	
	@Transactional
	public User createUser(String name) {
		User user = new User();
		user.setName(name);
		entityManager.persist(user);
		return user;
	}
	
	@Transactional
	public User getUser(String name) {
		User user = entityManager.find(User.class, name);
		return user;
	}
	

}
