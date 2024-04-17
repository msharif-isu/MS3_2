package com.project.trivia.raphaelTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.project.trivia.Leaderboard.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class leaderboardTest {

	@MockBean
	LeaderboardRepository lbRepo;

	@Autowired
	private LeaderboardController controller;

	//@Autowired
	//private MockMvc mockMvc;

	/*
	@Test
	void getFirstLeaderboard() throws Exception {
		this.mockMvc.perform(get("/leaderboard/1")).andDo(print()).andExpect(status().isOk()).andExpect(content().llbRepo.findById(1))
	}
	 */
	/*
	@Test
	void getFirstLeaderboard() {
		Leaderboard lb1 = lbRepo.findById(1);
	}
*/
}
