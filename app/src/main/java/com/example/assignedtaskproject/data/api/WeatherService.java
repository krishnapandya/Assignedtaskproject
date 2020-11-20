package com.example.assignedtaskproject.data.api;

import com.example.assignedtaskproject.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    public static String BaseUrl = "http://api.openweathermap.org/";

    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("q") String city, @Query("appid") String app_id);
}
