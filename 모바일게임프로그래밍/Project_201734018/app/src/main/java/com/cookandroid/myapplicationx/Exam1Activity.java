package com.cookandroid.myapplicationx;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Exam1Activity extends AppCompatActivity {
    Button Exam1Button,Exam1Close;
    EditText address,name,hp;
    TextView Exam1Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam1);
        setTitle("개인정보 입력");

        address = findViewById(R.id.address);
        name = findViewById(R.id.name);
        hp = findViewById(R.id.hp);

        Exam1Text = findViewById(R.id.Exam1Text);
        Exam1Button = findViewById(R.id.Exam1Button);
        Exam1Close = findViewById(R.id.Exam1Close);
        Exam1Button.setBackgroundColor(Color.parseColor("#8EFB8E"));
        Exam1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                str += "주소 : " + address.getText().toString() + "\n";
                str += "이름 : " + name.getText().toString() + "\n";
                str += "번호 : " + hp.getText().toString() + "\n";

                Exam1Text.setText(str);

                address.setText("");
                name.setText("");
                hp.setText("");
            }
        });
        Exam1Close.setBackgroundColor(Color.parseColor("#8EFB8E"));
        Exam1Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
