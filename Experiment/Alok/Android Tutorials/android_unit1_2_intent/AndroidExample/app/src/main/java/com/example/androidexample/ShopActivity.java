package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {
    private TextView counterText;
    private TextView bitCoinCounterText;
    private Button buyButton;
    private Button backButton;
    private int counter = 0;
    private int bitcoinCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);             // link to Shop activity XML

        /* initialize UI elements */
        counterText = findViewById(R.id.shop_counter_txt);
        bitCoinCounterText = findViewById(R.id.shop_bitcoin_counter_txt);
        buyButton = findViewById(R.id.shop_buy_btn);
        backButton = findViewById(R.id.shop_back_btn);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            counterText.setText("You currently have NaN points");
        } else {
            counter = Integer.parseInt(extras.getString("NUM"));
            counterText.setText("You currently have " + extras.getString("NUM") + " points");
        }

        /* Update counters if user has enough points */
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter >= 50) {
                    counter -= 50;
                    bitcoinCounter++;
                    counterText.setText("You currently have " + String.valueOf(counter) + " points");
                    bitCoinCounterText.setText("You currently have " + String.valueOf(bitcoinCounter) + " BitCoin");
                }
            }
        });

        /* Switch back to Main activity */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });
    }
}
