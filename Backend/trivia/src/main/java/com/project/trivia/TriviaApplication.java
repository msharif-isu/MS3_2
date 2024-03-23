package com.project.trivia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * The main application class for the WebSocket server.
 *
 * This class uses the `@ComponentScan` annotation to specify the base package
 * for component scanning. It scans the "com.example.demo.websocket" package
 * and its subpackages for Spring components, such as WebSocket endpoints and
 * controllers.
 *
 * Include this annotation as per your project structure needs
 * common case:
 * websocket-related files placed in sub-directory
 * all http controllers work except the websocket ones
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.project.trivia.websocket", "com.project.trivia.Leaderboard"})
public class TriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriviaApplication.class, args);
	}	

}
