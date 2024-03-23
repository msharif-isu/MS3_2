package com.project.trivia.websocket.Leaderboard;

import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.Leaderboard.LeaderboardRepository;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/leaderboard/{username}")
@Component
public class LeaderboardServer {

    private static LeaderboardRepository leaderboardRepository;
    @Autowired
    public void setLeaderboardRepository(LeaderboardRepository repository) {leaderboardRepository = repository;}

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();

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
            sendLeaderboardData(username);
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

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
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

    /**
     * Sends the leaderboard data to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     */
    private void sendLeaderboardData(String username) {
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

        String message = sb.toString();

        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
            logger.info("Sent leaderboard data to " + username);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }
}
