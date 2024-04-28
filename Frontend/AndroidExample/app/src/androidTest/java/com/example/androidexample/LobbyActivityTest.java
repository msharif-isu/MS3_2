package com.example.androidexample;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import static androidx.test.espresso.Espresso.*;

import androidx.test.espresso.action.*;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LobbyActivityTest {

    @Rule
    public ActivityScenarioRule<LobbiesActivity> activityScenarioRule = new ActivityScenarioRule<>(LobbiesActivity.class);
    private static final String VALID_USERNAME = "aloks";
    private static final int VALID_USER_ID = 1;

    @Before
    public void setUp() {
        // Set up SharedPreferences with a valid username and user ID
        ActivityScenario<LobbiesActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
    }

    @Test
    public void testSliderUpdatesLobbySize() {
        onView(withId(R.id.createLobbyButton)).perform(click());
        setSliderProgress(R.id.slider);
        onView(withId(R.id.lobbySize)).check(matches(withText("10")));
    }

    private static void setSliderProgress(int sliderId) {
        onView(withId(sliderId)).perform(new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT, Press.FINGER));
    }

    @Test
    public void testCreateLobby() {
        onView(withId(R.id.createLobbyButton)).perform(click());
        setSliderProgress(R.id.slider);
        onView(withId(R.id.createLobby)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycleView)).check(matches(isDisplayed()));
    }

    @Test
    public void testJoinLobby() {
        onView(withId(R.id.lobbyList)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        0, RecycleViewAction.clickChildViewWithId(R.id.button2)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycleView)).check(matches(isDisplayed()));
    }

    @Test
    public void testLeaveLobby() {
    }

    @Test
    public void testStartGame() {
    }

}