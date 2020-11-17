package com.android.collegeproject.api;

import com.android.collegeproject.model.NewsModelClass;
import com.android.collegeproject.model.Sources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiNewsMethods {
    @GET("/v2/top-headlines")
    Call<NewsModelClass> getTopHeadlines(
            @Query("country") String country,
            @Query("apiKey") String api_key
    );
    @GET("/v2/everything")
    Call<NewsModelClass> getEverything(
            @Query("q") String q,
            @Query("apiKey") String api_key
    );
    @GET("/v2/sources")
    Call<Sources> getSources(
            @Query("apiKey") String api_key
    );
}
