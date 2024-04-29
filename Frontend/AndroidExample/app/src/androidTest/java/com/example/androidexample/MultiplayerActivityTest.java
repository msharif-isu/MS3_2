package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MultiplayerActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);
    private static final String VALID_USERNAME = "aloks";
    private static final String VALID_PASSWORD = "password123";

    @Before
    public void login() {
        onView(withId(R.id.login_username_edt)).perform(typeText(VALID_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText(VALID_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.login_btn)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deletingEmptyRoom() {
        onView(withId(R.id.multiPlayerButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.createLobbyButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.createLobby)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.buttonLeaveRoom)).perform();
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEnteringRoomSolo() {
        onView(withId(R.id.multiPlayerButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.createLobbyButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.createLobby)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.editQuestionCatagory)).perform(typeText("DEBUG"),closeSoftKeyboard());
        onView(withId(R.id.buttonStartGame)).perform(click());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check if question has been loaded
        onView(withId(R.id.question)).check(matches(not(withText(""))));
    }
}
