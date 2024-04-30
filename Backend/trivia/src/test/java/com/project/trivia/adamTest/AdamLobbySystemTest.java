package com.project.trivia.adamTest;


import com.project.trivia.Lobby.Lobby;
import com.project.trivia.Lobby.LobbyRepository;
import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class AdamLobbySystemTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    @Autowired
    LobbyRepository lobbyRepo;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void lobbyCreationTest() {
        User user1 = new User("TestAlok", "password123", "aloks@iastate.edu");
        User user2 = new User("TestAlok2", "password456", "aloks@iastate.edu");
        userRepo.save(user1);
        userRepo.save(user2);

        //http of the create lobby
        String testUserEndpoint = "/create/" + user1.getId() + "/3";

        Response response = RestAssured.given().post(testUserEndpoint);

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        try {
            // Parse the response as a JSONObject
            JSONObject returnObj = new JSONObject(returnString);

            assertEquals("TestAlok", returnObj.getString("host"));
            assertEquals(3, returnObj.getInt("roomSize"));
            assertEquals(1, returnObj.getInt("playerCount"));
            assertFalse(returnObj.getBoolean("finished"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Lobby lobby = userRepo.findById(user1.getId()).getLobby();

        testUserEndpoint = "/joinRoom/" + lobby.getId() + "/2";

        //Test to add user to lobby
        response = RestAssured.given().put("/joinRoom/" + lobby.getId() + "/" + user2.getId());

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response.getBody().asString();
        try {
            // Parse the response as a JSONObject
            JSONObject returnObj = new JSONObject(returnString);

            assertEquals("TestAlok", returnObj.getString("host"));
            assertEquals(2, returnObj.getInt("playerCount"));
            assertEquals(userRepo.findById(user2.getId()).getLobby().getId(), returnObj.getLong("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Make sure to remove the user from the table because it should be null
        userRepo.deleteById(user1.getId());
        userRepo.deleteById(user2.getId());

        assertNull(userRepo.findByUsername(user1.getUsername()));
        assertNull(userRepo.findByUsername(user2.getUsername()));

    }

    @Test
    public void leaveTest(){
        User user1 = new User("TestAlok", "password123", "aloks@iastate.edu");
        User user2 = new User("TestAlok2", "password456", "aloks@iastate.edu");
        userRepo.save(user1);
        userRepo.save(user2);


        Response response = RestAssured.given().delete("/leave/1/"+ user1.getId());


        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        userRepo.deleteById(user1.getId());
        userRepo.deleteById(user2.getId());

        assertNull(userRepo.findByUsername(user1.getUsername()));
        assertNull(userRepo.findByUsername(user2.getUsername()));
    }

    @Test
    public void changeHostTest(){
        User user1 = new User("TestAlok", "password123", "aloks@iastate.edu");
        User user2 = new User("TestAlok2", "password456", "aloks@iastate.edu");
        userRepo.save(user1);
        userRepo.save(user2);

        //http of the create lobby
        String testUserEndpoint = "/create/" + user1.getId() + "/3";

        Response response = RestAssured.given().post(testUserEndpoint);

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        Lobby lobby = userRepo.findById(user1.getId()).getLobby();

        response = RestAssured.given().put("/changeHost/"+ lobby.getId() + "/" + user2.getUsername());

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        userRepo.deleteById(user1.getId());
        userRepo.deleteById(user2.getId());

        assertNull(userRepo.findByUsername(user1.getUsername()));
        assertNull(userRepo.findByUsername(user2.getUsername()));
    }


    @Test
    public void lobbyStatusTest(){
        Lobby lobby = new Lobby(4, "TestAlok");
        lobbyRepo.save(lobby);

        Response response = RestAssured.given().put("/gameStatus/1/" + lobby.getId());

        lobby.getRoomSize();
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().put("/gameStatus/1/" + lobby.getId());

        lobbyRepo.delete(lobby);
    }

}
