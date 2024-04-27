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
public class JeopardyInstrumentedTest {

    @Rule
    public ActivityScenarioRule<JeopardyActivity> rule = new ActivityScenarioRule<>(JeopardyActivity.class);
    private ArrayList<Question> questions = new ArrayList<>();

    private void getQuestions(Context context) {
        JsonArrayRequest questionRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("%s/question", RequestURLs.SERVER_HTTP_URL),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        questions.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                Question q = new Question(jsonArray.getJSONObject(i));
                                questions.add(q);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        try {
                            throw volleyError;
                        } catch (VolleyError e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(questionRequest);
    }

    @Before
    public void setup() {
        rule.getScenario().onActivity(activity -> {
            getQuestions(activity.getApplicationContext());
        });
    }

    private void openQuestion() {
        try {
            Thread.sleep(1000);
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

//    @Test
//    public void testJeopardyCorrectAnswer() {
//        openQuestion();
//
//        String correctAnswer = "Philosophocles";
//
//        // Type in correct answer and submit
//        onView(withId(R.id.jeopardy_dialog_answer)).perform(typeText(correctAnswer), closeSoftKeyboard());
//        onView(withId(R.id.jeopardy_dialog_submit_button)).perform(click());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Check that question can't be selected again and that player got points
//        onView(withId(R.id.jeopardy_button_00)).check(matches(isNotEnabled()));
//        onView(withId(R.id.jeopardy_point_total_text)).check(matches(withText("Total Points: 100")));
//    }

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
}

