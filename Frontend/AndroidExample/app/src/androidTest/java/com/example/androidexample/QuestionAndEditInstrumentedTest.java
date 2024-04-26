package com.example.androidexample;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import url.RequestURLs;

@RunWith(AndroidJUnit4.class)
public class QuestionAndEditInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    private static final String VALID_USERNAME = "aloks";
    private static final String VALID_PASSWORD = "password123";

    private List<String> questionTypes = new ArrayList<>();

    private void getQuestionTypes(Context context) {
        JsonArrayRequest questionTypesRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("%s/query/topic", RequestURLs.SERVER_HTTP_URL),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        questionTypes.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                questionTypes.add(jsonArray.get(i).toString());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        questionTypes.addAll(questionTypes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(questionTypesRequest);
    }

    @Before
    public void setup() {
        try {
            rule.getScenario().onActivity(activity -> {
                getQuestionTypes(activity.getApplicationContext());
            });
        } catch (NullPointerException np) {
            onView(withId(R.id.login_username_edt)).perform(typeText(VALID_USERNAME), closeSoftKeyboard());
            onView(withId(R.id.login_password_edt)).perform(typeText(VALID_PASSWORD), closeSoftKeyboard());
            onView(withId(R.id.login_btn)).perform(click());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            rule.getScenario().onActivity(activity -> {
                getQuestionTypes(activity.getApplicationContext());
            });
        }
    }

    @Test
    public void testQuestionTopicFilter() {
        onView(withId(R.id.edit)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Filter by known filter
        Random r = new Random();
        String randomFilter = questionTypes.get(r.nextInt(questionTypes.size()));

        onView(withId(R.id.query_type_search_bar)).perform(typeText(randomFilter), closeSoftKeyboard());
        onView(withId(R.id.query_banner)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Attempt to scroll to an item that contains the special text.
        onView(withId(R.id.query_list))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(randomFilter))
                ));
    }
}

