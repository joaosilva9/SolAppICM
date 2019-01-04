package ua.pt.solapp;

import android.support.annotation.NonNull;

import ua.pt.solapp.WeatherTypeEntry;

class WeatherTypeResponse {

    @NonNull
    private final WeatherTypeEntry[] mWeatherForecast;

    public WeatherTypeResponse(@NonNull final WeatherTypeEntry[] weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    public WeatherTypeEntry[] getWeatherForecast() {
        return mWeatherForecast;
    }
}
