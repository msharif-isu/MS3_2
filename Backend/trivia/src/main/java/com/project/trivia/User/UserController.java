package com.project.trivia.User;

import com.project.trivia.FriendsList.Friends;
import com.project.trivia.FriendsList.FriendsRepository;
import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.UserStats.UserStats;
import com.project.trivia.UserStats.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    public String createUser(@RequestBody User user) {
        Friends temp = new Friends(user.getUsername());
        UserStats stats = new UserStats(user);

        if (user == null)
            return failure;
        else if(userRepository.existsByUsername(user.getUsername())){
            return "Username is already taken.";
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
        if (user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    public String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        friendRepo.deleteById(id);
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
        if (user != null) {
            return user.getBio();
        } else {
            return "User not found";
        }
    }

    @GetMapping(path="/users/getPoints/{id}")
    Leaderboard lb (@PathVariable int id) {
        User user = userRepository.findById(id);
        return user.getLeaderboard();
    }


    @GetMapping(value = "/images/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageById(@PathVariable String username) throws IOException {
        User user = userRepository.findByUsername(username);
        File imageFile = new File(user.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }

    @PostMapping("/setPfp/{id}")
    public String handleFileUpload(@RequestParam("image") MultipartFile imageFile, @PathVariable int id) {
        try {
            User user = userRepository.findById(id);
            String username = user.getUsername();

            // Create a directory for profile pictures if it doesn't exist
            String profilePicturesDirectory = directory + File.separator + "profile_pictures";
            File profilePicturesDir = new File(profilePicturesDirectory);
            if (!profilePicturesDir.exists()) {
                profilePicturesDir.mkdirs();
            }
            String imageFileName = id + "_" + username + ".jpg";
            File destinationFile = new File(profilePicturesDirectory + File.separator + imageFileName);
            imageFile.transferTo(destinationFile);
            user.setFilePath(destinationFile.getAbsolutePath());
            userRepository.save(user);
            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }



}
