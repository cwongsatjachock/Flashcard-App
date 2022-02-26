package com.example.victorsflashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView fQuestion = findViewById(R.id.flashcard_question);
        TextView fAnswer = findViewById(R.id.flashcard_answer);
        fQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fQuestion.setVisibility(View.INVISIBLE);
                fAnswer.setVisibility(View.VISIBLE);
            };
        });

        fAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fQuestion.setVisibility(View.VISIBLE);
                fAnswer.setVisibility(View.INVISIBLE);
            };
        });
    }

    

}