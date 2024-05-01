package url;

/**
 * A helper class that stores all of the URLs used in this app.
 */
public class RequestURLs {
    /**
     * The URL to be used to refer to any HTTP requests made to the VM server.
     * Change only this to switch to server from localhost.
     */
    private static final String SERVER_BASE_URL = "://coms-309-034.class.las.iastate.edu:8080";
//    private static final String SERVER_BASE_URL = "://10.0.2.2:8080";
    public static final String SERVER_HTTP_URL = "http" + SERVER_BASE_URL;
    public static final String SERVER_HTTP_USER_QUESTION_URL = SERVER_HTTP_URL + "/question";
    public static final String SERVER_HTTP_QUESTION_URL = SERVER_HTTP_URL + "/question";
    public static final String SERVER_HTTP_QUESTION_QUERY_URL = SERVER_HTTP_URL + "/query";
    public static final String SERVER_HTTP_ANSWER_CHECKER_URL = SERVER_HTTP_URL + "/question/answers";
    public static final String SERVER_HTTP_CREATE_USER_ANSWER_URL = SERVER_HTTP_URL + "/answer/add";
    public static final String SERVER_HTTP_USER_ADD_POINTS_URL = SERVER_HTTP_URL + "/leaderboard/addpoints";

    /**
     * The URL to be used to refer to any WebSocket requests made to the VM server
     */
    public static final String SERVER_WEBSOCKET_URL = "ws" + SERVER_BASE_URL;
    public static final String SERVER_WEBSOCKET_URL_MULTIPLAYER = "ws" + SERVER_BASE_URL;
    //"ws://10.0.2.2:8080";
    //"ws://coms-309-034.class.las.iastate.edu:8080";
    public static final String SERVER_WEBSOCKET_LEADERBOARD_URL =  SERVER_WEBSOCKET_URL + "/leaderboard";
}
