package project.Users.userController;

import org.springframework.transaction.annotation.Transactional;
import project.Questions.questionControllers.Question;


public interface UserRepository {
    Question findById(int id);

    @Transactional
    void deleteById(int id);
}
