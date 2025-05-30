package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.Question;
import com.example.myapplication.models.QuestionResponse;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    TextView txtQuestion, txtTimer;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnJoker;

    List<Question> questions = new ArrayList<>();
    int currentIndex = 0;
    int score = 0;
    CountDownTimer countDownTimer;
    final int TIME_PER_QUESTION = 15;
    boolean jokerUsed = false;
    String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestion = findViewById(R.id.txtQuestion);
        txtTimer = findViewById(R.id.txtTimer);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        btnJoker = findViewById(R.id.btnJoker);

        playerName = getIntent().getStringExtra("playerName");
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Joueur";
        }

        btnJoker.setOnClickListener(v -> useJoker());

        ApiService apiService = ApiClient.getApiService();
        apiService.getQuestions(10, "multiple").enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body().results;
                    afficherQuestion();
                } else {
                    Toast.makeText(QuizActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Erreur API : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void afficherQuestion() {
        if (currentIndex >= questions.size()) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("playerName", playerName);
            startActivity(intent);
            finish();
            return;
        }

        Question q = questions.get(currentIndex);
        txtQuestion.setText(android.text.Html.fromHtml(q.question));

        List<String> allAnswers = new ArrayList<>();
        allAnswers.addAll(q.incorrect_answers);
        allAnswers.add(q.correct_answer);
        Collections.shuffle(allAnswers);

        btnAnswer1.setText(allAnswers.get(0));
        btnAnswer2.setText(allAnswers.get(1));
        btnAnswer3.setText(allAnswers.get(2));
        btnAnswer4.setText(allAnswers.get(3));

        btnAnswer1.setOnClickListener(v -> handleAnswer(btnAnswer1));
        btnAnswer2.setOnClickListener(v -> handleAnswer(btnAnswer2));
        btnAnswer3.setOnClickListener(v -> handleAnswer(btnAnswer3));
        btnAnswer4.setOnClickListener(v -> handleAnswer(btnAnswer4));

        resetButtons();
        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(TIME_PER_QUESTION * 1000, 1000) {
            int timeLeft = TIME_PER_QUESTION;

            @Override
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(String.valueOf(timeLeft));
                timeLeft--;
            }

            @Override
            public void onFinish() {
                txtTimer.setText("0");
                Toast.makeText(QuizActivity.this, "Temps écoulé !", Toast.LENGTH_SHORT).show();
                disableButtons();
                handleAnswer(null);
            }
        }.start();
    }

    private void handleAnswer(Button selectedButton) {
        if (countDownTimer != null) countDownTimer.cancel();

        String correctAnswer = questions.get(currentIndex).correct_answer;

        if (selectedButton == null) {
            for (Button b : new Button[]{btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4}) {
                if (b.getText().toString().equals(correctAnswer)) {
                    b.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    b.setEnabled(false);
                }
            }
        } else {
            String selectedText = selectedButton.getText().toString();
            if (selectedText.equals(correctAnswer)) {
                selectedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                score++;
            } else {
                selectedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                for (Button b : new Button[]{btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4}) {
                    if (b.getText().toString().equals(correctAnswer)) {
                        b.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                        break;
                    }
                }
            }
            disableButtons();
        }

        btnAnswer1.postDelayed(() -> {
            currentIndex++;
            afficherQuestion();
        }, 1000);
    }

    private void resetButtons() {
        for (Button b : new Button[]{btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4}) {
            b.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            b.setEnabled(true);
        }

        if (!jokerUsed) {
            btnJoker.setEnabled(true);
        }
    }

    private void disableButtons() {
        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);
    }

    private void useJoker() {
        if (jokerUsed) return;

        String correctAnswer = questions.get(currentIndex).correct_answer;
        List<Button> wrongButtons = new ArrayList<>();

        for (Button b : new Button[]{btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4}) {
            if (!b.getText().toString().equals(correctAnswer)) {
                wrongButtons.add(b);
            }
        }

        Collections.shuffle(wrongButtons);
        if (wrongButtons.size() >= 2) {
            for (int i = 0; i < 2; i++) {
                Button b = wrongButtons.get(i);
                b.setEnabled(false);
                b.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        } else if (wrongButtons.size() == 1) {
            Button b = wrongButtons.get(0);
            b.setEnabled(false);
            b.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }

        btnJoker.setEnabled(false);
        jokerUsed = true;

        Toast.makeText(this, "Deux mauvaises réponses supprimées !", Toast.LENGTH_SHORT).show();
    }
}
