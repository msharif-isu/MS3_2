    package com.example.androidexample;

    import androidx.appcompat.app.AppCompatActivity;

    import android.util.Log;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.JsonArrayRequest;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;

    import org.json.JSONArray;
    import org.json.JSONException;

    import java.util.ArrayList;

    public class LoginActivity extends AppCompatActivity {

        private EditText usernameEditText;
        private EditText passwordEditText;
        private Button loginButton;
        private Button signupButton;

        private String username;
        private String backendUrl = "http://10.0.2.2:8081/users/";
        //change to "http://coms-309-034.class.las.iastate.edu:8080/users/"

        private SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            usernameEditText = findViewById(R.id.login_username_edt);
            passwordEditText = findViewById(R.id.login_password_edt);
            loginButton = findViewById(R.id.login_btn);
            signupButton = findViewById(R.id.signup_btn);

            sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
            username = sharedPreferences.getString("username", "");

            // Check if user already logged in
            if (sharedPreferences.contains("username")) {
                proceedToNextActivity(username);
            }

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    //For debugging, will remove later.
                    //Toast.makeText(LoginActivity.this, "username: " + username, Toast.LENGTH_SHORT).show();
                    loginRequest(username, password);
                }
            });

            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);  // go to SignupActivity
                }
            });
        }

        /**
         * Handles the login.
         *
         * @param username
         * @param password
         */
        private void loginRequest(final String username, final String password) {
            getUsernameId(username, password);
        }

        /**
         * Handles getting the username's corresponding id from the server, and sends the id to the getPasswordId method
         *
         * @param username
         * @param password
         */
        private void getUsernameId(final String username, final String password) {
            // create the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            // This is the server. This can be changed in case backend is modified.
            String url = backendUrl + "getIdByUsername/" + username;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle successful response
                            int usernameId = Integer.parseInt(response);
                            // I pass it over to the password method, which will compare
                            // the two ids.
                            getPasswordIds(usernameId, password, username);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error response
                    // This will probably be updated to have more specific errors at a later date.
                    Toast.makeText(LoginActivity.this, "Error getting username ID: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

        /**
         * Gets the id by using the password from the backend and compares it with the username id.
         *
         * @param usernameId
         * @param password
         */
        private void getPasswordIds(final int usernameId, final String password, final String username) {
            // Create the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = backendUrl + "getIdByPassword/" + password;

            // Request a string response from the provided URL.
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // Successful response, will compare ids. This implementation will likely
                            // be changed at a later date.
                            ArrayList<Integer> passwordIds = new ArrayList<>();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    int id = response.getInt(i);
                                    passwordIds.add(id);
                                }
                                // Compare password IDs with username ID
                                if (passwordIds.contains(usernameId)) {
                                    // Login successful
                                    // Redirects to another activity but I will probably change this
                                    // To redirect to the main menu when it is implemented.
                                    proceedToNextActivity(username);
                                } else {
                                    // Login failed
                                    // Show error message.
                                    // In the future, I plan to add more specific error messages based on what error might've occurred.
                                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error response
                    Toast.makeText(LoginActivity.this, "Error getting password IDs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsonArrayRequest);
        }

        private void proceedToNextActivity(String username) {
            // Save username in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", username);
            //Toast.makeText(LoginActivity.this, "username: " + username, Toast.LENGTH_SHORT).show();
            editor.commit();

            // Redirects to MainActivity and pass the username as an extra
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish(); // Finish LoginActivity so user can't go back to it using back button
        }

    }
