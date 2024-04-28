package com.project.trivia.UserStats;

import com.project.trivia.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {

    UserStats findById(int id);

    @Transactional
    void deleteById(int id);

}
