package com.example.assignedtaskproject.data.repo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.assignedtaskproject.data.api.APIObject;
import com.example.assignedtaskproject.data.room.CityWeatherDao;
import com.example.assignedtaskproject.data.room.CityWeatherDatabase;
import com.example.assignedtaskproject.model.CityWeather;
import com.example.assignedtaskproject.model.WeatherResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    public static CityWeatherDatabase cityWeatherDatabase;
    public static String AppId = "e2f19cb4ba5049b64870a676415f5cda";
    private static final String TAG = "WeatherRepository";
    public static CityWeatherDao cityWeatherDao;
    public MutableLiveData<String> cityWeatherResponse=new MutableLiveData<>();
    CityWeather cityWeatherLive=new CityWeather();

    public WeatherRepository(Application application) {
        cityWeatherDatabase = CityWeatherDatabase.getInstance(application);
        cityWeatherDao = cityWeatherDatabase.cityWeatherDao();
    }

    public void insert(CityWeather cityWeather) {

        new Thread(new InsertCityweather(cityWeather)).start();

    }


    public class InsertCityweather implements Runnable {
        CityWeather cityWeather;

        public InsertCityweather(CityWeather cityWeather) {
            this.cityWeather = cityWeather;
        }

        @Override
        public void run() {
            cityWeatherDao.Insert(cityWeather);
        }
    }

    public LiveData<String> getCurrentData(String city, LifecycleOwner context) {
      //  final CityWeather[] cityWeather = {new CityWeather()};
        Observable.fromRunnable(() -> {
            cityWeatherLive = cityWeatherDao.getCityWeather(city);

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<Object>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Object o) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (cityWeatherLive !=null){

                            Date timeStamp = null,currentTime = null;
                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                            Date currentLocalTime = cal.getTime();
                            DateFormat date = new SimpleDateFormat("HH:mm:ss");
// you can get seconds by adding  "...:ss" to it
                            date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                            String localTime = date.format(currentLocalTime);
                            try {
                                timeStamp=date.parse(cityWeatherLive.getTimestamp());
                                currentTime=date.parse(localTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            long diff= TimeUnit.MILLISECONDS.toSeconds(currentTime.getTime()-timeStamp.getTime());
                            if (diff<=60) {
                                Log.d(TAG, "getCurrentData:"+diff + cityWeatherLive.getCity_name());

                                String stringBuilder = "" +
                                        "From Database :: \n Country: " +
                                        cityWeatherLive.getCountry() +
                                        "\n" +
                                        "Temperature: " +
                                        cityWeatherLive.getTemp() +
                                        "\n" +
                                        "Temperature(Min): " +
                                       cityWeatherLive.getTemp_min() +
                                        "\n" +
                                        "Temperature(Max): " +
                                       cityWeatherLive.getTemp_max() +
                                        "\n" +
                                        "Humidity: " +
                                      cityWeatherLive.getHumidity() +
                                        "\n" +
                                        "Pressure: " +
                                        cityWeatherLive.getPressure();
                                cityWeatherResponse.setValue(stringBuilder);
                            }
                            else{
                                Log.d(TAG, "getCurrentData11:"+diff + cityWeatherLive.getCity_name());

                                Call<WeatherResponse> call = APIObject.getInsatance().getCurrentWeatherData(city, AppId);
                                call.enqueue(new Callback<WeatherResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                                        if (response.code() == 200) {
                                            WeatherResponse weatherResponse = response.body();
                                            assert weatherResponse != null;

                                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                                            Date currentLocalTime = cal.getTime();
                                            DateFormat date = new SimpleDateFormat("HH:mm:ss");
// you can get seconds by adding  "...:ss" to it
                                            date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                                            String localTime = date.format(currentLocalTime);

                                            CityWeather cityWeather = new CityWeather();
                                            cityWeather.setLat(String.valueOf(weatherResponse.coord.lat));
                                            cityWeather.setCity_name(weatherResponse.name);
                                            cityWeather.setLon(String.valueOf(weatherResponse.coord.lon));
                                            cityWeather.setHumidity(String.valueOf(weatherResponse.main.humidity));
                                            cityWeather.setTemp(String.valueOf(weatherResponse.main.temp));
                                            cityWeather.setTemp_min(String.valueOf(weatherResponse.main.temp_min));
                                            cityWeather.setTemp_max(String.valueOf(weatherResponse.main.temp_max));
                                            cityWeather.setPressure(String.valueOf(weatherResponse.main.pressure));
                                            cityWeather.setTimestamp(localTime);
                                            cityWeather.setCountry(weatherResponse.sys.country);
                                            cityWeather.setFeels_like(String.valueOf(weatherResponse.main.feels_like));
                                            insert(cityWeather);

                                            String stringBuilder = "Country: " +
                                                    weatherResponse.sys.country +
                                                    "\n" +
                                                    "Temperature: " +
                                                    weatherResponse.main.temp +
                                                    "\n" +
                                                    "Temperature(Min): " +
                                                    weatherResponse.main.temp_min +
                                                    "\n" +
                                                    "Temperature(Max): " +
                                                    weatherResponse.main.temp_max +
                                                    "\n" +
                                                    "Humidity: " +
                                                    weatherResponse.main.humidity +
                                                    "\n" +
                                                    "Pressure: " +
                                                    weatherResponse.main.pressure;
                                            cityWeatherResponse.setValue(stringBuilder);

                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                                        cityWeatherResponse.setValue(t.getMessage());
                                    }
                                });
                            }
                        }
                        else {
                            Call<WeatherResponse> call = APIObject.getInsatance().getCurrentWeatherData(city, AppId);
                            call.enqueue(new Callback<WeatherResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                                    if (response.code() == 200) {
                                        WeatherResponse weatherResponse = response.body();
                                        assert weatherResponse != null;

                                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                                        Date currentLocalTime = cal.getTime();
                                        DateFormat date = new SimpleDateFormat("HH:mm:ss");
// you can get seconds by adding  "...:ss" to it
                                        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                                        String localTime = date.format(currentLocalTime);

                                        CityWeather cityWeather = new CityWeather();
                                        cityWeather.setLat(String.valueOf(weatherResponse.coord.lat));
                                        cityWeather.setCity_name(weatherResponse.name);
                                        cityWeather.setLon(String.valueOf(weatherResponse.coord.lon));
                                        cityWeather.setHumidity(String.valueOf(weatherResponse.main.humidity));
                                        cityWeather.setTemp(String.valueOf(weatherResponse.main.temp));
                                        cityWeather.setTemp_min(String.valueOf(weatherResponse.main.temp_min));
                                        cityWeather.setTemp_max(String.valueOf(weatherResponse.main.temp_max));
                                        cityWeather.setPressure(String.valueOf(weatherResponse.main.pressure));
                                        cityWeather.setTimestamp(localTime);
                                        cityWeather.setCountry(weatherResponse.sys.country);
                                        cityWeather.setFeels_like(String.valueOf(weatherResponse.main.feels_like));
                                        insert(cityWeather);

                                        String stringBuilder = "Country: " +
                                                weatherResponse.sys.country +
                                                "\n" +
                                                "Temperature: " +
                                                weatherResponse.main.temp +
                                                "\n" +
                                                "Temperature(Min): " +
                                                weatherResponse.main.temp_min +
                                                "\n" +
                                                "Temperature(Max): " +
                                                weatherResponse.main.temp_max +
                                                "\n" +
                                                "Humidity: " +
                                                weatherResponse.main.humidity +
                                                "\n" +
                                                "Pressure: " +
                                                weatherResponse.main.pressure;
                                        cityWeatherResponse.setValue(stringBuilder);

                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                                    cityWeatherResponse.setValue(t.getMessage());
                                }
                            });
                        }
                    }
                });


        return cityWeatherResponse;
    }
}
