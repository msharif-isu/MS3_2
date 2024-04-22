package com.example.androidexample;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.androidexample.SignupActivity;
import com.example.androidexample.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class OwaisSystemTest {

    @Rule
    public ActivityScenarioRule<SignupActivity> activityScenarioRule = new ActivityScenarioRule<>(SignupActivity.class);

    private static final String VALID_EMAIL = "alok@alok.com";
    private static final String VALID_USERNAME = "alok2";
    private static final String VALID_PASSWORD = "password";

    private static final String INVALID_EMAIL = "invalid_email";
    private static final String INVALID_PASSWORD = "password1";

    @Test
    public void testPasswordMismatch() {
        // Enter a valid password in the password field
        Espresso.onView(withId(R.id.signup_email_edt)).perform(ViewActions.typeText("alok@alok.com"));
        Espresso.onView(withId(R.id.signup_password_edt)).perform(ViewActions.typeText(VALID_PASSWORD));
        Espresso.closeSoftKeyboard();

        // Enter a mismatched password in the confirm password field
        Espresso.onView(withId(R.id.signup_confirm_edt)).perform(ViewActions.typeText(INVALID_PASSWORD));
        Espresso.closeSoftKeyboard();

        // Click the sign-up button
        Espresso.onView(withId(R.id.signup_signup_btn)).perform(ViewActions.click());

        // Check if the error message is correctly displayed
        Espresso.onView(withId(R.id.signup_password_edt)).check(matches(hasErrorText("Passwords don't match")));
    }

    @Test
    public void testInvalidEmail() {
        Espresso.onView(withId(R.id.signup_email_edt)).perform(ViewActions.typeText(INVALID_EMAIL));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.signup_signup_btn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.signup_email_edt)).check(matches(hasErrorText("Enter a valid email address")));
    }

    @Test
    public void testValidSignup() {
        Espresso.onView(withId(R.id.signup_username_edt)).perform(ViewActions.typeText(VALID_USERNAME));
        Espresso.onView(withId(R.id.signup_password_edt)).perform(ViewActions.typeText(VALID_PASSWORD));
        Espresso.onView(withId(R.id.signup_confirm_edt)).perform(ViewActions.typeText(VALID_PASSWORD));
        Espresso.onView(withId(R.id.signup_email_edt)).perform(ViewActions.typeText(VALID_EMAIL));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.signup_signup_btn)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.login_btn)).check(matches(isDisplayed()));
    }



}
