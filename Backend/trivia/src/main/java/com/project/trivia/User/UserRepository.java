package com.project.trivia.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    @Transactional
    void deleteById(int id);

    User findByUsername(String username);

    List<User> findAllByPassword(String password);

    boolean existsByUsername(String username);

}

