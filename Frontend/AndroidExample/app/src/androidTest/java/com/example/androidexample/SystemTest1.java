package com.example.androidexample;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SystemTest1 {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    private static final String VALID_USERNAME = "aloks";
    private static final String VALID_PASSWORD = "password123";
    private static final int VALID_USER_ID = 1;

    @Before
    public void login() {
        rule.getScenario().onActivity(activity -> {
            SharedPreferences prefs = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            if (prefs.getString("USERNAME", "").isEmpty()) {
                onView(withId(R.id.login_username_edt)).perform(typeText(VALID_USERNAME), closeSoftKeyboard());
                onView(withId(R.id.login_password_edt)).perform(typeText(VALID_PASSWORD), closeSoftKeyboard());
                onView(withId(R.id.login_btn)).perform(click());
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    @Test
    public void checkLeaderboard() {
        onView(withId(R.id.leaderboard)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.leaderboard_banner)).check(matches(isDisplayed()));
    }
    @Test
    public void jeopardyTest() {
        onView(withId(R.id.jeopardyButton)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.jeopardy_button_00)).perform(click());
        onView(withId(R.layout.jeopardy_question_dialog)).check(matches(isDisplayed()));
    }
}

