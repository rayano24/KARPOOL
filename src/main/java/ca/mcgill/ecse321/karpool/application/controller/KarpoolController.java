package ca.mcgill.ecse321.karpool.application.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.karpool.model.User;
import ca.mcgill.ecse321.karpool.application.repository.*;

public class KarpoolController {

	public static final String ERROR_NOT_FOUND_MESSAGE = "Not found";
	
	@Autowired 
	KarpoolRepository repository;
	
	@PostMapping("/users/{name}")
	public String createUser(@PathVariable("name")String name)
	{
		User user = repository.createUser(name);
		return user.getName();
	}
	
	@GetMapping("/users/{name}")
	public String queryUser(@PathVariable("name")String name)
	{
		User user = repository.getUser(name);
		if(user == null)
		{
			return "NOT FOUND";
		}
		return user.getName();
	}
	
	
}
