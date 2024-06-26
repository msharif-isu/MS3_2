package com.example.androidexample;

import androidx.test.espresso.Espresso;
import static androidx.test.espresso.Espresso.onView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    private static final String VALID_USERNAME = "aloks";
    private static final String VALID_PASSWORD = "password123";

    private static final String INVALID_USERNAME = "invalid_username";
    private static final String INVALID_PASSWORD = "invalid_password";

    @Test
    public void testValidLogin() {
        onView(withId(R.id.login_username_edt)).perform(ViewActions.typeText(VALID_USERNAME));
        onView(withId(R.id.login_password_edt)).perform(ViewActions.typeText(VALID_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidLogin() {
        onView(withId(R.id.login_username_edt)).perform(ViewActions.typeText(INVALID_USERNAME));
        onView(withId(R.id.login_password_edt)).perform(ViewActions.typeText(INVALID_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login_username_edt)).check(matches(hasErrorText("Invalid username or password.")));
    }

    @Test
    public void testEmptyUsername() {
        onView(withId(R.id.login_password_edt)).perform(ViewActions.typeText(VALID_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        onView(withId(R.id.login_username_edt)).check(matches(hasErrorText("Please type a username.")));
    }

    @Test
    public void testEmptyPassword() {
        onView(withId(R.id.login_username_edt)).perform(ViewActions.typeText(VALID_USERNAME));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_btn)).perform(ViewActions.click());
        onView(withId(R.id.login_password_edt)).check(matches(hasErrorText("Please type a password.")));
    }
}
