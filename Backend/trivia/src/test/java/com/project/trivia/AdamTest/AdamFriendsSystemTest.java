package com.project.trivia.AdamTest;


import com.project.trivia.FriendsList.Friends;
import com.project.trivia.FriendsList.FriendsRepository;
import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import com.project.trivia.UserStats.UserStats;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class AdamFriendsSystemTest {
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


    @Test
    public void addingFriendsTest() {
        String user1 = "{" +
                "\"username\":\"TestAlok\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";

        String user2 = "{" +
                "\"username\":\"TestAlok2\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";
        //Creates users
        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user1).
                when().
                post("/users");

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user2).
                when().
                post("/users");

        statusCode = response.statusCode();
        assertEquals(200, statusCode);


        response = RestAssured.given().post("/" + userRepo.findByUsername("TestAlok").getId() +"/addFriend/TestAlok2");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        assertEquals("TestAlok is now friends with TestAlok2", returnString);


        //Test for adding the same friend
        response = RestAssured.given().post("/" + userRepo.findByUsername("TestAlok").getId() +"/addFriend/TestAlok2");
        statusCode = response.statusCode();
        assertEquals(200, statusCode);

        returnString = response.getBody().asString();
        assertEquals("User is already friends with this friend.", returnString);

        //Test for if one of the two users don't exists
        response = RestAssured.given().post("/" + userRepo.findByUsername("TestAlok").getId() +"/addFriend/TestAlok9873264");
        statusCode = response.statusCode();
        assertEquals(200, statusCode);

        returnString = response.getBody().asString();
        assertEquals("User or friend not found.", returnString);

        userRepo.deleteById(userRepo.findByUsername("TestAlok").getId());
        userRepo.deleteById(userRepo.findByUsername("TestAlok2").getId());
        friendsRepo.deleteById(friendsRepo.findByUsername("TestAlok").getId());
        friendsRepo.deleteById(friendsRepo.findByUsername("TestAlok2").getId());
    }

    @Test
    public void removeFriendsTest() {
        String user1 = "{" +
                "\"username\":\"TestAlok\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";

        String user2 = "{" +
                "\"username\":\"TestAlok2\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";
        //Creates users
        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user1).
                when().
                post("/users");

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user2).
                when().
                post("/users");

        statusCode = response.statusCode();
        assertEquals(200, statusCode);

        //Adding each user as friends
        response = RestAssured.given().post("/" + userRepo.findByUsername("TestAlok").getId() +"/addFriend/TestAlok2");

        statusCode = response.statusCode();
        assertEquals(200, statusCode);


        response = RestAssured.given().delete("/" + userRepo.findByUsername("TestAlok").getId() +"/removeFriend/TestAlok2");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        assertEquals("TestAlok and TestAlok2 are no longer friends ", returnString);


        //Test for adding the same friend
        response = RestAssured.given().delete("/" + userRepo.findByUsername("TestAlok").getId() +"/removeFriend/TestAlok2");
        statusCode = response.statusCode();
        assertEquals(200, statusCode);

        returnString = response.getBody().asString();
        assertEquals("TestAlok is not friends with TestAlok2", returnString);

        //Test for if one of the two users don't exists
        response = RestAssured.given().delete("/" + userRepo.findByUsername("TestAlok").getId() +"/removeFriend/TestAlok9873264");
        statusCode = response.statusCode();
        assertEquals(200, statusCode);

        returnString = response.getBody().asString();
        assertEquals("User or friend not found.", returnString);

        userRepo.deleteById(userRepo.findByUsername("TestAlok").getId());
        userRepo.deleteById(userRepo.findByUsername("TestAlok2").getId());
        friendsRepo.deleteById(friendsRepo.findByUsername("TestAlok").getId());
        friendsRepo.deleteById(friendsRepo.findByUsername("TestAlok2").getId());
    }

    @Test
    public void friendsListRepo() {
        String user1 = "{" +
                "\"username\":\"TestAlok\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";

        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user1).
                when().
                post("/users");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                get("/friendsList/" + userRepo.findByUsername("TestAlok").getId());


        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                get("/friends/" + userRepo.findByUsername("TestAlok").getId());

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                get("/allPeople");
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);




        userRepo.deleteById(userRepo.findByUsername("TestAlok").getId());
        friendsRepo.deleteById(friendsRepo.findByUsername("TestAlok").getId());

    }



}
