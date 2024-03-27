package com.project.trivia.MPQuestions;

import com.project.trivia.Questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
    Answer findById(int id);

    @Transactional
    void deleteById(int id);

    long count();
}
