package Api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * This class is the principal class of the server
 * @author Students in first year of master IT at UPEC
 * @version 1.0
 */
@SpringBootApplication
@EnableAsync

public class Application {
	
	/**
	 * Main method to start the server 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
	