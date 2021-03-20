package com.elsoudany.said.tripreminderapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.elsoudany.said.tripreminderapp.room.AppDatabase;
import com.elsoudany.said.tripreminderapp.room.Trip;
import com.elsoudany.said.tripreminderapp.room.TripDAO;
import com.elsoudany.said.tripreminderapp.room.User;
import com.elsoudany.said.tripreminderapp.room.UserDAO;
import com.elsoudany.said.tripreminderapp.room.UserTrip;
import com.elsoudany.said.tripreminderapp.room.UserTripDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProcessingTripsActivity extends AppCompatActivity {
    ArrayList<Trip> processingTripList = new ArrayList<>();
    RecyclerView processingTripListView;
    TripsAdapter tripsAdapter;
    MyHandler handler;
    TripDAO tripDAO;
    UserDAO userDAO;
    UserTripDAO userTripDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_trips);
        processingTripListView = findViewById(R.id.processingTripList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        processingTripListView.setLayoutManager(layoutManager);
        tripsAdapter = new TripsAdapter(this,processingTripList);
        processingTripListView.setAdapter(tripsAdapter);
        handler = new MyHandler();
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"DataBase-name").build();
                tripDAO = db.tripDAO();
                userTripDAO = db.userTripDAO();
                userDAO = db.userDAO();
                User user1 = new User("55");
                Trip trip1 = new Trip("trip10","Alex","Cairo","5/10/2021","5:10pm","55","processing");
                Trip trip2 = new Trip("trip20","Alex","Cairo","5/10/2021","5:10pm","55","processing");
                userDAO.insertAll(user1);
                tripDAO.insertAll(trip1,trip2);
                List<UserTrip> tripList = userTripDAO.getAllTrips("55");

                processingTripList.addAll((ArrayList<Trip>) tripList.get(0).tripList);
                processingTripList.removeIf(new Predicate<Trip>() {
                    @Override
                    public boolean test(Trip trip) {
                        return !trip.status.equals("processing");
                    }
                });
                handler.sendEmptyMessage(1);
            }
        }.start();


    }
    public class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            tripsAdapter.notifyDataSetChanged();
        }
    }
}