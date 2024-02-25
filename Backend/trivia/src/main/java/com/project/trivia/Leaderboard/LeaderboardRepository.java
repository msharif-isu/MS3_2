package com.project.trivia.Leaderboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    Leaderboard findById(int id);

    @Transactional
    void deleteById(int id);
}