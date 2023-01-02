package com.cookandroid.myapplicationx;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Exam2Activity extends AppCompatActivity {

    TextView exam2TextView1;
    RadioGroup radioGroup1;
    RadioButton radioButton1, radioButton2;
    CheckBox checkBox1;
    Button exam2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam2);
        setTitle("색 변경");

        exam2TextView1 = findViewById(R.id.exam2TextView1);
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        checkBox1 = findViewById(R.id.checkBox1);

        exam2Button = findViewById(R.id.exam2Close);

        String str = "NO PAIN NO GAIN. \n고통없이 얻어지는 것은 없다.";
        exam2TextView1.setText(str);
        exam2Button.setBackgroundColor(Color.parseColor("#8EFB8E"));
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1 :
                        exam2TextView1.setTextColor(Color.RED);
                        break;
                    case R.id.radioButton2 :
                        exam2TextView1.setTextColor(Color.BLUE);
                        break;
                    case R.id.radioButton3 :
                        exam2TextView1.setTextColor(Color.GREEN);
                        break;
                    case R.id.radioButton4 :
                        exam2TextView1.setTextColor(Color.rgb(216,124,232));
                        break;
                }
            }
        });

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {

                    if (exam2TextView1.getCurrentTextColor() == Color.RED) {
                        exam2TextView1.setBackgroundColor(Color.RED);
                    } else if (exam2TextView1.getCurrentTextColor() == Color.BLUE) {
                        exam2TextView1.setBackgroundColor(Color.BLUE);
                    } else {
                        exam2TextView1.setBackgroundColor(exam2TextView1.getCurrentTextColor());
                    }
                } else {
                    exam2TextView1.setBackgroundColor(Color.WHITE);
                }
            }
        });

        exam2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
