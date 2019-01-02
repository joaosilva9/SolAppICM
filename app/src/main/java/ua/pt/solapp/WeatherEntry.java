package ua.pt.solapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "weather", indices = {@Index(value = {"date"}, unique = true)})
public class WeatherEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date; //forecastDate
    private double maxTemp;
    private double minTemp;
    private double probPrecipitacao;
    private int globalIdLocal;
    private Date dataUpdate;
    private String predWindDir;
    private int idWeatherType;
    private int classWindSpeed;
    private double longitude;
    private double latitude;

    @Ignore
    public WeatherEntry(Date date, double maxTemp, double minTemp, double probPrecipitacao, int globalIdLocal, Date dataUpdate, String predWindDir, int idWeatherType, int classWindSpeed, double longitude, double latitude) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.probPrecipitacao = probPrecipitacao;
        this.globalIdLocal = globalIdLocal;
        this.dataUpdate = dataUpdate;
        this.predWindDir = predWindDir;
        this.idWeatherType = idWeatherType;
        this.classWindSpeed = classWindSpeed;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public WeatherEntry(int id, Date date, double maxTemp, double minTemp, double probPrecipitacao, int globalIdLocal, Date dataUpdate, String predWindDir, int idWeatherType, int classWindSpeed, double longitude, double latitude) {
        this.id = id;
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.probPrecipitacao = probPrecipitacao;
        this.globalIdLocal = globalIdLocal;
        this.dataUpdate = dataUpdate;
        this.predWindDir = predWindDir;
        this.idWeatherType = idWeatherType;
        this.classWindSpeed = classWindSpeed;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getProbPrecipitacao() {
        return probPrecipitacao;
    }

    public int getGlobalIdLocal() {
        return globalIdLocal;
    }

    public Date getDataUpdate() {
        return dataUpdate;
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

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
