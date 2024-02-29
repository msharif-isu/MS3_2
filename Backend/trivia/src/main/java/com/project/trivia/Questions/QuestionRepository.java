package com.project.trivia.Questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findById(int id);

    @Transactional
    void deleteById(int id);
}