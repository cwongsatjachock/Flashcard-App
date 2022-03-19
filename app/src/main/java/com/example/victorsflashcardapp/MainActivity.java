package com.example.victorsflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView fQuestion;
    TextView fAnswer;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fQuestion = findViewById(R.id.flashcard_question);
        fAnswer = findViewById(R.id.flashcard_answer);
        ImageButton fNext = findViewById(R.id.next_button);


        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            fQuestion.setText(allFlashcards.get(0).getQuestion());
            fAnswer.setText(allFlashcards.get(0).getAnswer());
        }

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

        fNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFlashcards.size() == 0)
                    return;

                currentCardDisplayedIndex++;

                if(currentCardDisplayedIndex >= allFlashcards.size()) {
                    currentCardDisplayedIndex = 0;
                }

                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);

                fQuestion.setText(flashcard.getAnswer());
                fAnswer.setText(flashcard.getQuestion());
            }
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

                flashcardDatabase.insertCard(new Flashcard(questionString, answerString));
                allFlashcards = flashcardDatabase.getAllCards();
            }
        }
    }

}