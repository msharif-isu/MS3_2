package project.Users.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.Questions.questionControllers.Question;
import project.Questions.questionControllers.QuestionRepository;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    QuestionRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    List<Question> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/user/{id}")
    Question getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }
}
