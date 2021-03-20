package com.elsoudany.said.tripreminderapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Trip.class, User.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripDAO tripDAO();
    public abstract UserTripDAO userTripDAO();
    public abstract UserDAO userDAO();

}
