package com.project.trivia.roomChat;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@ServerEndpoint("/chat/{roomID}/{username}")
@Component
public class Chat {

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    // Map roomID to sessions
    private static Map<Long, List<Session>> roomSessionsMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(Chat.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session  represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     * @param id       the id the room that the user is trying to join
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("roomID") Long id) throws IOException {

        // server side log
        logger.info("[onOpen] " + username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
            return;
        }

        // map current session with username
        sessionUsernameMap.put(session, username);

        // map current username with session
        usernameSessionMap.put(username, session);

        // add session to room
        List<Session> sessionsInRoom = roomSessionsMap.get(id);
        if (sessionsInRoom == null) {
            sessionsInRoom = new ArrayList<>();
            roomSessionsMap.put(id, sessionsInRoom);
        }
        sessionsInRoom.add(session);

        // send to the user joining in
        sendMessageToPArticularUser(username, "Welcome to room " + id + " " + username);

        // send to everyone in the chat
        broadcastToRoom(id, "User: " + username + " has Joined room " + id);
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session, String message, @PathParam("roomID") Long id) throws IOException {

        // get the username by session
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onMessage] " + username + ": " + message);

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {

            // split by space
            String[] split_msg = message.split("\\s+");

            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }
            String destUserName = split_msg[0].substring(1);    //@username and get rid of @
            String actualMessage = actualMessageBuilder.toString();
            sendMessageToPArticularUser(destUserName, "[DM from " + username + "]: " + actualMessage);
            sendMessageToPArticularUser(username, "[DM to " + destUserName + "]: " + actualMessage);
        } else { // Message to whole chat
            broadcastToRoom(id, username + ": " + message);
        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session, @PathParam("roomID") Long id) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // remove session from room sessions map
        List<Session> sessionsInRoom = roomSessionsMap.get(id);
        if (sessionsInRoom != null) {
            sessionsInRoom.remove(session);
            if (sessionsInRoom.isEmpty()) {
                roomSessionsMap.remove(id);
            }
        }

        // send the message to chat
        broadcastToRoom(id, username + " disconnected");
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
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToPArticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
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

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users in room.
     * @param roomId  The id of the room where the message is being sent.
     */
    private void broadcastToRoom(Long roomId, String message) {
        List<Session> sessionsInRoom = roomSessionsMap.get(roomId);
        if (sessionsInRoom != null) {
            for (Session session : sessionsInRoom) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.info("[Broadcast Exception] " + e.getMessage());
                }
            }
        }
    }
}




