package com.project.trivia.raphaelTests;

import com.project.trivia.MPQuestions.Answer;
import com.project.trivia.MPQuestions.AnswerRepository;
import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import com.project.trivia.User.User;

import com.project.trivia.Queryboard.Query;

import com.project.trivia.User.UserRepository;
import com.project.trivia.roomChat.MessageRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.project.trivia.Leaderboard.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.util.Predicates.isTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;


import io.restassured.RestAssured;
import io.restassured.response.Response;


import io.restassured.RestAssured;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class raphaelTest {


	@Autowired
	private LeaderboardRepository lbRepo;
	private static Leaderboard lbInit;
	@Autowired
	private UserRepository userRepo;
	private static User userInit;

	@Autowired
	private QuestionRepository questRepo;
	private static List<Question> questInit = new ArrayList<>();

	@Autowired
	private AnswerRepository answerRepo;
	private static Answer answerInit;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private LeaderboardController controller;

	//@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	//@Autowired
	//private MockMvc mockMvc;


	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;

		lbInit = new Leaderboard(11112912, 12911, 1800, 2911, 1112912, "aloks");

		userInit = new User("Alok", "1234", "alok@iastate.edu");

		questInit = new ArrayList<>();

		answerInit = new Answer("Alokss", "Thales of Miletusss", false);


	}


	@Test
	void userLeaderboardTest() {
		//lbRepo.save(lbInit);
		userRepo.save(userInit);
		//lbRepo.findById(1).setUser(userRepo.findById(1));
		//lbRepo.save(lbInit);



		ResponseEntity<Leaderboard> lb1 = restTemplate.postForEntity("/leaderboard", lbInit, Leaderboard.class);
		lb1.getBody().setUser(userRepo.findById(1));
		userInit.setLeaderboard(lb1.getBody());

		assertThat(lb1.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(lb1.getBody().getUser()).isEqualTo(lbRepo.findById(1).getUser());
		assertThat(userInit.getLeaderboard()).isEqualTo(lb1.getBody());
	}

	@Test
	void questionQueryTest() {
		Question q1 = new Question("Who was the first philosopher?", "Thales of Miletus", "Philosophy", false, false);
		Question q2 = new Question("Who were the three famous Greek philosophers?", "Socrates, Plato, and Aristotle", "Philosophy", false, false);
		Question q3 = new Question("What were the two main schools of thought during enlightenment?", "Continental Rationalists and the British Empicircists", "Philosophy", false, false);
		Question q4 = new Question("Who was the first existentialist?", "Soren Kierkegaard", "Philosophy", false, false);
		Question q5 = new Question("Who was famous for his Ontological Argument?", "St. Anselm of Canterbury", "Philosophy", false, false);
		Question q6 = new Question("What school of thought was heavily influenced by economics?", "Frankfurt School of Thought", "Philosophy", false, false);
		Question q7 = new Question("Who was the Chinese philosopher who used terms line Ren and Li?", "Confucius", "Philosophy", false, false);
		Question q8 = new Question("What was the main book of Taoism?", "Tao Te Ching", "Philosophy", false, false);
		Question q9 = new Question("Who wrote the Tao Te Ching?", "Laozi", "Philosophy", false, false);
		Question q10 = new Question("Who is Raphael's favorite philsopher?", "Philosophocles", "Personal", false, false);

		questInit.add(q1);
		questInit.add(q2);
		questInit.add(q3);
		questInit.add(q4);
		questInit.add(q5);
		questInit.add(q6);
		questInit.add(q7);
		questInit.add(q8);
		questInit.add(q9);
		questInit.add(q10);

		Question[] questArray = new Question[9];
		for (int i=0; i<9; i++) {
			questArray[i] = questInit.get(i);
		}


		ResponseEntity<Question[]> questTest = restTemplate.getForEntity("/query/topic/Philosophy", Question[].class);
		assertThat(questTest.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertArrayEquals(questTest.getBody(), questArray);
		//assertArrayEquals(questRepo.findById(1).getAnswers().toArray(), questArray[0].getAnswers().toArray());
	}


	@Test
	void answerQuestionTest() {
		Question q1 = new Question("Who was the first philosopher?", "Thales of Miletus", "Philosophy", false, false);

		ResponseEntity<Answer> ansTest = restTemplate.postForEntity("/answer", answerInit, Answer.class);
		ansTest.getBody().setQuestion(questRepo.findById(1));
		q1.addAnswer(ansTest.getBody());
		int ansId = q1.getAnswers().size();
		assertThat(ansTest.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(ansTest.getBody().getQuestion()).isEqualTo(questRepo.findById(1));
		assertThat(q1.getAnswers().get(ansId-1)).isEqualTo(ansTest.getBody());
		q1.getAnswers().remove(ansId-1);
	}

	@Test
	void putAnswer() {
		Answer newAnswer = new Answer("a lock", "Miles", false);
		newAnswer.setQuestion(questRepo.findById(1));

		ResponseEntity<Answer> ansTest = restTemplate.postForEntity("/answer", answerInit, Answer.class);
		Answer ogAnswer = ansTest.getBody();
		int id = ogAnswer.getId().intValue();
		String ansUrl = "/answer/" + id;
		restTemplate.put(ansUrl, answerInit, Answer.class);

		assertThat(ansTest.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(ogAnswer).isEqualTo(ansTest.getBody());
	}






}
