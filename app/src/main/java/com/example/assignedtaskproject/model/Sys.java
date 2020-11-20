package com.example.assignedtaskproject.model;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;


   public class Coord {
        @SerializedName("lon")
        public float lon;
        @SerializedName("lat")
        public float lat;
    }
}
