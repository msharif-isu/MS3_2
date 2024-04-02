package com.project.trivia.FriendsList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    Friends findById(int id);
    @Transactional
    void deleteById(int id);

    Friends findByUsername(String name);

}