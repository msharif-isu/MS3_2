package url;

/**
 * A helper class that stores all of the URLs used in this app.
 */
public class RequestURLs {
    /**
     * The URL to be used to refer to any HTTP requests made to the VM server
     */
    public static final String SERVER_HTTP_URL = "http://coms-309-034.class.las.iastate.edu:8080";
            //"http://10.0.2.2:8080";

            //"http://coms-309-034.class.las.iastate.edu:8080";
    public static final String SERVER_HTTP_USER_QUESTION_URL = "http://coms-309-034.class.las.iastate.edu:8080/question";
    public static final String SERVER_HTTP_QUESTION_URL = "http://10.0.2.2:8080/question";
    public static final String SERVER_HTTP_QUESTION_QUERY_URL = "http://10.0.2.2:8080/query";
    public static final String SERVER_HTTP_ANSWER_CHECKER_URL = "http://10.0.2.2:8080/question/answers";
    public static final String SERVER_HTTP_CREATE_USER_ANSWER_URL = "http://10.0.2.2:8080/answer/add";
    public static final String SERVER_HTTP_USER_ADD_POINTS_URL = "http://10.0.2.2:8080/users";

    /**
     * The URL to be used to refer to any WebSocket requests made to the VM server
     */
    public static final String SERVER_WEBSOCKET_URL = "ws://10.0.2.2:8080";
    public static final String SERVER_WEBSOCKET_URL_MULTIPLAYER = "ws://coms-309-034.class.las.iastate.edu:8080";
            //"ws://10.0.2.2:8080";
            //"ws://coms-309-034.class.las.iastate.edu:8080";
    public static final String SERVER_WEBSOCKET_LEADERBOARD_URL = "ws://10.0.2.2:8080/leaderboard";
}
