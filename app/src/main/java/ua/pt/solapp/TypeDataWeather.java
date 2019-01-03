package ua.pt.solapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "typeDataWeather", indices = {@Index(value = {"date"}, unique = true)})
public class TypeDataWeather {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("data")
    @Expose
    private ArrayList<WeatherTypeEntry> weatherTypeEntries;

    public int getId() {
        return id;
    }

    public ArrayList<WeatherTypeEntry> getWeatherTypeEntries() {
        return weatherTypeEntries;
    }
}
