package com.project.trivia.MatchHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Integer> {

    MatchHistory findById(int id);

    @Transactional
    void deleteById(int id);
}
