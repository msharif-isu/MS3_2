package com.project.trivia.Lobby;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    @Autowired
    LobbyRepository lobbyRepo;
    @Autowired
    UserRepository userRepo;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/lobbies")
    List<Lobby> getAllLobbies() {
        return lobbyRepo.findAll();
    }

    @GetMapping(path = "/lobbies/{id}")
    public Lobby getLobbyById(@PathVariable int id){
        return lobbyRepo.findById(id);
    }
    @PostMapping(path = "/create/{userId}/{roomSize}")
    public Lobby createRoom(@PathVariable int  userId, @PathVariable int roomSize){
        User host = userRepo.findById(userId);
        Lobby lobby = new Lobby(roomSize, host.getUsername());
        lobby.getPlayers().add(host);
        host.setLobby(lobby);
        lobbyRepo.save(lobby);
        userRepo.save(host);
        return lobby;
    }

    @PutMapping(path = "/joinRoom/{roomId}/{userId}")
    public String joinRoom(@PathVariable int userId, @PathVariable int roomId){
        User user = userRepo.findById(userId);
        Lobby lobby = lobbyRepo.findById(roomId);

        lobby.getPlayers().add(user);
        user.setLobby(lobby);
        lobbyRepo.save(lobby);
        userRepo.save(user);

        return success;
    }

    @GetMapping(path = "/inRoom/{id}")
    public List<User> getInRoom(@PathVariable int id){
        return lobbyRepo.findById(id).getPlayers();
    }

}
