package com.project.trivia.Lobby;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Lobby findById(int id);

    @Transactional
    void deleteById(int id);



}

