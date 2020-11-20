package com.example.assignedtaskproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.example.assignedtaskproject.databinding.ActivityMainBinding;
import com.example.assignedtaskproject.viewmodel.Weatherviewmodel;

//created by krishna desai.....


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private LiveData<String> cityWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        Weatherviewmodel viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Weatherviewmodel.class);



//search box ....
        activityMainBinding.searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    cityWeather = viewModel.getCityWeather(query,MainActivity.this);
                    cityWeather.observe(MainActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            activityMainBinding.weatherData.setText(s);
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


}