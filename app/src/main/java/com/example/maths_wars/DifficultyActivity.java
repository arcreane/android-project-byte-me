package com.example.maths_wars;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    Button btnEasy, btnMedium, btnHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);

        btnEasy.setOnClickListener(v -> startQuiz("easy"));
        btnMedium.setOnClickListener(v -> startQuiz("medium"));
        btnHard.setOnClickListener(v -> startQuiz("hard"));
    }

    private void startQuiz(String difficulty) {
        Intent intent = new Intent(DifficultyActivity.this,
                QuizActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
}

