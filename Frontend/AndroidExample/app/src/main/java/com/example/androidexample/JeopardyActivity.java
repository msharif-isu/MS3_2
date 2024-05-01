package com.example.androidexample;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import url.RequestURLs;

public class JeopardyActivity extends AppCompatActivity {
    private List<List<Button>> buttonColumns;
    private List<Button> questionCategory0Buttons;
    private List<Button> questionCategory1Buttons;
    private List<Button> questionCategory2Buttons;
    private List<TextView> categoryTitles;
    private TextView pointsText;

    private String username;
    private int userID;
    private int points;
    private int questionsAnswered;
    private static final int totalQuestions = 9;
    private boolean attemptedRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeopardy);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("USERNAME","");
        userID = prefs.getInt("USER_ID", 0);

        points = 0;
        questionsAnswered = 0;
        pointsText = findViewById(R.id.jeopardy_point_total_text);

        pointsText.setText("Total Points: " + points);

        buttonColumns = new ArrayList<>();
        questionCategory0Buttons = new ArrayList<>();
        questionCategory1Buttons = new ArrayList<>();
        questionCategory2Buttons = new ArrayList<>();

        categoryTitles = new ArrayList<>();

        Button questionButton00 = findViewById(R.id.jeopardy_button_00);
        Button questionButton10 = findViewById(R.id.jeopardy_button_10);
        Button questionButton20 = findViewById(R.id.jeopardy_button_20);
        Button questionButton01 = findViewById(R.id.jeopardy_button_01);
        Button questionButton11 = findViewById(R.id.jeopardy_button_11);
        Button questionButton21 = findViewById(R.id.jeopardy_button_21);
        Button questionButton02 = findViewById(R.id.jeopardy_button_02);
        Button questionButton12 = findViewById(R.id.jeopardy_button_12);
        Button questionButton22 = findViewById(R.id.jeopardy_button_22);

        questionCategory0Buttons.add(questionButton00);
        questionCategory0Buttons.add(questionButton10);
        questionCategory0Buttons.add(questionButton20);
        questionCategory1Buttons.add(questionButton01);
        questionCategory1Buttons.add(questionButton11);
        questionCategory1Buttons.add(questionButton21);
        questionCategory2Buttons.add(questionButton02);
        questionCategory2Buttons.add(questionButton12);
        questionCategory2Buttons.add(questionButton22);
        buttonColumns.add(questionCategory0Buttons);
        buttonColumns.add(questionCategory1Buttons);
        buttonColumns.add(questionCategory2Buttons);

        TextView category0 = findViewById(R.id.jeopardy_category_0);
        TextView category1 = findViewById(R.id.jeopardy_category_1);
        TextView category2 = findViewById(R.id.jeopardy_category_2);

        categoryTitles.add(category0);
        categoryTitles.add(category1);
        categoryTitles.add(category2);

        Question placeholder = new Question(99, "99?", "69", "Number", false, false);

        // If no questions are loaded yet, show this message
        buttonColumns.forEach(c -> {
            c.forEach(q -> {
                q.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Question hasn't loaded", Toast.LENGTH_SHORT).show();
                        questionDialog(q, placeholder);
                    }
                });
            });
        });

        requestJeopardyQuestions();
    }

    /**
     * Gives the category buttons their corresponding questions
     */
    private void requestJeopardyQuestions() {
        JsonArrayRequest questionRequest = new JsonArrayRequest(
                Request.Method.GET,
                RequestURLs.SERVER_HTTP_QUESTION_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Sort questions into different lists based on question type
                        List<Question> questions = new ArrayList<>();
                        HashSet<String> categories = new HashSet<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Question q = new Question(response.getJSONObject(i));
                                questions.add(q);
                                categories.add(q.getQuestionType());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        List<List<Question>> categoryQuestions = new ArrayList<>();
                        List<String> categoryList = new ArrayList<>(categories);

                        for (int i = 0; i < categoryTitles.size(); i++) {
                            String category = categoryList.get(i);
                            List<Question> temp = new ArrayList<>(questions);
                            temp.removeIf(q -> !q.getQuestionType().equals(category));
                            categoryQuestions.add(temp);
                            categoryTitles.get(i).setText(category);
                        }

                        // Set each button to a random question in their category
                        Random rand = new Random();
                        for (int col = 0; col < buttonColumns.size(); col++) {
                            for (Button b : buttonColumns.get(col)) {
                                int randQuestionIndex = rand.nextInt(categoryQuestions.get(col).size());

                                Question q = categoryQuestions.get(col).get(randQuestionIndex);
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        questionDialog(b, q);
                                    }
                                });
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!attemptedRefresh) {
                            Toast.makeText(getApplicationContext(), "Unable to load questions, reattempting to get questions from server", Toast.LENGTH_LONG).show();
                            requestJeopardyQuestions();
                            attemptedRefresh = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(questionRequest);
    }

    /**
     * Creates a popup dialog to allow users to answer a question
     * @param question
     */
    private void questionDialog(Button button, Question question) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.jeopardy_question_dialog);
        dialog.show();

        TextView questionPoints = dialog.findViewById(R.id.jeopardy_dialog_point_worth);
        TextView questionDisplay = dialog.findViewById(R.id.jeopardy_dialog_question);
        EditText answerText = dialog.findViewById(R.id.jeopardy_dialog_answer);
        Button submitButton = dialog.findViewById(R.id.jeopardy_dialog_submit_button);

        questionPoints.setText(button.getText());
        questionDisplay.setText(question.getQuestion());
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences statsPrefs = getSharedPreferences("UserStatistics", MODE_PRIVATE);
                SharedPreferences.Editor editor = statsPrefs.edit();
                int totalAnswered = statsPrefs.getInt("TOTAL_ANSWERS", 0);
                int gamesPlayed = statsPrefs.getInt("GAMES_PLAYED", 0);
                totalAnswered += 1;
                gamesPlayed += 1;
                editor.putInt("TOTAL_ANSWERS", totalAnswered);
                editor.putInt("GAMES_PLAYED", gamesPlayed);
                editor.apply();

                String answer = answerText.getText().toString();
                if (!answer.trim().isEmpty()) {
                    int earnedPoints = Integer.parseInt(button.getText().toString());
                    checkAnswer(earnedPoints, question, answer);

                    questionsAnswered++;
                    button.setEnabled(false);

                    Log.d("JeopardyActivity", "Questions Answered: " + questionsAnswered);
                    if (questionsAnswered == totalQuestions) {
                        Log.d("JeopardyActivity", "END OF THE GAME");
                        endScreenDialog();
                    }

                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Creates the end screen dialog after all questions are answered
     */
    private void endScreenDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_jeopardy_end_screen);

        TextView pointsText = dialog.findViewById(R.id.dialog_jeopardy_end_screen_points_earned);
        Button playAgain = dialog.findViewById(R.id.dialog_jeopardy_end_screen_play_again_button);
        Button mainMenu = dialog.findViewById(R.id.dialog_jeopardy_end_screen_main_menu_button);

        pointsText.setText("Points Earned: " + points);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JeopardyActivity.this, JeopardyActivity.class));
                finish();
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JeopardyActivity.this, MainActivity.class));
                finish();
            }
        });

        dialog.show();
    }

    /**
     * Saves the current user's answer to the given question
     * @param questionID
     * @param givenAnswer
     */
    private void saveUserAnswer(int questionID, String givenAnswer, boolean correct) throws JSONException {
        JsonObjectRequest saveUserRequest = new JsonObjectRequest(
                Request.Method.POST,
                String.format("%s/%d", RequestURLs.SERVER_HTTP_CREATE_USER_ANSWER_URL, questionID),
                new JSONObject() {{
                    put("userName", username);
                    put("correct", correct);
                    put("answer", givenAnswer);
                }},
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JeopardyActivity", "Successfully added user answer to database");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JeopardyActivity", "Failed to add user answer to database");
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(saveUserRequest);
    }

    /**
     * Checks to see if an answer to a question is correct
     * @param question
     * @param givenAnswer
     */
    private void checkAnswer(int pointsToAdd, Question question, String givenAnswer) {
        if (givenAnswer.equals(question.getAnswer())) {
            points += pointsToAdd;
            pointsText.setText("Total Points: " + points);
            addUserPoints(pointsToAdd);

            Log.d("JeopardyActivity", "Saving correct user answer");
            try {
                saveUserAnswer(question.getId(), givenAnswer, true);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            Log.d("JeopardyActivity", "Saving incorrect user answer");
            try {
                saveUserAnswer(question.getId(), givenAnswer, false);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds the given points to the current user
     * @param pointsToAdd
     */
    private void addUserPoints(int pointsToAdd) {
        JsonObjectRequest addUserPointsRequest = new JsonObjectRequest(
                Request.Method.POST,
                String.format("%s/%s/%d", RequestURLs.SERVER_HTTP_USER_ADD_POINTS_URL, userID, pointsToAdd),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JeopardyActivity", "Successfully added user points");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JeopardyActivity", "Failed to add user points");
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(addUserPointsRequest);
    }
}
