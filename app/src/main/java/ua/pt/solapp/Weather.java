package ua.pt.solapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "weather", indices = {@Index(value = {"date"}, unique = true)})
public class Weather {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("precipitaProb")
    @Expose
    private String precipitaProb;

    @SerializedName("tMin")
    @Expose
    private String tMin;

    @SerializedName("tMax")
    @Expose
    private String tMax;

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

    @Ignore
    public Weather(String precipitaProb, String tMin, String tMax, String predWindDir, int idWeatherType, int classWindSpeed, String longitude, String forecastDate, String latitude) {
        this.precipitaProb = precipitaProb;
        this.tMin = tMin;
        this.tMax = tMax;
        this.predWindDir = predWindDir;
        this.idWeatherType = idWeatherType;
        this.classWindSpeed = classWindSpeed;
        this.longitude = longitude;
        this.forecastDate = forecastDate;
        this.latitude = latitude;
    }

    public Weather(int id, String precipitaProb, String tMin, String tMax, String predWindDir, int idWeatherType, int classWindSpeed, String longitude, String forecastDate, String latitude) {
        this.id = id;
        this.precipitaProb = precipitaProb;
        this.tMin = tMin;
        this.tMax = tMax;
        this.predWindDir = predWindDir;
        this.idWeatherType = idWeatherType;
        this.classWindSpeed = classWindSpeed;
        this.longitude = longitude;
        this.forecastDate = forecastDate;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public String gettMin() {
        return tMin;
    }

    public String gettMax() {
        return tMax;
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
