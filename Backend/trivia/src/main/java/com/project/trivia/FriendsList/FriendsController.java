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


    @PostMapping(path = "/friendToTable")
    String addToTable(@RequestBody Friends friend){
        if (friend == null){
            return failure;
        }
        friendsRepo.save(friend);

        return success;
    }

    @PostMapping(path = "/{userId}/addFriend/{friendId}")
    String addFriend(@PathVariable int userId, @PathVariable int friendId){
        User user = userRepo.findById(userId);
        Friends friend = friendsRepo.findById(friendId);

        if (user == null || friend == null) {
            return "User or friend not found.";
        }

        if (user.getFriends().contains(friend)) {
            return "User is already friends with this friend.";
        }

        user.getFriends().add(friend);
        userRepo.save(user);

        return user.getUsername() + " is now friends with " + friend.getUsername();
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

    @GetMapping(path = "/friendsOfUser/{userId}")
    List<Friends> friendsOfUser(@PathVariable int userId){
        User user = userRepo.findById(userId);
        if(user == null){
            return Collections.emptyList();
        }
        return user.getFriends();
    }


}

