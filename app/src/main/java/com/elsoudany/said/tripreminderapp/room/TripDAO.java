package com.elsoudany.said.tripreminderapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDAO {
    @Query("SELECT * FROM trips")
    List<Trip> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Trip... trips);

    @Delete
    void delete(Trip trip);

    @Delete
    void deleteAll(List<Trip> trips);
}

