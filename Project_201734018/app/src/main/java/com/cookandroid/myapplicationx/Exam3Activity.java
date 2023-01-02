package com.cookandroid.myapplicationx;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Exam3Activity extends AppCompatActivity {

    String[] animalsArr = {"고양이","개","강아지","소","토끼"};
    int[] imageArr = {R.drawable.animal1,R.drawable.animal2,R.drawable.animal3,R.drawable.animal4,R.drawable.animal5};

    Spinner spinner1;
    Button exam3Close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam3);
        setTitle("동물 사진 컬렉션");
        setTitleColor(Color.parseColor("#8EFB8E"));

        spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,animalsArr);
        spinner1.setAdapter(adapter);

        final ImageView imageView1 = findViewById(R.id.imageView1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),animalsArr[position] + "의 이미지입니다.",Toast.LENGTH_SHORT).show();
                imageView1.setImageResource(imageArr[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        exam3Close = findViewById(R.id.exam3Close);

        exam3Close.setBackgroundColor(Color.parseColor("#8EFB8E"));
        exam3Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
