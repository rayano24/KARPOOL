package ca.mcgill.ecse321.karpool.application.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.karpool.model.User;
import ca.mcgill.ecse321.karpool.application.Rating;
import ca.mcgill.ecse321.karpool.application.repository.*;

public class KarpoolController {

	public static final String ERROR_NOT_FOUND_MESSAGE = "Not found";
	
	@Autowired
	KarpoolRepository repository;

	//TODO: Password can't just be read as a regular String. NEEDS some form of encryption.
	//FIXME: Why does createUser need to return the usersName?
	
	
	/**
	 * @param name
	 * @param email
	 * @param password
	 * @param phone
	 * @param rating
	 * @return the users name
	 */
	@PostMapping("/users/{email}")
	public String createUser(@PathVariable("name")String name, String email, String password, String phone, Rating rating)
	{
		User user = repository.createUser(name, email, password, phone, rating);
		return user.getName();
	}
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return OK if the account is authenticated 
	 */
	@GetMapping("/users/{email}")
	public boolean authenticateUser(@PathVariable("email")String email, @PathVariable("password")String password)
	{
		try {
			User user = repository.getUser(email);
			if(user.getPassword().equals(password)) 
				return true;	
		}
		catch(NullPointerException e) {
			return false;
		}
		
		return false;	
	}
	
	/**
	 * 
	 * @param email
	 * @return the account that was searched for 
	 */
	@GetMapping("/users/{email}")
	public String queryUser(@PathVariable("email")String email)
	{
		User user = repository.getUser(email);
		if(user == null)
		{
			return "NOT FOUND";
		}
		return user.getName();
	}
	
	
	/**
	 * Add a  rating to the user 
	 * @param email
	 * @param rating
	 */
	@GetMapping("/users/{email}")
	public void addRating(@PathVariable("email")String email, Rating rating)
	{
		try {
			User user = repository.getUser(email);
			user.setRating(rating);
			
		}
		catch (NullPointerException e) {
			System.out.println("NOT FOUND");
		}
				
		
	}
	
	


}
