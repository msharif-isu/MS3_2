package com.example.androidexample;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import url.RequestURLs;

@RunWith(AndroidJUnit4.class)
public class AlokSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> rule = new ActivityScenarioRule<>(LoginActivity.class);
    private static final String VALID_USERNAME = "aloks";
    private static final String VALID_PASSWORD = "password123";

    @Before
    public void setup() {
        login();
    }

    private void login() {
        onView(withId(R.id.login_username_edt)).perform(typeText(VALID_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText(VALID_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.login_btn)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void openQuestion() {
        onView(withId(R.id.jeopardyButton)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Click a 100 point question
        onView(withId(R.id.jeopardy_button_00)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJeopardyBlankAnswer() {
        openQuestion();

        onView(withId(R.id.jeopardy_dialog_submit_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If the dialog is still there, the answer wasn't submitted
        onView(withId(R.id.jeopardy_dialog_submit_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testJeopardyIncorrectAnswer() {
        openQuestion();

        // Type in incorrect answer and submit
        onView(withId(R.id.jeopardy_dialog_answer)).perform(typeText("Clearly wrong answer"), closeSoftKeyboard());
        onView(withId(R.id.jeopardy_dialog_submit_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check that question can't be selected again and that player didn't get points
        onView(withId(R.id.jeopardy_button_00)).check(matches(isNotEnabled()));
        onView(withId(R.id.jeopardy_point_total_text)).check(matches(withText("Total Points: 0")));
    }

    @Test
    public void testPartialNewQuestion() {
        onView(withId(R.id.edit)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.query_add_question_button)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.question_submission_question_input)).perform(typeText("Who's the greatest?"), closeSoftKeyboard());
        onView(withId(R.id.question_submission_answer_input)).perform(typeText("theREALalok"), closeSoftKeyboard());
        onView(withId(R.id.question_submission_submit_button)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If any of the fields are still left, the question wasn't submitted
        onView(withId(R.id.question_submission_question_input)).check(matches(withText("Who's the greatest?")));
    }

    @Test
    public void testFilledNewQuestion() {
        onView(withId(R.id.edit)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.query_add_question_button)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.question_submission_question_input)).perform(typeText("Who's the greatest?"), closeSoftKeyboard());
        onView(withId(R.id.question_submission_answer_input)).perform(typeText("theREALalok"), closeSoftKeyboard());
        onView(withId(R.id.question_submission_question_type_input)).perform(typeText("DEBUG"), closeSoftKeyboard());
        onView(withId(R.id.question_submission_submit_button)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If all the fields are empty, the question was submitted
        onView(withId(R.id.question_submission_question_input)).check(matches(withText("")));
        onView(withId(R.id.question_submission_answer_input)).check(matches(withText("")));
        onView(withId(R.id.question_submission_question_type_input)).check(matches(withText("")));
    }
}

