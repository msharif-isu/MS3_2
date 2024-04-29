package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SinglePlayerTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);
    private static final String VALID_USERNAME = "test";
    private static final String VALID_PASSWORD = "password123";
    private static final String WRONG_ANSWER = "test";

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
    public void testSingleplayer() throws InterruptedException {
        onView(withId(R.id.singlePlayerButton)).perform(click());
        onView(withId(R.id.singlePlayerButton)).perform(click());
        Thread.sleep(1000);

        for (int i = 0; i < 3; i++) {
            onView(withId(R.id.answer_box)).perform(typeText(WRONG_ANSWER), closeSoftKeyboard());
            onView(withId(R.id.submit_button)).perform(click());
            Thread.sleep(500);
        }

        onView(withId(R.id.numberOfCorrectQuestions)).check(matches(withText("Total points: 0")));
    }

    @Test
    public void testBackToMainMenu() throws InterruptedException {
        onView(withId(R.id.singlePlayerButton)).perform(click());
        onView(withId(R.id.singlePlayerButton)).perform(click());
        Thread.sleep(1000);

        for (int i = 0; i < 3; i++) {
            onView(withId(R.id.answer_box)).perform(typeText(WRONG_ANSWER), closeSoftKeyboard());
            onView(withId(R.id.submit_button)).perform(click());
            Thread.sleep(500);
        }

        Thread.sleep(500);
        onView(withId(R.id.button)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.play)).check(matches(isDisplayed()));
    }
}
