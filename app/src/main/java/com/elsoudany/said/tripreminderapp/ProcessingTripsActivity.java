package com.elsoudany.said.tripreminderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProcessingTripsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1005;
    ArrayList<Trip> processingTripList = new ArrayList<>();
    RecyclerView processingTripListView;
    TripsAdapter tripsAdapter;
    MyHandler handler;
    TripDAO tripDAO;
    UserDAO userDAO;
    UserTripDAO userTripDAO;
    FloatingActionButton fab;

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
        fab = findViewById(R.id.addBtn);
        fab.setOnClickListener(view ->{
            startActivityForResult(new Intent(this,AddTripActivity.class),REQUEST_CODE);
        });
        handler = new MyHandler();
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"DataBase-name").build();
                tripDAO = db.tripDAO();
                userTripDAO = db.userTripDAO();
                userDAO = db.userDAO();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String id = firebaseAuth.getCurrentUser().getUid();
                User user1 = new User(id);

                userDAO.insertAll(user1);

                List<UserTrip> tripList = userTripDAO.getAllTrips(id);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("radio");
                String start = data.getStringExtra("startPoint");
                String end = data.getStringExtra("endPoint");
                String date = data.getStringExtra("date");
                String time = data.getStringExtra("time");
                String name = data.getStringExtra("tripName");
                String userid = data.getStringExtra("userId");
                String status = data.getStringExtra("status");
                Trip addedTrip = new Trip(name,start,end,date,time,userid,status);
                processingTripList.add(addedTrip);
                tripsAdapter.notifyDataSetChanged();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        tripDAO.insertAll(addedTrip);
                    }
                }.start();
            }
        }
    }
}