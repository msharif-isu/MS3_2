package com.project.trivia.raphaelTests;

import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.Leaderboard.LeaderboardController;
import com.project.trivia.Leaderboard.LeaderboardRepository;
import com.project.trivia.MPQuestions.Answer;
import com.project.trivia.MPQuestions.AnswerRepository;
import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
		userInit.setBio("BIO");
		userInit.setUsername("Alok");
		userRepo.save(userInit);
		//lbRepo.findById(1).setUser(userRepo.findById(1));
		//lbRepo.save(lbInit);



		ResponseEntity<Leaderboard> lb1 = restTemplate.postForEntity("/leaderboard", lbInit, Leaderboard.class);
		lb1.getBody().setUser(userRepo.findById(1));
		userInit.setLeaderboard(lb1.getBody());

		assertThat(lb1.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(lb1.getBody().getUser()).isEqualTo(lbRepo.findById(1).getUser());
		assertThat(userInit.getLeaderboard()).isEqualTo(lb1.getBody());
		assertThat(userInit.getUsername()).isEqualTo("Alok");
		assertThat(userInit.getBio()).isEqualTo("BIO");

		ResponseEntity<Leaderboard[]> lb2 = restTemplate.getForEntity("/leaderboard", Leaderboard[].class);
		assertThat(lb2.getBody().length).isEqualTo(lbRepo.findAll().toArray().length);


		ResponseEntity<Leaderboard> lb4 = restTemplate.postForEntity("/leaderboard/addpoints/1/100", lbInit, Leaderboard.class);


		ResponseEntity<Leaderboard> lb3 = restTemplate.getForEntity("/leaderboard/1", Leaderboard.class);
		assertThat(lb3.getBody().getName()).isEqualTo(lbRepo.findById(1).getName());
		assertThat(lb3.getBody().getUserPoints()).isEqualTo(lbRepo.findById(1).getUserPoints());
		
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

		ResponseEntity<Question> questTest2 = restTemplate.getForEntity("/question/1", Question.class);
		assertThat(questTest2.getBody().getQuestion()).isEqualTo(questRepo.findById(1).getQuestion());

		ResponseEntity<Answer[]> questTest3 = restTemplate.getForEntity("/question/answers/1", Answer[].class);
		assertThat(questTest3.getBody().length).isEqualTo(questRepo.findById(1).getAnswers().size());

		questRepo.findById(1).setAnswerList(Arrays.stream(questTest3.getBody()).toList());
		//assertArrayEquals(questTest3.getBody(), questRepo.findById(1).getAnswers().toArray());
		//assertArrayEquals(questTest3.getBody(), questRepo.findById(1).getAnswers().toArray());
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

		ResponseEntity<Answer> ansTest2 = restTemplate.getForEntity("/answer/1", Answer.class);
		assertThat(ansTest2.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(ansTest2.getBody().getAnswer()).isEqualTo("Thales of Miletusss");
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
