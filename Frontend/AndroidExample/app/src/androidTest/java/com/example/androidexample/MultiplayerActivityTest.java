package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.slider.Slider;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.JVM)
public class MultiplayerActivityTest {

    public static ViewAction setValue(float value) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return IsInstanceOf.instanceOf(Slider.class);
            }

            @Override
            public String getDescription() {
                return "Set Slider value to " + value;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Slider slider = (Slider) view;
                slider.setValue(value);
            }
        };
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);
    private static final String VALID_USERNAME = "test";
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

    @Test(expected = PerformException.class)
    public void testDeletingEmptyRoom() {
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

        onView(withId(R.id.buttonLeaveRoom)).perform(click());
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check to see if lobby was destroyed
        onView(withId(R.id.lobbyList))
                .perform(RecyclerViewActions.scrollTo(hasDescendant((withText(VALID_USERNAME)))));
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
        onView(withId(R.id.buttonStartGame)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.editQuestionCatagory)).perform(typeText("DEBUG"),closeSoftKeyboard());
        onView(withId(R.id.numQuestionsSlider)).perform(setValue(5.0F));
        onView(withId(R.id.buttonStartGame)).perform(click());
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check if question has been loaded
        onView(withId(R.id.question)).check(matches(not(withText(""))));
    }
}
