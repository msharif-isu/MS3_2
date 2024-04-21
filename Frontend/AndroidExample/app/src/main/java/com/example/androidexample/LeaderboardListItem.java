package com.example.androidexample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A class to represent a user's points in the leaderboard list
 */
public class LeaderboardListItem implements Serializable {
    /**
     * Username
     */
    private String username;

    /**
     * HashMap representing daily, weekly, monthly, yearly, and lifetime points
     */
    private HashMap<LeaderboardTimeFrameEnum, Integer> points;

    /**
     * Constructs a list item to store user points to display
     * @param username - user name
     * @param points - A HashMap of <LeaderboardTimeFrameEnum, Integer> to represent
     *               daily, weekly, monthly, yearly, and lifetime points
     */
    public LeaderboardListItem(String username, HashMap<LeaderboardTimeFrameEnum, Integer> points) {
        this.username = username;
        this.points = points;
    }

    /**
     * Returns the daily points of this user
     * @return daily points
     * @throws NullPointerException - daily points weren't set
     */
    public int getDailyPoints() throws NullPointerException{
        return points.get(LeaderboardTimeFrameEnum.DAILY);
    }

    /**
     * Returns the weekly points of this user
     * @return weekly points
     * @throws NullPointerException - weekly points weren't set
     */
    public int getWeeklyPoints() throws NullPointerException{
        return points.get(LeaderboardTimeFrameEnum.WEEKLY);
    }

    /**
     * Returns the monthly points of this user
     * @return monthly points
     * @throws NullPointerException - monthly points weren't set
     */
    public int getMonthlyPoints() throws NullPointerException{
        return points.get(LeaderboardTimeFrameEnum.MONTHLY);
    }


    /**
     * Returns the yearly points of this user
     * @return yearly points
     * @throws NullPointerException - yearly points weren't set
     */
    public int getYearlyPoints() throws NullPointerException{
        return points.get(LeaderboardTimeFrameEnum.YEARLY);
    }

    /**
     * Returns the lifetime points of this user
     * @return yearly points
     * @throws NullPointerException - lifetime points weren't set
     */
    public int getLifetimePoints() throws NullPointerException{
        return points.get(LeaderboardTimeFrameEnum.LIFETIME);
    }

    /**
     * Returns the username of this user
     * @return user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Parses a WebSocket message into the appropriate list of LeaderboardListItems
     * @param message - WebSocket message
     * @return list of LeaderboardListItems
     */
    public static List<LeaderboardListItem> parseLeaderboardMessage(String message) {
        Scanner scnr = new Scanner(message);

        ArrayList<LeaderboardListItem> data = new ArrayList<>();

        while(scnr.hasNextLine()) {
            String lbString = scnr.nextLine();
            Scanner parser = new Scanner(lbString);

            String username = parser.next();

            HashMap<LeaderboardTimeFrameEnum, Integer> points = new HashMap<>();

            points.put(LeaderboardTimeFrameEnum.DAILY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.WEEKLY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.MONTHLY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.YEARLY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.LIFETIME, Integer.parseInt(parser.next()));
            parser.close();

            LeaderboardListItem item = new LeaderboardListItem(username, points);
            data.add(item);
        }
        scnr.close();

        return data;
    }
}
