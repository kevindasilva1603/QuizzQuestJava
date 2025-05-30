package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.ScoreStorage;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    ListView listViewScores;
    Button btnClear, btnHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        listViewScores = findViewById(R.id.listViewScores);
        btnClear = findViewById(R.id.btnClear);
        btnHome = findViewById(R.id.btnHome);

        chargerClassement();

        btnClear.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment effacer le classement ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        ScoreStorage.clearScores(this);
                        chargerClassement();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(RankingActivity.this, AccueilActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void chargerClassement() {
        List<ScoreStorage.ScoreEntry> entries = ScoreStorage.getScoreEntries(this);
        List<String> display = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            ScoreStorage.ScoreEntry e = entries.get(i);
            display.add("👤 " + e.name + " : " + e.score + " / 10");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display);
        listViewScores.setAdapter(adapter);
    }
}
