package com.example.assignedtaskproject.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIObject {
    public static WeatherService authAPI=null;
    private APIObject(){}

    public static WeatherService getInsatance(){
        if (authAPI==null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WeatherService.BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            authAPI = retrofit.create(WeatherService.class);
        }
        return authAPI;
    }
}
