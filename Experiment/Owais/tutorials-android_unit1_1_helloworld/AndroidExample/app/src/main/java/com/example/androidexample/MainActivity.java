package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private Button changeMessageButton;  // define button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        messageText.setText("Bruh.");

        changeMessageButton = findViewById(R.id.button);  // link to the button in the Main activity XML
        changeMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMessage();
            }
        });
    }

    private void changeMessage() {
        String[] messages = {"Bruh.", "Hello World", "What's up?", "Coding is fun! Until you encounter a bug.", "I'm running out of messages to write.", "You ever consumed the delicacy that is cheese?", "Hello TA if you are reading this.", "Hello group members if you are reading this.", "Please give me an A on this demo", "Is this a significant enough change?"};

        // randomly select a message from the array
        int randomIndex = (int) (Math.random() * messages.length);

        // Change the message text
        messageText.setText(messages[randomIndex]);
    }
}
