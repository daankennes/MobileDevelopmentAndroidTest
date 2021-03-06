package velo.daank.com.velolijst;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Station.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StationDao stationDao();
}
