package com.example.maths_wars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvFinalScore, tvFeedback;
    Button btnPlayAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvFinalScore = findViewById(R.id.tvFinalScore);
        tvFeedback = findViewById(R.id.tvFeedback);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        int finalScore = getIntent().getIntExtra("score", 0);
        tvFinalScore.setText("Score: " + finalScore);

        SharedPreferences prefs = getSharedPreferences("maths_wars_prefs", MODE_PRIVATE);
        int highScore = prefs.getInt("high_score", 0);
        if (finalScore > highScore) {
            prefs.edit().putInt("high_score", finalScore).apply();
            highScore = finalScore;
        }

        String feedback;
        if (finalScore >= 200) {
            feedback = "High Score: " + highScore + "\nYou're a Math Jedi 💫";
        } else if (finalScore >= 100) {
            feedback = "High Score: " + highScore + "\nYou're a Math Knight 🧠";
        } else {
            feedback = "High Score: " + highScore + "\nYou're a Math Padawan 🥲";
        }

        tvFeedback.setText(feedback);

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
