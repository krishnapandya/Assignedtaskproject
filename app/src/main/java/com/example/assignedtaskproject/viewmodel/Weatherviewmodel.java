package com.example.assignedtaskproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;


import com.example.assignedtaskproject.data.repo.WeatherRepository;

//viewmodel class...
public class Weatherviewmodel  extends AndroidViewModel {
WeatherRepository weatherRepository;


    public Weatherviewmodel(@NonNull Application application) {
        super(application);
        weatherRepository =new WeatherRepository(application);

    }

    public LiveData<String> getCityWeather(String city, LifecycleOwner context){
        return weatherRepository.getCurrentData(city,context);
    }



}
