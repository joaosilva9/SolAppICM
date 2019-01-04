package ua.pt.solapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "weather")
public class Weather {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("precipitaProb")
    @Expose
    private String precipitaProb;

    @SerializedName("tMin")
    @Expose
    private String min;

    @SerializedName("tMax")
    @Expose
    private String max;

    @SerializedName("predWindDir")
    @Expose
    private String predWindDir;

    @SerializedName("idWeatherType")
    @Expose
    private int idWeatherType;

    @SerializedName("classWindSpeed")
    @Expose
    private int classWindSpeed;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("forecastDate")
    @Expose
    private String forecastDate;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    public Weather(int id, String precipitaProb, String min, String max, String predWindDir, int idWeatherType, int classWindSpeed, String longitude, String forecastDate, String latitude) {
        this.id = id;
        this.precipitaProb = precipitaProb;
        this.min = min;
        this.max = max;
        this.predWindDir = predWindDir;
        this.idWeatherType = idWeatherType;
        this.classWindSpeed = classWindSpeed;
        this.longitude = longitude;
        this.forecastDate = forecastDate;
        this.latitude = latitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrecipitaProb(String precipitaProb) {
        this.precipitaProb = precipitaProb;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setPredWindDir(String predWindDir) {
        this.predWindDir = predWindDir;
    }

    public void setIdWeatherType(int idWeatherType) {
        this.idWeatherType = idWeatherType;
    }

    public void setClassWindSpeed(int classWindSpeed) {
        this.classWindSpeed = classWindSpeed;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getPredWindDir() {
        return predWindDir;
    }

    public int getIdWeatherType() {
        return idWeatherType;
    }

    public int getClassWindSpeed() {
        return classWindSpeed;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public String getLatitude() {
        return latitude;
    }
}
