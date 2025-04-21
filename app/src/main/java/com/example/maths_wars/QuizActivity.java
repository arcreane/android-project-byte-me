package com.example.maths_wars;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestion, tvScore;
    Button btnOption1, btnOption2, btnOption3, btnOption4;
    ProgressBar progressBar;
    int score = 0;
    double correctAnswer;
    CountDownTimer timer;
    String difficulty;
    SoundPool soundPool;
    int soundCorrect, soundWrong;
    private boolean isSoundEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        difficulty = getIntent().getStringExtra("difficulty");

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        progressBar = findViewById(R.id.progressBar);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);

        // Load sound preference
        SharedPreferences prefs = getSharedPreferences("maths_wars_prefs", MODE_PRIVATE);
        boolean isSoundEnabled = prefs.getBoolean("sound_enabled", true);

        // SoundPool setup
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(attrs)
                .build();

        soundCorrect = soundPool.load(this, R.raw.correct, 1);
        soundWrong = soundPool.load(this, R.raw.wrong, 1);

        // Store isSoundEnabled flag for use in other methods
        this.isSoundEnabled = isSoundEnabled;

        generateQuestion();
    }


    void generateQuestion() {
        tvScore.setText("Score: " + score);
        String questionText = "";
        double answer = 0;
        String[] operations;

        if (difficulty.equals("easy")) {
            operations = new String[]{"+", "-"};
        } else if (difficulty.equals("medium")) {
            operations = new String[]{"+", "-", "*", "/"};
        } else {
            operations = new String[]{"sqrt", "radical", "+", "-", "*", "/"};
        }

        String op = operations[new Random().nextInt(operations.length)];
        int max = 20;
        if (difficulty.equals("medium")) max = 30;
        else if (difficulty.equals("hard")) max = 50;

        int a = new Random().nextInt(max) + 1;
        int b = new Random().nextInt(max - 1) + 1;

        switch (op) {
            case "+":
                answer = a + b;
                questionText = a + " + " + b + " = ?";
                break;
            case "-":
                answer = a - b;
                questionText = a + " - " + b + " = ?";
                break;
            case "*":
                answer = a * b;
                questionText = a + " × " + b + " = ?";
                break;
            case "/":
                answer = Math.round((double) a / b * 10.0) / 10.0;
                questionText = a + " ÷ " + b + " = ?";
                break;
            case "sqrt":
                int n = new Random().nextInt(10) + 1;
                answer = Math.sqrt(n * n);
                questionText = "√" + (n * n) + " = ?";
                break;
            case "radical":
                int radBase = new Random().nextInt(5) + 2;
                int exponent = 2 + new Random().nextInt(3);
                int radical = (int) Math.pow(radBase, exponent);
                answer = radBase;
                questionText = "What is the " + exponent + "√" + radical + " ?";
                break;
        }

        correctAnswer = answer;
        final double roundedCorrect = Math.round(correctAnswer * 100.0) / 100.0;

        Button[] buttons = {btnOption1, btnOption2, btnOption3, btnOption4};
        for (Button btn : buttons) {
            btn.setOnClickListener(null);
        }

        int correctBtn = new Random().nextInt(4);
        for (int i = 0; i < 4; i++) {
            final int index = i;
            if (i == correctBtn) {
                buttons[i].setText(formatAnswer(roundedCorrect));
                buttons[i].setOnClickListener(v -> {
                    animateButton(buttons[index]);
                    correct();
                });
            } else {
                double fake = roundedCorrect + new Random().nextInt(10) - 5;
                if (fake == roundedCorrect) fake += 1;
                buttons[i].setText(formatAnswer(fake));
                buttons[i].setOnClickListener(v -> {
                    animateButton(buttons[index]);
                    wrong();
                });
            }
        }

        tvQuestion.setText(questionText);
        startTimer();
    }

    String formatAnswer(double val) {
        if (val == (int) val) {
            return String.valueOf((int) val);
        } else {
            return String.format("%.2f", val);
        }
    }

    void startTimer() {
        if (timer != null) timer.cancel();

        progressBar.setProgress(100);

        timer = new CountDownTimer(10000, 100) {
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) millisUntilFinished / 100);
            }

            public void onFinish() {
                wrong();
            }
        }.start();
    }

    void correct() {
        score += 10;
        animateScoreUpdate();
        vibrateShort();
        if (isSoundEnabled) soundPool.play(soundCorrect, 1, 1, 0, 0, 1);

        Toast.makeText(this, "Correct ✅", Toast.LENGTH_SHORT).show();
        generateQuestion();
    }

    void wrong() {
        vibrateLong();
        if (isSoundEnabled) soundPool.play(soundWrong, 1, 1, 0, 0, 1);
        Toast.makeText(this, "Wrong ❌", Toast.LENGTH_SHORT).show();
        endQuiz();
    }

    private void animateScoreUpdate() {
        tvScore.setScaleX(1f);
        tvScore.setScaleY(1f);
        tvScore.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(150)
                .withEndAction(() -> tvScore.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start())
                .start();
    }


    void endQuiz() {
        if (timer != null) timer.cancel();
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    void vibrateShort() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) v.vibrate(100);
    }

    void vibrateLong() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) v.vibrate(500);
    }

    void animateButton(Button button) {
        button.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).withEndAction(() ->
                button.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        ).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        if (soundPool != null) soundPool.release();
    }
}
