package ua.pt.solapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import ua.pt.solapp.Weather;

@Dao
public interface WeatherDao {

    @Insert
    void insert(Weather... weather);

    @Update
    void update(Weather... weathers);

    @Delete
    void delete(Weather... weathers);

    @Query("SELECT * FROM weather")
    LiveData<List<Weather>> gelAllWeathers();

    @Query("SELECT * FROM weather WHERE longitude = :longitude AND latitude = :latitude AND forecastDate = :forecastDate")
    LiveData<Weather> getWeather(String longitude, String latitude, String forecastDate);

    @Query("SELECT idWeatherType FROM weather WHERE longitude = :longitude AND latitude = :latitude AND forecastDate = :forecastDate")
    LiveData<Integer> getIdWeatherType(String longitude, String latitude, String forecastDate);

}
