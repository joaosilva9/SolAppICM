package ua.pt.solapp;

import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TypeDataWeather {

    @SerializedName("data")
    @Expose
    private ArrayList<WeatherTypeEntry> weatherTypeEntries;

    public ArrayList<WeatherTypeEntry> getWeatherTypeEntries() {
        return weatherTypeEntries;
    }
}
