package com.example.assignedtaskproject.data.room;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.assignedtaskproject.model.CityWeather;

@Database(entities = {CityWeather.class}, version = 1)
public abstract class CityWeatherDatabase extends RoomDatabase {
    public static CityWeatherDatabase cityWeatherDatabase;

    public abstract CityWeatherDao cityWeatherDao();


    public static CityWeatherDatabase getInstance(Context context) {
        if (cityWeatherDatabase == null) {
            cityWeatherDatabase = Room.databaseBuilder(context.getApplicationContext(), CityWeatherDatabase.class, "weatherdetails_db")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return cityWeatherDatabase;

    }

}
