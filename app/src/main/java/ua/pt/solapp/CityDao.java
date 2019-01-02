package ua.pt.solapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CityDao {

    @Insert
    void insert(CityEntry cityEntry);

    @Query("DELETE FROM city")
    void deleteAll();

    @Query("SELECT * FROM city")
    LiveData<List<CityEntry>> gelAllCities();

    @Query("SELECT globalIdLocal FROM city WHERE local = :local")
    LiveData<Integer> getGlobalIdLocal(String local);


}
