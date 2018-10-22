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
import ca.mcgill.ecse321.karpool.application.Rating;
import ca.mcgill.ecse321.karpool.application.*;
import ca.mcgill.ecse321.karpool.application.repository.*;

public class KarpoolController {

	public static final String ERROR_NOT_FOUND_MESSAGE = "NOT FOUND";

	@Autowired
	KarpoolRepository repository;

	@Autowired
	Driver driver;

	@Autowired
	Set<Passenger> passengers;

	//TODO: Password can't just be read as a regular String. NEEDS some form of encryption.
	//FIXME: Why does createUser need to return the usersName?


	/**
	 * Checks if number is appropraite + confirms user does not have a criminal record (to be further implemented in Sprint 2)
	 * @param name
	 * @param email
	 * @param password
	 * @param phone
	 * @param rating
	 * @return the users object if found, null if not
	 */
	@PostMapping("/USERS/{email}")
	public User createUser(@PathVariable("name")String name, String email, String password, String phone, Rating rating, Boolean criminalRecord)
	{
		//TODO: need to check the rest of the inputs as well, not just phone number
		try {
			if(phone.length() == 10) {
				Integer.parseInt(phone);
			}
			else {
				System.out.println("You entered an invalid phone number");
				return null;
			}

		} catch(NullPointerException |  NumberFormatException e) {
			System.out.println("You entered an invalid phone number");
			return null;
		}
		if(!criminalRecord) {
		return repository.createUser(name, email, password, phone, rating, criminalRecord);
		}

		return null;
	}

	/**
	 *
	 * @param email
	 * @param password
	 * @return TRUE if the account is authenticated
	 */
	@GetMapping("/USERS/{email}")
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
	@GetMapping("/USERS/{email}")
	public String queryUser(@PathVariable("email")String email)
	{
		User user = repository.getUser(email);
		if(user == null)
		{
			return ERROR_NOT_FOUND_MESSAGE;
		}
		return user.getName();
	}



	@PostMapping("/TRIPS/{trip}")
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

	@PostMapping("/TRIPS/{trip}")
	public void closeTrip(@PathVariable ("trip") Trip trip)
	{
		repository.closeTrip(trip);


	}




	/**
	 * Adds a  rating to the user
	 * @param email
	 * @param rating
	 */
	@GetMapping("/USERS/{email}")
	public void addRating(@PathVariable("email")String email, Rating rating)
	{
		try {
			User user = repository.getUser(email);
			user.setRating(rating);

		}
		catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}

	}
<<<<<<< HEAD
	
	/**
	 * This method allows for new passengers to be added to a specific 
	 * trip taking place. It checks to see if the passenger is already 
=======

	/**
	 * This method allows for new passengers to be added to a specific
	 * trip taking place. It checks to see if the passenger is already
>>>>>>> f727ddf3a7469fcd43e6c43fb35526ea14385b43
	 * signed up for this trip, if not it adds to passenger to the trip.
	 * @param passenger
	 * @param trip
	 * @return
	 */
	@GetMapping
	public boolean addPassenger(Passenger passenger, Trip trip) {

		boolean wasAdded = false;
		if (trip.getSeatAvailable()<=0) {
		return false;
	}
		else if (passengers.contains(passenger)) {
		return false;
	}

	else
	{
		passenger.setTrip(trip);
	}

	wasAdded = true;
	return wasAdded;



}


	public float Distance (int zipcode1, int zipcode2) throws MalformedURLException, IOException
	{

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
            if (RoughDistance.charAt(2) == 'e') {
            	return 0;
            }
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
