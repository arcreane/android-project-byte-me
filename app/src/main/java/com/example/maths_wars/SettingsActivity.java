package com.example.maths_wars;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch switchSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchSound = findViewById(R.id.switchSound);

        SharedPreferences prefs = getSharedPreferences("maths_wars_prefs", MODE_PRIVATE);
        boolean isSoundOn = prefs.getBoolean("sound_enabled", true);
        switchSound.setChecked(isSoundOn);

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("sound_enabled", isChecked).apply();
        });
    }
}
