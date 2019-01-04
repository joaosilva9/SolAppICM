package ua.pt.solapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {CityEntry.class, Weather.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract CityDao cityDao();

    public abstract WeatherDao weatherDao();

    private static volatile MyDatabase instance;

    public static MyDatabase getDatabase(final Context context){
        if (instance==null){
            synchronized (MyDatabase.class){
                if (instance==null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, "database").build();
                }
            }
        }
        return instance;
    }
}
