package com.project.trivia.Achievements;

import com.project.trivia.Questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AchievementRepository extends JpaRepository<Achievement, Long>{
    Achievement findById(int id);

    @Transactional
    void deleteById(int id);

    long count();
}
