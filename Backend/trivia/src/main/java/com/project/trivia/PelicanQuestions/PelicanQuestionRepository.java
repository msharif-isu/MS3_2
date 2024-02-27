package com.project.trivia.PelicanQuestions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PelicanQuestionRepository extends JpaRepository<PelicanQuestions, Long> {
    PelicanQuestions findById(int id);

    @Transactional
    void deleteById(int id);
}
