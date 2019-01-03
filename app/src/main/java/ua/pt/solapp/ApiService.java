package ua.pt.solapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    String BASE_URL = "http://api.ipma.pt/open-data/";

    @GET("forecast/meteorology/cities/daily/{globalIdLocal}.json")
    Call<WeatherInfo> getWeather(@Path("globalIdLocal") int globalIdLocal);

    @GET("distrits-islands.json")
    Call<DistrictInfo> getDistrict();

    @GET("weather-type-classe.json")
    Call<TypeDataWeather> getWeatherTypeInfo();

}
