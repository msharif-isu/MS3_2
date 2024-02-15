package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView resultTxt; // text for results
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9; // numbers for calculator
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide, btnEquals, btnClear; // other operations for calculator
    private Button backBtn;

    private StringBuilder currentInput = new StringBuilder();
    private double operand1 = 0;
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        // initialize UI elements
        backBtn = findViewById(R.id.counter_back_btn);
        resultTxt = findViewById(R.id.number);
        btn0 = findViewById(R.id.button0);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);
        btn8 = findViewById(R.id.button8);
        btn9 = findViewById(R.id.button9);
        btnAdd = findViewById(R.id.button_add);
        btnSubtract = findViewById(R.id.button_subtract);
        btnMultiply = findViewById(R.id.button_multiply);
        btnDivide = findViewById(R.id.button_divide);
        btnEquals = findViewById(R.id.button_equals);
        btnClear = findViewById(R.id.button_clear);

        // Set OnClickListener for number buttons
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                currentInput.append(button.getText());
                updateResult();
            }
        };

        btn0.setOnClickListener(numberClickListener);
        btn1.setOnClickListener(numberClickListener);
        btn2.setOnClickListener(numberClickListener);
        btn3.setOnClickListener(numberClickListener);
        btn4.setOnClickListener(numberClickListener);
        btn5.setOnClickListener(numberClickListener);
        btn6.setOnClickListener(numberClickListener);
        btn7.setOnClickListener(numberClickListener);
        btn8.setOnClickListener(numberClickListener);
        btn9.setOnClickListener(numberClickListener);

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                //intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });


        // Set OnClickListener for operator buttons
        View.OnClickListener operatorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = ((Button) v).getText().toString();
                operand1 = Double.parseDouble(currentInput.toString());
                currentInput.setLength(0); // Clear current input for the next number
            }
        };

        btnAdd.setOnClickListener(operatorClickListener);
        btnSubtract.setOnClickListener(operatorClickListener);
        btnMultiply.setOnClickListener(operatorClickListener);
        btnDivide.setOnClickListener(operatorClickListener);

        // Set OnClickListener for equals button
        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double operand2 = Double.parseDouble(currentInput.toString());
                double result = performOperation(operand1, operand2, operator);
                currentInput.setLength(0);
                currentInput.append(result);
                updateResult();
            }
        });

        // Set OnClickListener for clear button
        btnClear.setOnClickListener(new View.OnClickListener() {
            // Clears the number and the operator
            @Override
            public void onClick(View v) {
                currentInput.setLength(0);
                operand1 = 0;
                operator = "";
                updateResult();
            }
        });
    }

    private void updateResult() {
        resultTxt.setText(currentInput.toString());
    }


    // Does the math based on the given parameters.
    private double performOperation(double operand1, double operand2, String operator) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "x":
                return operand1 * operand2;
            case "/":
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    // Handle division by zero
                    return Double.NaN;
                }
            default:
                return operand2;
        }

    }

}
