package ua.pt.solapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {CityEntry.class}, version = 1)
public abstract class CityDatabase extends RoomDatabase {

    public abstract CityDao cityDao();

    private static volatile CityDatabase instance;

    public static CityDatabase getDatabase(final Context context){
        if (instance==null){
            synchronized (CityDatabase.class){
                if (instance==null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), CityDatabase.class, "city_database").build();
                }
            }
        }
        return instance;
    }

}
