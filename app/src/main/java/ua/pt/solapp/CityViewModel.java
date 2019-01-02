package ua.pt.solapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

public class CityViewModel extends AndroidViewModel {

    private MediatorLiveData<List<CityEntry>> mCityEntryLive = new MediatorLiveData<>();

    private MediatorLiveData<Integer> mglobalIdLocal = new MediatorLiveData<>();

    private CityDatabase db = CityDatabase.getDatabase(getApplication());

    private CityRepository mRepository;

    private LiveData<List<CityEntry>> allCities;

    private LiveData<Integer> globalIdLocal;


    public CityViewModel (Application application) {
        super(application);
        mRepository = new CityRepository(application);

    }

    LiveData<List<CityEntry>> getAllCities() {
        allCities = db.cityDao().gelAllCities();
        mCityEntryLive.addSource(allCities, new Observer<List<CityEntry>>() {
            @Override
            public void onChanged(@Nullable List<CityEntry> cityEntries) {
                if (cityEntries == null || cityEntries.isEmpty()){
                    // Fetch data from API
                }else{
                    mCityEntryLive.removeSource(allCities);
                    mCityEntryLive.setValue(cityEntries);
                }
            }
        });
        return mCityEntryLive;
    }

    LiveData<Integer> getGlobalIdLocal(String local) {
        globalIdLocal = mRepository.getGlobalIdLocal(local);
        return globalIdLocal;
    }

    public void insert(CityEntry cityEntry) { mRepository.insert(cityEntry); }

}
