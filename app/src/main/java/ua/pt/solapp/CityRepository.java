package ua.pt.solapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class CityRepository {
    private CityDao cityDao;
    private LiveData<List<CityEntry>> allCities;
    private LiveData<Integer> globalIdLocal;

    CityRepository(Application application){
        CityDatabase db = CityDatabase.getDatabase(application);
        cityDao = db.cityDao();
        allCities = cityDao.gelAllCities();
        globalIdLocal = null;
    }

    LiveData<List<CityEntry>> getAllCities(){
        return allCities;
    }

    LiveData<Integer> getGlobalIdLocal(String local){
        globalIdLocal = cityDao.getGlobalIdLocal(local);
        return globalIdLocal;
    }

    public void insert (CityEntry cityEntry){
        new insertAsyncTask(cityDao).execute(cityEntry);

    }

    private static class insertAsyncTask extends AsyncTask<CityEntry, Void, Void> {

        private CityDao mAsyncTaskDao;

        insertAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CityEntry... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
