package com.example.androidexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;


import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Execute test methods in alphabetical order
public class ProfileFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    private static final String VALID_USERNAME = "aloks";
    private static final int VALID_USER_ID = 1;
    private static final String VALID_BIO = "This is a valid bio";

    private static final String NEW_BIO = "I am the REAL Alok. No other Alok will ever come close to me. I am the one true Alok.";

    private static final String FRIEND_NAME = "aloks2";

//    @Test
//    public void testNavigationToProfileFragment() {
//        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
//        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
//    }

    @Test
    public void testEditBio() {
        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editBioButton)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.bioEdit)).perform(ViewActions.typeText(VALID_BIO));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.userBiography)).check(ViewAssertions.matches(withText(VALID_BIO)));
    }

    @Test
    public void testReEditBio() {
        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editBioButton)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.bioEdit)).perform(ViewActions.typeText(NEW_BIO));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.userBiography)).check(ViewAssertions.matches(withText(NEW_BIO)));
    }

    @Test
    public void testEmptyBio() {
        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editBioButton)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.bioEdit)).perform(ViewActions.clearText());
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.bioEdit)).check(matches(hasErrorText("Please enter a bio.")));
    }

    @Test
    public void testMaxLengthBio() {
        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editBioButton)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.bioEdit)).perform(ViewActions.typeText("I am the only real Alok. No other Alok will ever come close to the level of real that I have become."));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.userBiography)).check(ViewAssertions.matches(withText("I am the only real Alok. No other Alok will ever come close to the level of real that I have become.")));
    }

    @Test
    public void testTooLongBio() {
        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editBioButton)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.bioEdit)).perform(ViewActions.typeText("I am the only real Alok. No other Alok will ever come close to the level of real that I have achieved"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.bioEdit)).check(matches(hasErrorText("Please enter shorter bio. Maximum 100 characters.")));
    }

//    @Test
//    public void testUploadProfilePicture() {
//        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
//        scenario.onActivity(activity -> {
//            Context context = activity.getApplicationContext();
//            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("USERNAME", VALID_USERNAME);
//            editor.putInt("USER_ID", VALID_USER_ID);
//            editor.apply();
//        });
//        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
//        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
//        Espresso.onView(withId(R.id.editProfilePictureButton)).perform(ViewActions.click());
//
//        //TODO IDK HOW TO MAKE THIS WORK
//    }

    @Test
    public void testAddFriend() {
        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.addFriends)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.friendUsernameEdit)).perform(ViewActions.typeText(FRIEND_NAME));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.addFriend)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown());
        Espresso.onView(withId(R.id.friendList))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(FRIEND_NAME))))
                .check(matches(hasDescendant(withText(FRIEND_NAME))));
    }

    @Test
    public void testRemoveFriend() {
        ActivityScenario<MainActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", VALID_USERNAME);
            editor.putInt("USER_ID", VALID_USER_ID);
            editor.apply();
        });
        Espresso.onView(withId(R.id.bottomNavigationView)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.friendList))
                .perform(actionWithAssertions(scrollTo(hasDescendant(withText(FRIEND_NAME)))));
        Espresso.onView(allOf(withId(R.id.friendUsername), withText(FRIEND_NAME))).perform(ViewActions.click());
        Espresso.onView(allOf(withId(R.id.removeFriend), hasSibling(withText(FRIEND_NAME))))
                .perform(ViewActions.click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown());
        Espresso.onView(withText(FRIEND_NAME)).check(ViewAssertions.doesNotExist());
    }

}
