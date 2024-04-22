package com.project.trivia.raphaelTests;

import com.project.trivia.User.User;

import com.project.trivia.User.UserRepository;
import com.project.trivia.roomChat.MessageRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.project.trivia.Leaderboard.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import io.restassured.RestAssured;
import io.restassured.response.Response;


import io.restassured.RestAssured;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class leaderboardTest {


	@Autowired
	private LeaderboardRepository lbRepo;
	private static Leaderboard lbInit;
	@Autowired
	private UserRepository userRepo;
	private static User userInit;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private LeaderboardController controller;

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;

		lbInit = new Leaderboard(11112912, 12911, 1800, 2911, 1112912, "aloks");

		userInit = new User("Alok", "1234", "alok@iastate.edu");


		//lbRepo.findById(0).setUser(user1);
	}

	/*
	@Test
	void getFirstLeaderboard() throws Exception {
		this.mockMvc.perform(get("/leaderboard/1")).andDo(print()).andExpect(status().isOk()).andExpect(content().llbRepo.findById(1))
	}
	 */

	@Test
	void getFirstLeaderboard() {
		lbRepo.save(lbInit);
		userRepo.save(userInit);
		lbRepo.findById(1).setUser(userRepo.findById(1));


		ResponseEntity<Leaderboard> lb1 = restTemplate.getForEntity("/leaderboard/1", Leaderboard.class);
		assertThat(lb1.getStatusCode()).isEqualTo(HttpStatus.OK);
		//assertThat(lb1.getBody().getUser()).isEqualTo(lbRepo.findById(1).getUser());
	}



}
