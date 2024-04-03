package com.project.trivia.Lobby;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
    List<Lobby> getCurrentLobbies() {
        List<Lobby> currentLobbies = new ArrayList<>();
        for (Lobby lobby : getAllLobbies()){
            if (!lobby.getFinished()){
                currentLobbies.add(lobby);
            }
        }
        return currentLobbies;
    }

    @GetMapping(path = "/allLobbies")
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
    public Lobby joinRoom(@PathVariable int userId, @PathVariable int roomId){
        User user = userRepo.findById(userId);
        Lobby lobby = lobbyRepo.findById(roomId);

        if (lobby == null){
            return null;
        }
        if(lobby.getPlayerCount() >= lobby.getRoomSize()){
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "full");
        }

        if(lobby.getFinished()){
            throw new ResponseStatusException(HttpStatus.GONE,"Game has ended no longer able to join");
        }

        lobby.getPlayers().add(user);
        user.setLobby(lobby);
        lobby.setPlayerCount(lobby.getPlayerCount() + 1);

        lobbyRepo.save(lobby);
        userRepo.save(user);

        return lobby;
    }

    @PutMapping(path = "/changeHost/{roomId}/{username}")
    public Lobby changeHost(@PathVariable int roomId, @PathVariable String username){
        Lobby lobby = lobbyRepo.findById(roomId);
        User newHost = userRepo.findByUsername(username);

        if(newHost == null){
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "player doesn't exist");
        } else if(lobby.getFinished()){
            throw new ResponseStatusException(HttpStatus.GONE, "lobby isn't real");
        }

        lobby.setHost(newHost.getUsername());
        lobbyRepo.save(lobby);

        return lobby;
    }

    @DeleteMapping(path = "/leave/{roomId}/{userId}")
    public Lobby leaveRoom (@PathVariable int userId, @PathVariable int roomId){
        User user = userRepo.findById(userId);
        Lobby lobby = lobbyRepo.findById(roomId);

        if (lobby == null){
            return null;
        }

        lobby.getPlayers().remove(user);
        user.setLobby(null);
        lobby.setPlayerCount(lobby.getPlayerCount() - 1);

        if(lobby.getRoomSize() <= 0){
            lobby.setFinished(true);
        }

        lobbyRepo.save(lobby);
        userRepo.save(user);

        return lobby;
    }

    @PutMapping(path = "/gameStatus/{status}/{roomId}")
    public Lobby setStatus(@PathVariable int status, @PathVariable int roomId){
        Lobby lobby = lobbyRepo.findById(roomId);
        switch (status){
            case 0:
                lobby.setFinished(false);
            case 1:
                lobby.setFinished(true);
        }

        lobbyRepo.save(lobby);

        return lobby;

    }

    @GetMapping(path = "/inRoom/{id}")
    public List<User> getInRoom(@PathVariable int id){
        return lobbyRepo.findById(id).getPlayers();
    }

}
