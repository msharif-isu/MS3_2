package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class TFAActivity extends AppCompatActivity {

    private CheckBox doNotChallengeCheckbox;
    private EditText textBox;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2fa);

        // Initialize views
        doNotChallengeCheckbox = findViewById(R.id.do_not_challenge_checkbox);
        textBox = findViewById(R.id.text_box);
        verifyButton = findViewById(R.id.verify_button);
        // Initial state of checkbox
        doNotChallengeCheckbox.setChecked(false);

        // Add an onClickListener to the checkbox
        doNotChallengeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = doNotChallengeCheckbox.isChecked();
                if (isChecked) {
                    // Code for when the box is checked, unimplemented
                } else {
                    // Code for when the box isn't checked, also unimplemented
                }
            }
        });

        // onClickListener for the verify button
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TFAActivity.this, MainActivity.class);

                // username and password from intent extras
                String username = getIntent().getStringExtra("USERNAME");
                String password = getIntent().getStringExtra("PASSWORD");

                // Pass username and password to MainActivity
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);

                startActivity(intent);
            }
        });
    }
}
