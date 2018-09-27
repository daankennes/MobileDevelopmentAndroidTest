package velo.daank.com.velolijst;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StationDao {

    @Query("SELECT * FROM station")
    List<Station> getAll();

    @Query("SELECT * FROM station WHERE id IN (:stationIds)")
    List<Station> loadAllByIds(int[] stationIds);

    @Query("SELECT * FROM station WHERE id = :id LIMIT 1")
    Station findById(int id);

    @Query("SELECT * FROM station WHERE name = :name LIMIT 1")
    Station findByName(String name);

    @Insert
    void insertAll(Station... stations);

    /*@Delete
    void delete(Station station);*/
}
