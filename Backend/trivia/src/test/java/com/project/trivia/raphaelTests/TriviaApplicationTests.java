package com.project.trivia.raphaelTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.project.trivia.Leaderboard.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class leadboardTest {

	@Autowired
	private LeaderboardController controller;

	@Test
	void contextLoads() throws Exception{
		assertThat(controller).isNotNull();
	}

}
