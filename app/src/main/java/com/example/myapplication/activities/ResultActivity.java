package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.ScoreStorage;

public class ResultActivity extends AppCompatActivity {

    TextView txtFinalScore;
    Button btnRetry, btnRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtFinalScore = findViewById(R.id.txtFinalScore);
        btnRetry = findViewById(R.id.btnRetry);
        btnRanking = findViewById(R.id.btnRanking);

        int score = getIntent().getIntExtra("score", 0);
        String playerName = getIntent().getStringExtra("playerName");

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Joueur";
        }

        txtFinalScore.setText("Ton score : " + score + " / 10");

        // (Optionnel) Toast pour vérifier les valeurs reçues
        // Toast.makeText(this, "Nom: " + playerName + " | Score: " + score, Toast.LENGTH_SHORT).show();

        // Sauvegarder score et nom dans les préférences
        ScoreStorage.saveScore(this, score, playerName);

        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
            startActivity(intent);
            finish();
        });

        btnRanking.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, RankingActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
