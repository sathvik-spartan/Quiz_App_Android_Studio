package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView, highScoreText;
    private RadioGroup optionsGroup;
    private Button nextButton;

    private final String[] questions = {
            "What is the capital of France?",
            "Which language is used for Android development?",
            "Who developed Java?"
    };

    private final String[][] options = {
            {"Berlin", "Madrid", "Paris", "Rome"},
            {"Python", "Java", "Swift", "C++"},
            {"Microsoft", "Apple", "Sun Microsystems", "Google"}
    };

    private final int[] answers = {2, 1, 2}; // Correct answer index

    private int currentQuestionIndex = 0;
    private int score = 0;
    private SharedPreferences sharedPreferences;
    private int highScore;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        highScoreText = findViewById(R.id.highScoreText);
        optionsGroup = findViewById(R.id.optionsGroup);
        nextButton = findViewById(R.id.nextButton);

        sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        highScore = sharedPreferences.getInt("HIGH_SCORE", 0);
        highScoreText.setText("High Score: " + highScore);

        loadQuestion();

        nextButton.setOnClickListener(v -> checkAnswer());
    }

    private void loadQuestion() {
        if (currentQuestionIndex >= questions.length) {
            showResult();
            return;
        }

        questionTextView.setText(questions[currentQuestionIndex]);
        optionsGroup.removeAllViews();

        for (int i = 0; i < options[currentQuestionIndex].length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(options[currentQuestionIndex][i]);
            radioButton.setId(i);
            radioButton.setTextColor(Color.parseColor(getString(R.string.ffffff)));
            optionsGroup.addView(radioButton);
        }
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Select an answer!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedId == answers[currentQuestionIndex]) {
            score++;
        }

        currentQuestionIndex++;
        loadQuestion();
    }

    @SuppressLint("SetTextI18n")
    private void showResult() {
        Toast.makeText(this, "Quiz Over! Score: " + score + "/" + questions.length, Toast.LENGTH_LONG).show();

        if (score > highScore) {
            highScore = score;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH_SCORE", highScore);
            editor.apply();
            highScoreText.setText("High Score: " + highScore);
            Toast.makeText(this, "New High Score!", Toast.LENGTH_SHORT).show();
        }

        nextButton.setEnabled(false);
    }
}
