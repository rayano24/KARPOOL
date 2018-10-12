package ca.mcgill.ecse321.karpool.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class KarpoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarpoolApplication.class, args);
	}

	@RequestMapping("/")
  public String greeting(){
    return "Hello world!";
  }
}
