package com.project.trivia.User;

import com.project.trivia.Leaderboard.Leaderboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    public String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        else if(userRepository.existsByUsername(user.getUsername())){
            return failure;
        }
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    public String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }


    //Gives user points
    @PutMapping(path = "/users/{username}/{points}")
    public User givePoints(@PathVariable String username , @PathVariable int points){
        User user = userRepository.findByUsername(username);
        if(user == null)
            return null;
        user.setPoints((int) (user.getPoints() + points));
        userRepository.save(user);
        return user;
    }

    //Temp way to get id of username passowrd

    @GetMapping(path = "/users/getIdByUsername/{username}")
    public int getIdByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            return -1;
        }
    }


    @GetMapping(path = "/users/getIdByPassword/{password}")
    public ArrayList<Integer> getIdByPassword(@PathVariable String password) {
        List<User> users = userRepository.findAllByPassword(password);
        ArrayList<Integer> passwordIds = new ArrayList<>();

        for (User user : users) {
            passwordIds.add(user.getId());
        }
        return passwordIds;
    }
    
    @GetMapping(path="/users/getPoints/{id}")
    Leaderboard lb (@PathVariable int id) {
        User user = userRepository.findById(id);
        return user.getLeaderboard();
    }
}
