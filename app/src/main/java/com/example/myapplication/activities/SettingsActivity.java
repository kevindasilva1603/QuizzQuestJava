package com.example.myapplication.activities;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class SettingsActivity extends AppCompatActivity {

    Switch switchSound, switchTheme;
    Button btnHome;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchSound = findViewById(R.id.switchSound);
        switchTheme = findViewById(R.id.switchTheme);
        btnHome = findViewById(R.id.btnHome); // ✅ Ajout nécessaire

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Charger les préférences sauvegardées
        switchSound.setChecked(prefs.getBoolean("sound", true));
        switchTheme.setChecked(prefs.getBoolean("darkmode", false));

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("sound", isChecked).apply();
            Toast.makeText(this, "Son " + (isChecked ? "activé" : "désactivé"), Toast.LENGTH_SHORT).show();
        });

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("darkmode", isChecked).apply();
            Toast.makeText(this, "Thème " + (isChecked ? "sombre" : "clair"), Toast.LENGTH_SHORT).show();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AccueilActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
