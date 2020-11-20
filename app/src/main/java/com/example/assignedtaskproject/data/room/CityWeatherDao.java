package com.example.assignedtaskproject.data.room;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignedtaskproject.model.CityWeather;

@Dao
public interface CityWeatherDao {

    @Insert
    public void Insert(CityWeather cityWeather);

    @Update
    void update(CityWeather cityWeather);


    @Delete
    void delete(CityWeather cityWeather);


    @Query("DELETE FROM city_weather")
    void deleteAllcities();

    @Query("SELECT * FROM city_weather WHERE city_name=:city_name")
    CityWeather getCityWeather(String city_name);

}
