package ua.pt.solapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "weatherType", indices = {@Index(value = {"date"}, unique = true)})
public class WeatherTypeEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("idWeatherType")
    @Expose
    private int idWeatherType;

    @SerializedName("descIdWeatherTypePT")
    @Expose
    private String info;

    @Ignore
    public WeatherTypeEntry(int idWeatherType, String info) {
        this.idWeatherType = idWeatherType;
        this.info = info;
    }

    public WeatherTypeEntry(int id, int idWeatherType, String info) {
        this.id = id;
        this.idWeatherType = idWeatherType;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public int getIdWeatherType() {
        return idWeatherType;
    }

    public String getInfo() {
        return info;
    }
}
