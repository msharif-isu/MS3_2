package com.project.trivia.websocket.Leaderboard;

import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.Leaderboard.LeaderboardRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RestController
@ServerEndpoint("/leaderboard/{username}")
@Component
public class LeaderboardServer {

    private static LeaderboardRepository leaderboardRepository;
    @Autowired
    public void setLeaderboardRepository(LeaderboardRepository repository) {leaderboardRepository = repository;}

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map <Session, String> sessionUsernameMap = new Hashtable <>();
    private static Map <String, Session> usernameSessionMap = new Hashtable <>();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(LeaderboardServer.class);
    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        // server side log
        logger.info("[onOpen] " + username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        }
        else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);

            // send to the user joining in
            sendLeaderboardData(sessionUsernameMap.get(session));
        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        if (username != null) {
            // server side log
            logger.info("[onClose] " + username);

            // remove user from memory mappings
            sessionUsernameMap.remove(session);
            usernameSessionMap.remove(username);
        }
    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message: " + message);
    }

    /**
     * Sends the leaderboard data to a specific client
     *
     * @param username The username of the recipient.
     */
    private void sendLeaderboardData(String username) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(getLeaderboardData());
            logger.info("Sent leaderboard data to " + username);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }


    @GetMapping(path = "/leaderboard/update")
    /**
     * Sends leaderboard data to all connected clients
     */
    public void broadcastLeaderboardData() {
        broadcast(getLeaderboardData());
        logger.info("Broadcasted data to all connected users");
    }

    /**
     * Returns the current leaderboard data as a single string
     * @return leaderboard data as a String
     */
    private String getLeaderboardData() {
        List<Leaderboard> leaderboardData = leaderboardRepository.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(!leaderboardData.isEmpty()) {
            for (Leaderboard lb : leaderboardData) {
                sb.append(lb.getId() + " " +
                        lb.getUserPoints() + " " +
                        lb.getWeeklyPoints() + " " +
                        lb.getMonthlyPoints() + " " +
                        lb.getYearlyPoints() + " " +
                        lb.getLifetimePoints()+"\n");
            }
        }

        return sb.toString();
    }

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}
