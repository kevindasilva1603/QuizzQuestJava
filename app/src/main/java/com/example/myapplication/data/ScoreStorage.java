package com.example.myapplication.data;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreStorage {

    private static final String PREF_NAME = "quiz_scores";
    private static final String KEY_SCORES = "scores";

    public static void saveScore(Context context, int score, String name) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<ScoreEntry> entries = getScoreEntries(context);
        entries.add(new ScoreEntry(name, score));
        Collections.sort(entries, (a, b) -> b.score - a.score);

        JSONArray jsonArray = new JSONArray();
        for (ScoreEntry entry : entries) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", entry.name);
                obj.put("score", entry.score);
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        prefs.edit().putString(KEY_SCORES, jsonArray.toString()).apply();
    }

    public static List<ScoreEntry> getScoreEntries(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(KEY_SCORES, null);
        List<ScoreEntry> entries = new ArrayList<>();

        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String name = obj.getString("name");
                    int score = obj.getInt("score");
                    entries.add(new ScoreEntry(name, score));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return entries;
    }

    public static class ScoreEntry {
        public String name;
        public int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
    public static void clearScores(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_SCORES).apply();
    }

}