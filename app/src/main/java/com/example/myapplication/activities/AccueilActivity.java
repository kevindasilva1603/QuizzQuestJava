package com.example.myapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AccueilActivity extends AppCompatActivity {

    Button btnPlay, btnRanking, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        btnPlay = findViewById(R.id.btnPlay);
        btnRanking = findViewById(R.id.btnRanking);
        btnSettings = findViewById(R.id.btnSettings);

        btnPlay.setOnClickListener(v -> showNameDialog());

        btnRanking.setOnClickListener(v -> {
            Intent intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void showNameDialog() {
        EditText input = new EditText(this);
        input.setHint("Entrez votre prénom");

        new AlertDialog.Builder(this)
                .setTitle("Avant de jouer")
                .setMessage("Quel est votre prénom ?")
                .setView(input)
                .setPositiveButton("Commencer", (dialog, which) -> {
                    String playerName = input.getText().toString().trim();
                    if (!playerName.isEmpty()) {
                        Intent intent = new Intent(this, QuizActivity.class);
                        intent.putExtra("playerName", playerName);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
