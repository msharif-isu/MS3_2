package com.project.trivia.User;

import com.project.trivia.FriendsList.Friends;
import com.project.trivia.FriendsList.FriendsRepository;
import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.UserStats.UserStats;
import com.project.trivia.UserStats.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendsRepository friendRepo;

    @Autowired
    UserStatsRepository statsRepo;

    private static String directory = System.getProperty("user.dir");

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    public String createUser(@RequestBody User user) {
        if (user.getUsername() == null)
            return failure;
        Friends temp = new Friends(user.getUsername());
        UserStats stats = new UserStats();
        if(userRepository.existsByUsername(user.getUsername())){
            return failure;
        }
        statsRepo.save(stats);

        user.setStats(stats);
        userRepository.save(user);
        friendRepo.save(temp);
        return success;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User request) {
        User user = userRepository.findById(id);
        if(user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if(request.getUsername() == null || userRepository.existsByUsername(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Username already Taken");
        }
        if(request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if(request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }
        if(request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        userRepository.save(user);
        return userRepository.findById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    public String deleteUser(@PathVariable int id) {
        Friends temp = friendRepo.findByUsername((userRepository.findById(id).getUsername()));
        friendRepo.deleteById(temp.getId());
        userRepository.deleteById(id);
        return success;
    }


    //Gives user points
    @PutMapping(path = "/users/{username}/{points}")
    public User givePoints(@PathVariable String username, @PathVariable int points) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return null;
        user.setPoints((int) (user.getPoints() + points));
        userRepository.save(user);
        return user;
    }

    @PutMapping(path = "/editBio/{username}/{bio}")
    public User editBio(@PathVariable String username, @PathVariable String bio) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return null;
        user.setBio(bio);
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

    @GetMapping(path = "/users/getBio/{username}")
    public String getBioByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }
        return user.getBio();
    }

    @GetMapping(path = "/users/getPoints/{id}")
    Leaderboard lb(@PathVariable int id) {
        User user = userRepository.findById(id);
        return user.getLeaderboard();
    }


    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageById(@PathVariable int id) throws IOException {
        User user = userRepository.findById(id);
        File imageFile = new File(user.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }

    @PostMapping("/setPfp/{id}")
    public String handleFileUpload(@RequestParam("image") MultipartFile imageFile, @PathVariable int id) {

        try {
            File destinationFile = new File(directory + File.separator + imageFile.getOriginalFilename());
            imageFile.transferTo(destinationFile);  // save file to disk

            User user = userRepository.findById(id);
            user.setFilePath(destinationFile.getAbsolutePath());
            userRepository.save(user);

            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }


}
