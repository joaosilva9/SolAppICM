package ua.pt.solapp;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherInfo {

    @SerializedName("owner")
    @Expose
    private String owner;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("data")
    @Expose
    private ArrayList<Weather> data;

    @SerializedName("globalIdLocal")
    @Expose
    private int globalIdLocal;

    @SerializedName("dataUpdate")
    @Expose
    private String dataUpdate;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<Weather> getData() {
        return data;
    }

    public void setData(ArrayList<Weather> data) {
        this.data = data;
    }

    public int getGlobalIdLocal() {
        return globalIdLocal;
    }

    public void setGlobalIdLocal(int globalIdLocal) {
        this.globalIdLocal = globalIdLocal;
    }

    public String getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(String dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "owner='" + owner + '\'' +
                ", country='" + country + '\'' +
                ", data=" + data +
                ", globalIdLocal=" + globalIdLocal +
                ", dataUpdate='" + dataUpdate + '\'' +
                '}';
    }
}
