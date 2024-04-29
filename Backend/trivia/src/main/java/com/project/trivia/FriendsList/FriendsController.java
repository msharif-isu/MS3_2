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


    @PostMapping(path = "/{userId}/addFriend/{friendName}")
    String addFriend(@PathVariable int userId, @PathVariable String friendName){
        User user1 = userRepo.findById(userId);
        User user2 = userRepo.findByUsername(friendName);
        Friends friend1 = friendsRepo.findByUsername(friendName);
        Friends friend2 = friendsRepo.findByUsername(user1.getUsername());

        if (user1 == null || friend1 == null) {
            return "User or friend not found.";
        }

        if (user1.getFriends().contains(friend1)) {
            return "User is already friends with this friend.";
        }

        //updates each users friends stats
        user1.getStats().setNumberOfFreinds(user1.getStats().getNumberOfFreinds() + 1);
        user2.getStats().setNumberOfFreinds(user2.getStats().getNumberOfFreinds() + 1);

        user1.getFriends().add(friend1);
        userRepo.save(user1);

        user2.getFriends().add(friend2);
        userRepo.save(user2);


        return user1.getUsername() + " is now friends with " + friend1.getUsername();
    }


    @DeleteMapping("/{userId}/removeFriend/{friendName}")
    String removeFriend(@PathVariable int userId, @PathVariable String friendName) {
        User user1 = userRepo.findById(userId);
        User user2 = userRepo.findByUsername(friendName);
        Friends friend1 = friendsRepo.findByUsername(friendName);
        Friends friend2 = friendsRepo.findByUsername(user1.getUsername());

        if (user1 == null || friend1 == null) {
            return "User or friend not found.";
        }
        if (!user1.getFriends().contains(friend1)) {
            return user1.getUsername() +" is not friends with " + friend1.getUsername();
        }

        //updates each users friends stats
        user1.getStats().setNumberOfFreinds(user1.getStats().getNumberOfFreinds() - 1);
        user2.getStats().setNumberOfFreinds(user2.getStats().getNumberOfFreinds() - 1);

        user1.getFriends().remove(friend1);
        userRepo.save(user1);

        user2.getFriends().remove(friend2);
        userRepo.save(user2);



        return user1.getUsername() + " and " + friend1.getUsername() + " are no longer friends ";
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