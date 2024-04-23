package com.project.trivia;


import com.project.trivia.User.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class AdamSystemTest {
    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testFindById(){
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

    @Test
    public void addingUserTest(){
        String user = "{" +
                "\"username\":\"TestAlok\", " +
                "\"password\":\"password456\", " +
                "\"email\":\"aloks@iastate.edu\"" +
                "}";
        Response response = RestAssured.given().
                contentType(ContentType.JSON).
                body(user).
                when().
                post("/users");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }

}
