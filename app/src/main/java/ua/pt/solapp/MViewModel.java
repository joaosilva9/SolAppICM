package ua.pt.solapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MViewModel extends AndroidViewModel {

    private MediatorLiveData<List<CityEntry>> mCityEntryLive = new MediatorLiveData<>();
    private Repository mRepository;

    //City
    private LiveData<List<CityEntry>> allCities;
    private LiveData<Integer> globalIdLocal;
    private LiveData<CityEntry> city;

    //Weather
    private LiveData<List<Weather>> weathers;
    private LiveData<Weather> weather;
    private LiveData<Integer> idWeatherType;

    private int cacheSize = 10 * 1024 * 1024; // 10 MB
    private Cache cache = new Cache(getApplication().getCacheDir(), cacheSize);

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cache(cache)
            .build();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);

    public MViewModel (Application application) {
        super(application);
        mRepository = new Repository(application, apiService);

    }

    //City

    public LiveData<CityEntry> getCity(String local){
        city = mRepository.getCity(local);
        return city;
    }

    public LiveData<List<CityEntry>> getAllCities() {
        allCities = mRepository.gelAllCities();
        return mCityEntryLive;
    }

    public LiveData<Integer> getGlobalIdLocal(String local) {
        globalIdLocal = mRepository.getIdLocal(local);
        return globalIdLocal;
    }

    public void insertCities() { mRepository.insertCities(); }

    //Weather

    public LiveData<List<Weather>> getWeathers() {
        weathers = mRepository.getAllWeathers();
        return weathers;
    }

    public LiveData<Weather> getWeather(String longitude, String latitude, String forecastDate) {
        weather = mRepository.getWeather(longitude, latitude, forecastDate);
        return weather;
    }

    public LiveData<Integer> getIdWeatherType(String longitude, String latitude, String forecastDate){
        idWeatherType = mRepository.getIdWeatherType(longitude, latitude, forecastDate);
        return idWeatherType;
    }

    public void insertWeathers(int idLocal){
        mRepository.insertWeathers(idLocal);
    }

    public void deleteWeathers(){
        mRepository.deleteWeathers();
    }

}