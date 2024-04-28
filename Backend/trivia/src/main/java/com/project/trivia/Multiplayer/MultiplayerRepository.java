package com.project.trivia.Multiplayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface MultiplayerRepository extends JpaRepository<Multiplayer, Long> {
    Multiplayer findById(int id);

    @Transactional
    void deleteById(int id);



}

