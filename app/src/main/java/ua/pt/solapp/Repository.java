package ua.pt.solapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private final ApiService apiService;
    private final CityDao cityDao;
    private final WeatherDao weatherDao;
    private final Executor executor;

    public Repository(Application application, ApiService apiService){
        MyDatabase db = MyDatabase.getDatabase(application);
        this.apiService = apiService;
        this.cityDao = db.cityDao();
        this.weatherDao = db.weatherDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CityEntry>> gelAllCities(){
        return cityDao.gelAllCities();
    }

    //CityPart

    public LiveData<Integer> getIdLocal(String local){
        return cityDao.getGlobalIdLocal(local);
    }

    public LiveData<CityEntry> getCity(String local){
        return cityDao.getCity(local);
    }

    public void insertCities(){
        executor.execute(() -> apiService.getDistrict().enqueue(new Callback<DistrictInfo>() {
            @Override
            public void onResponse(Call<DistrictInfo> call, Response<DistrictInfo> response) {
                ArrayList<CityEntry> cityEntries = response.body().getData();
                for (int i = 0; i<cityEntries.size(); i++){
                    int finalI = i;
                    executor.execute(() -> {
                        CityEntry entry = cityEntries.get(finalI);
                        if (cityDao.getCity(entry.getLocal()) == null) cityDao.insert(entry);
                    });
                }
            }
            @Override
            public void onFailure(Call<DistrictInfo> call, Throwable t) {

            }
        }));
    }

    //WeatherPart
    public LiveData<List<Weather>> getAllWeathers(){
        return weatherDao.gelAllWeathers();
    }

    public LiveData<Weather> getWeather(String longitude, String latitude, String forecastDate){
        return weatherDao.getWeather(longitude, latitude, forecastDate);
    }

    public LiveData<Integer> getIdWeatherType(String longitude, String latitude, String forecastDate){
        return weatherDao.getIdWeatherType(longitude, latitude, forecastDate);
    }

    public void insertWeathers(int idLocal){
        executor.execute(() -> apiService.getWeather(idLocal).enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                ArrayList<Weather> weatherInfos = response.body().getData();
                for (int i = 0; i<weatherInfos.size(); i++){
                    int finalI = i;
                    executor.execute(() -> {
                        Weather weather = weatherInfos.get(finalI);
                        Weather weatherInDao = weatherDao.getWeather(weather.getLongitude(), weather.getLatitude(), weather.getForecastDate()).getValue();

                        if (weatherInDao == null)  weatherDao.insert(weather);
                        else{
                            int id = weatherInDao.getId();
                            String precipitaProb = weather.getPrecipitaProb();
                            String tMin = weather.getMin();
                            String tMax = weather.getMax();
                            String predWindDir = weather.getPredWindDir();
                            int idWeatherType = weather.getIdWeatherType();
                            int classWindSpeed = weather.getClassWindSpeed();
                            String longitude = weather.getLongitude();
                            String forecastDate = weather.getForecastDate();
                            String latitude = weather.getLatitude();
                            Weather weather1 = new Weather(id, precipitaProb, tMin, tMax, predWindDir, idWeatherType, classWindSpeed, longitude, forecastDate, latitude);
                            weatherDao.update(weather1);
                        }

                    });
                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {

            }
        }));
    }

    public void deleteWeathers(){

            List<Weather> weathers = weatherDao.gelAllWeathers().getValue();
            String DATE_FORMAT_PATTERN1 = "yyyy-MM-dd";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = new SimpleDateFormat(DATE_FORMAT_PATTERN1).parse(df.format(new Date()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (int i = 0; i<weathers.size(); i++){
                Weather weather = weathers.get(i);
                Date date1 = null;
                try {
                     date1 = new SimpleDateFormat(DATE_FORMAT_PATTERN1).parse(weather.getForecastDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1.before(date)) {
                    executor.execute(() -> weatherDao.delete(weather));
                }
        }
    }
}
