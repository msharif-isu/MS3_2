package com.project.trivia;


import com.project.trivia.FriendsList.FriendsRepository;
import com.project.trivia.Lobby.Lobby;
import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class AdamSystemTest {
    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    @Autowired
    FriendsRepository friendsRepo;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }


    //Not Trivial Test
    @Test
    public void addingUserTest() {
        //user made for testing
        String user = "{" +
                "\"username\":\"TestAlok\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";

        //Send request and get response
        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user).
                when().
                post("/users");

        //Chcek status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        //Check response body should be success
        String returnString = response.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString);

        // getting user from table after saving
        User testUser = userRepo.findByUsername("TestAlok");

        //Checking response to make sure you cannot add users with the same username
        response = RestAssured.given().
                contentType(ContentType.JSON).
                body(testUser).
                when().
                post("/users");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        //Shouuld fail since alok is already in the table
        returnString = response.getBody().asString();
        assertEquals("{\"message\":\"failure\"}", returnString);

        //Make sure to remove the user from the table because it should be null
        userRepo.deleteById(userRepo.findByUsername("TestAlok").getId());
        friendsRepo.deleteById(friendsRepo.findByUsername("TestAlok").getId());
        assertNull(userRepo.findByUsername("TestAlok"));
        assertNull(friendsRepo.findByUsername("TestAlok"));


        //Check to make sure you can't add an empty user
        user = "{}";
        response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user).
                when().
                post("/users");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response.getBody().asString();
        assertEquals("{\"message\":\"failure\"}", returnString);

    }

    @Test
    public void updatingUserBioTest() {
        //User to update bio of
        String user = "{" +
                "\"username\":\"TestAlok\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";
        //Creates user
        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user).
                when().
                post("/users");

        //Status response
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        //set bio of user
        response = RestAssured.given().
                put("/editBio/TestAlok/this is a test bio");

        //Set of bio of the test user
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        //Checks to make sure the test bio is correct
        assertEquals("this is a test bio", userRepo.findByUsername("TestAlok").getBio());

        //delete the user from table
        userRepo.deleteById(userRepo.findByUsername("TestAlok").getId());
        friendsRepo.deleteById(friendsRepo.findByUsername("TestAlok").getId());
        assertNull(userRepo.findByUsername("TestAlok"));
        assertNull(friendsRepo.findByUsername("TestAlok"));

        //Put to make sure that the user is empty and should return null
        response = RestAssured.given().
                put("/editBio/TestAlok/this is a test bio");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assertNull(userRepo.findByUsername("TestAlok"));

    }

    @Test
    public void updatingUserInfoTest() {
        //Create new test user to update
        User user = new User("TestAlok", "password456", "aloks@iastate.edu");
        userRepo.save(user);

        //Id of test user
        int userId = userRepo.findByUsername("TestAlok").getId();

        //New username to change
        String newUserInfo = "{" +
                "\"username\":\"alokTest\", " +
                "\"password\":\"password123\", " +
                "\"email\":\"osamman@iastate.edu\"" +
                "}";

        //http of put test user with id
        String testUserEndpoint = "/users/" + userId;

        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(newUserInfo).
                when().
                put(testUserEndpoint);

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        try {
            // Parse the response as a JSONObject
            JSONObject returnObj = new JSONObject(returnString);

            // Access the username directly from the JSONObject
            assertEquals("alokTest", returnObj.getString("username"));
            assertEquals("password123", returnObj.getString("password"));
            assertEquals("osamman@iastate.edu", returnObj.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Test To check that you cannot change username to an existing username
        response = RestAssured.given().
                contentType(ContentType.JSON).
                body("{" +
                        "\"username\":\"alokTest\"" +
                        "}").
                when().
                put(testUserEndpoint);

        //Throws a I_AM_A_TEAPOT reponse since that is what I used to show the error that the username is taken
        statusCode = response.getStatusCode();
        assertEquals(418, statusCode);

        //Make sure to remove the user from the table because it should be null
        userRepo.deleteById(userRepo.findByUsername("alokTest").getId());
        assertNull(userRepo.findByUsername("TestAlok"));


        //Checking that you cannot update a user that doesn't exist
        response = RestAssured.given().
                contentType(ContentType.JSON).
                body("{" +
                        "\"username\":\"alokTest\"" +
                        "}").
                when().
                put(testUserEndpoint);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void lobbyCreationTest() {
        User user1 = new User("TestAlok", "password123", "aloks@iastate.edu");
        User user2 = new User("TestAlok2", "password456", "aloks@iastate.edu");
        userRepo.save(user1);
        userRepo.save(user2);

        //http of the create lobby
        String testUserEndpoint = "/create/" + user1.getId() + "/3";

        Response response = RestAssured.given().
                post(testUserEndpoint);

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
        response = RestAssured.given().
                put("/joinRoom/" + lobby.getId() + "/" + user2.getId());

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

    //Trival test
    @Test
    public void testFindById() {
        Response response = RestAssured.given().
                get("/users/1");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        try {
            // Parse the response as a JSONObject
            JSONObject returnObj = new JSONObject(returnString);

            // Access the username directly from the JSONObject
            assertEquals(userRepo.findById(1).getUsername(), returnObj.get("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAll() {
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("hello").
                when().
                get("/users");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }


}
