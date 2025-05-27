package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.ScoreStorage;

public class ResultActivity extends AppCompatActivity {

    TextView txtFinalScore;
    Button btnRetry, btnRanking, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtFinalScore = findViewById(R.id.txtFinalScore);
        btnRetry = findViewById(R.id.btnRetry);
        btnRanking = findViewById(R.id.btnRanking);
        btnHome = findViewById(R.id.btnHome);

        int score = getIntent().getIntExtra("score", 0);
        String playerName = getIntent().getStringExtra("playerName");

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Joueur";
        }

        txtFinalScore.setText("🎉 Ton score : " + score + " / 10");

        // Enregistrer score + joueur
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

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, AccueilActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
