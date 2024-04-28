package com.project.trivia.MPQuestions;

import java.io.IOException;
import java.util.*;


import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import com.project.trivia.roomChat.MessageRepository;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 * <p>
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 * <p>
 * Example URL: ws://localhost:8080/chat/1/username
 * <p>
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 */
@ServerEndpoint("/chat/{roomID}/{username}")
@Component
public class QuestionSocket {

    private static MessageRepository msgRepo;

    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;  // we are setting the static variable
    }

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private static Map<Long, List<Session>> roomSessionsMap = new HashMap<>();

    private static int randInt = 1;

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(QuestionSocket.class);

    //@Autowired
    //MPQuestionRepository mpqRepo;

    private static QuestionRepository questRepo;

    @Autowired
    public void setQuestionRepository(QuestionRepository repo) {
        questRepo = repo;
    }

    private Map<Long, Queue<Integer>> roomQuestionQueues = new HashMap<>();

    private static AnswerRepository ansRepo;

    @Autowired
    public void setAnswerRepository(AnswerRepository repo) {
        ansRepo = repo;
    }

    //private static UserRepository userRepo;
    // @Autowired
    // private void setUserRepository(UserRepository repo) {userRepo = repo;}

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session  represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(@PathParam("roomID") Long id, Session session, @PathParam("username") String username) throws IOException {

        // server side log
        logger.info("[onOpen] " + username);

        // Handle the case of a duplicate username
//        if (usernameSessionMap.containsKey(username)) {
//            session.getBasicRemote().sendText("Username already exists. Please choose a different username.");
//            return;
//        }
//        else {
        // map current session with username
        sessionUsernameMap.put(session, username);

        // map current username with session
        usernameSessionMap.put(username, session);

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

        // so anyone who joins sees the question
        showMessageOne(username);
//        }
    }

    @OnMessage
    public void onMessage(@PathParam("roomID") Long id, Session session, String message) throws IOException {
        String username = sessionUsernameMap.get(session);
        logger.info("[onMessage] " + username + ": " + message);

        if (message.startsWith("/question ")) {
            // Initialize the question queue for the room
            Queue<Integer> questionQueue = new LinkedList<>();
            String questionIdsString = message.substring("/question ".length());
            String[] questionIds = questionIdsString.split("-");
            for (String questionId : questionIds) {
                try {
                    questionQueue.offer(Integer.parseInt(questionId));
                } catch (NumberFormatException e) {
                    // Handle invalid question ID format
                }
            }
            roomQuestionQueues.put(id, questionQueue);
            sendNextQuestionInRoom(id);
        } else {
            // Check if it's the correct answer to the current question
            Queue<Integer> questionQueue = roomQuestionQueues.get(id);
            if (questionQueue != null && !questionQueue.isEmpty()) {
                int currentQuestionId = questionQueue.peek();
                Question currentQuestion = questRepo.findById(currentQuestionId);
                if (currentQuestion != null && message.equalsIgnoreCase(currentQuestion.getAnswer())) {
                    broadcastToRoom(id, "Correct!");
                    questionQueue.poll(); // Remove the answered question
                    sendNextQuestionInRoom(id);
                } else {
                    broadcastToRoom(id, "Incorrect. Try again!");
                }
            } else {
                // Handle the case where there's no question queue or it's empty
                broadcastToRoom(id, message);

            }
        }
    }

    private void sendNextQuestionInRoom(Long roomId) {
        Queue<Integer> questionQueue = roomQuestionQueues.get(roomId);
        if (questionQueue != null && !questionQueue.isEmpty()) {
            int nextQuestionId = questionQueue.peek();
            Question nextQuestion = questRepo.findById(nextQuestionId);
            if (nextQuestion != null) {
                broadcastToRoom(roomId, "Question: " + nextQuestion.getQuestion());
            } else {
                // Handle the case where the question ID is invalid
            }
        } else {
            broadcastToRoom(roomId, "All questions answered!");
            roomQuestionQueues.remove(roomId); // Remove the queue when done
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

//    /**
//     * Broadcasts a message to all users in the chat.
//     *
//     * @param message The message to be broadcasted to all users.
//     */
//    private void broadcast(String message) {
//        sessionUsernameMap.forEach((session, username) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                logger.info("[Broadcast Exception] " + e.getMessage());
//            }
//        });
//    }

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

    private void showMessageEveryone(long roomID) {
        String mpQuestion = "Question: " + questRepo.findById(randInt).getQuestion();
        broadcastToRoom(roomID, mpQuestion);
    }

    private void showMessageOne(String username) {
        String mpQuestion = "Question: " + questRepo.findById(randInt).getQuestion();
        sendMessageToPArticularUser(username, mpQuestion);
    }

    private void randomize() {
        long amount = questRepo.count();
        randInt = (int) ((Math.random() * amount) + 1);
        while (questRepo.findById(randInt).getUsed()) {
            randomize();

        }
    }

    private void resetUseValue() {
        List<Question> allQuestion = questRepo.findAll();
        for (int i = 1; i < allQuestion.size() + 1; i++) {
            Question localQuestRepo = questRepo.findById(i);
            localQuestRepo.setUsed(false);
            questRepo.save(localQuestRepo);
        }
    }

    private boolean allQuestionsUsed() {
        List<Question> allQuestion = questRepo.findAll();
        boolean allUsed = true;
        for (int i = 1; i < allQuestion.size() + 1; i++) {
            Question localQuestRepo = questRepo.findById(i);
            if (!localQuestRepo.getUsed()) {
                allUsed = false;
            }
        }
        return allUsed;
    }


}