package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import url.RequestURLs;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private EditText emailEditText;
    private Button loginButton;
    private Button signupButton;

    private String backendUrl = RequestURLs.SERVER_HTTP_URL + "/users";
    //change to "http://coms-309-034.class.las.iastate.edu:8080/users/"
    //or to "http://10.0.2.2:8080/users"
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);

        usernameEditText = findViewById(R.id.signup_username_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        confirmEditText = findViewById(R.id.signup_confirm_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);

        requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();
                if (isValidEmail(email)) {
                    emailEditText.setError(null);
                    if (password.equals(confirm)) {
                        Toast.makeText(getApplicationContext(), "Signing up", Toast.LENGTH_LONG).show();
                        sendSignupRequest(username, password, email);
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                        passwordEditText.setError("Passwords don't match");
                    }
                } else {
                    emailEditText.setError("Enter a valid email address");
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendSignupRequest(final String username, final String password, final String email) {
        String url = backendUrl;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if ("success".equals(message)) {
                                Toast.makeText(getApplicationContext(), "Signup successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                handleErrorResponse(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Unexpected response format", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplicationContext(), "Signup failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);
    }

    private void handleErrorResponse(String message) {
        if ("Username already exists".equals(message)) {
            Toast.makeText(getApplicationContext(), "Signup failed: " + message, Toast.LENGTH_SHORT).show();
            usernameEditText.setError("Username already exists");
        } else {
            Toast.makeText(getApplicationContext(), "Signup failed: " + message, Toast.LENGTH_SHORT).show();
        }
    }
}
