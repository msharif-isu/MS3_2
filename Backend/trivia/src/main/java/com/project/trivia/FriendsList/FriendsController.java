package com.project.trivia.FriendsList;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class FriendsController {

    @Autowired
    FriendsRepository friendsRepo;

    @Autowired
    UserRepository userRepo;

    private String success = "{\"message\":\"success\"}";

    private String failure = "{\"message\":\"failure\"}";
    @GetMapping(path = "/friends/{id}")
    Friends getFriendById(@PathVariable int id){
        return friendsRepo.findById(id);
    }

    @GetMapping(path = "/allPeople")
    List<Friends> getAllFriendsInTable() {
        return friendsRepo.findAll();
    }


    @PostMapping(path = "/{userId}/addFriend/{friendId}")
    String addFriend(@PathVariable int userId, @PathVariable int friendId){
        User user1 = userRepo.findById(userId);
        User user2 = userRepo.findById(friendId);
        Friends friend1 = friendsRepo.findById(friendId);
        Friends friend2 = friendsRepo.findById(userId);

        if (user1 == null || friend1 == null) {
            return "User or friend not found.";
        }

        if (user1.getFriends().contains(friend1)) {
            return "User is already friends with this friend.";
        }

        user1.getFriends().add(friend1);
        userRepo.save(user1);

        user2.getFriends().add(friend2);
        userRepo.save(user2);

        return user1.getUsername() + " is now friends with " + friend1.getUsername();
    }


    @DeleteMapping("/{userId}/removeFriend/{friendId}")
    String removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        User user = userRepo.findById(userId);
        Friends friend = friendsRepo.findById(friendId);

        if (user == null || friend == null) {
            return "User or friend not found.";
        }
        if (!user.getFriends().contains(friend)) {
            return user.getUsername() +" is not friends with " + friend.getUsername();
        }

        user.getFriends().remove(friend);
        userRepo.save(user);

        return user.getUsername() + " and " + friend.getUsername() + " are no longer friends ";
    }

    @GetMapping(path = "/friendsList/{userId}")
    List<Friends> friendsOfUser(@PathVariable int userId){
        User user = userRepo.findById(userId);
        if(user == null){
            return Collections.emptyList();
        }
        return user.getFriends();
    }


}

