package com.example.myapplication.network;

import com.example.myapplication.models.QuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api.php")
    Call<QuestionResponse> getQuestions(
            @Query("amount") int amount,
            @Query("type") String type
    );
}
