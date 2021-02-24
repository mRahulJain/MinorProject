package com.android.collegeproject.api;


import com.android.collegeproject.model.WeatherModelClass;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiWeatherMethods {

@GET("2.5/weather")
Call<WeatherModelClass> getWeatherByCoordinates(
        @Query("lat") double latitude,
        @Query("lon") double longitude,
        @Query("appid") String api_key
);

@GET("2.5/weather")
Call<WeatherModelClass> getWetherByCityName(
        @Query("q") String city,
        @Query("appid") String api_key
);
}
