package com.example.victorsflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView fQuestion;
    TextView fAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fQuestion = findViewById(R.id.flashcard_question);
        fAnswer = findViewById(R.id.flashcard_answer);

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

        ImageButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
            };
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if(data != null) {
                String questionString = data.getExtras().getString("QUESTION_KEY");
                String answerString = data.getExtras().getString("ANSWER_KEY");
                fQuestion.setText(questionString);
                fAnswer.setText(answerString);
            }
        }
    }

}