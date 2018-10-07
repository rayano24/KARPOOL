package ca.mcgill.ecse321.karpool.application.controller;

import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.karpool.model.Trip;
import ca.mcgill.ecse321.karpool.model.User;
import ca.mcgill.ecse321.karpool.application.*;	
import ca.mcgill.ecse321.karpool.application.repository.*;

public class KarpoolController {



	public static final String ERROR_NOT_FOUND_MESSAGE = "Not found";

	@Autowired
	KarpoolRepository repository;

	@Autowired
	Driver driver;

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
	public String createUser(@PathVariable("name")String name, String email, String password, String phone, Rating rating, boolean criminalRecord)
	{
		User user = repository.createUser(name, email, password, phone, rating, criminalRecord);
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
	
	

	@PostMapping("/trip/{trip}")
	public Trip createTrip (@PathVariable ("trip") String departureLocation, String destination, int seatAvailable, String departureTime)
	{
		Trip trip = repository.createTrip(destination,departureTime, departureLocation, seatAvailable);
		return trip;
	}


	@RequestMapping(path="/{departureLocation}/{destination}/{seats}/")
	public Trip queryTrip(@PathVariable("departureLocation") String departureLocation, @PathVariable("destination") String destination, @PathVariable ("seats") int seatAvailable)
	{
		Trip Trip = driver.getTrip();

		return Trip;
	}

	@PostMapping("/trip/{trip}")
	public void closeTrip(@PathVariable ("trip") Trip trip)
	{
		repository.closeTrip(trip);


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

//	public boolean addPassenger(Passenger passenger) {
//		boolean wasAdded = false;
//		if (passengers.contains(passenger)) {
//			return false;
//		}
//		Trip existingTrip = passenger.getTrip();
//		boolean isNewTrip = (existingTrip != null && !this.equals(existingTrip));
//
//		if (isNewTrip) {
//			passenger.setTrip(this);
//		}
//
//		else {
//			passenger.add(passenger);
//		}
//		wasAdded = true;
//		return wasAdded;
//
//
//
//	}
//
	public float Distance (int zipcode1, int zipcode2) throws MalformedURLException, IOException {

        BufferedReader br = null;

        try {

            URL url = new URL("https://www.zipcodeapi.com/rest/GOhazMBKVJ2VDSEOrrkf0sswW4D5c4NYOjZi2mGTjf2wuvgvTkUj5L1KpR2GkRRI/distance.json/" + zipcode1 + "/" +zipcode2 +"/km");
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            String RoughDistance = sb.toString();
            String intValue = RoughDistance.replaceAll("[^0-9, .]", "");
            float distance = Float.parseFloat(intValue);
            return distance;

        } finally {

            if (br != null) {
                br.close();
            }
        }
    }



}
