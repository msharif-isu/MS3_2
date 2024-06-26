package com.project.trivia.PelicanQuestions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PelicanQuestionRepository extends JpaRepository<PelicanQuestion, Long> {
    PelicanQuestion findById(int id);

    @Transactional
    void deleteById(int id);
}
