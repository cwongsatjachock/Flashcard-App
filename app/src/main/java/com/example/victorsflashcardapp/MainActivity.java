package com.example.victorsflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.security.AccessController;
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
                // get the center for the clipping circle
                int cx = fAnswer.getWidth() / 2;
                int cy = fAnswer.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                Animator anim = ViewAnimationUtils.createCircularReveal(fAnswer, cx, cy, 0f, finalRadius);

                fQuestion.setVisibility(View.INVISIBLE);
                fAnswer.setVisibility(View.VISIBLE);

                anim.setDuration(500);
                anim.start();
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

                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

                fQuestion.startAnimation(leftOutAnim);
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first
                        if(fAnswer.getVisibility() == View.VISIBLE)
                        {
                            fQuestion.setVisibility(View.VISIBLE);
                            fAnswer.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        fQuestion.startAnimation(rightInAnim);

                        currentCardDisplayedIndex++;

                        if(currentCardDisplayedIndex >= allFlashcards.size()) {
                            currentCardDisplayedIndex = 0;
                        }

                        allFlashcards = flashcardDatabase.getAllCards();
                        Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);

                        fQuestion.setText(flashcard.getQuestion());
                        fAnswer.setText(flashcard.getAnswer());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });



            }
        });

        ImageButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

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