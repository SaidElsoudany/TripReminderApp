package com.elsoudany.said.tripreminderapp.upcomingtrips;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.elsoudany.said.tripreminderapp.R;
import com.elsoudany.said.tripreminderapp.reminderwork.ReminderWorker;
import com.elsoudany.said.tripreminderapp.room.AppDatabase;
import com.elsoudany.said.tripreminderapp.room.Trip;
import com.elsoudany.said.tripreminderapp.room.TripDAO;
import com.elsoudany.said.tripreminderapp.room.User;
import com.elsoudany.said.tripreminderapp.room.UserDAO;
import com.elsoudany.said.tripreminderapp.room.UserTrip;
import com.elsoudany.said.tripreminderapp.room.UserTripDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProcessingTripsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1005;
    private static final String TAG = "MYTAG";
    ArrayList<Trip> processingTripList = new ArrayList<>();
    RecyclerView processingTripListView;
    TripsAdapter tripsAdapter;
    TripDAO tripDAO;
    UserDAO userDAO;
    UserTripDAO userTripDAO;
    FloatingActionButton fab;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"DataBase-name").build();
        tripDAO = db.tripDAO();
        userTripDAO = db.userTripDAO();
        userDAO = db.userDAO();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {

                User user1 = new User(id);

                userDAO.insertAll(user1);

                List<UserTrip> tripList = userTripDAO.getAllTrips(id);
                processingTripList.clear();
                processingTripList.addAll((ArrayList<Trip>) tripList.get(0).tripList);
                processingTripList.removeIf(new Predicate<Trip>() {
                    @Override
                    public boolean test(Trip trip) {
                        return !trip.status.equals("processing");
                    }
                });
                tripsAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        SharedPreferences sharedPreferences = getSharedPreferences("checkingComingFromService",MODE_PRIVATE);
        boolean comingFromService = sharedPreferences.getBoolean("comingFromService",false);
        if(hasFocus == true && comingFromService)
        {
            Log.i(TAG, "onWindowFocusChanged: ");
            new Thread()
            {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {

                    List<UserTrip> tripList = userTripDAO.getAllTrips(id);
                    processingTripList.clear();
                    processingTripList.addAll((ArrayList<Trip>) tripList.get(0).tripList);
                    processingTripList.removeIf(new Predicate<Trip>() {
                        @Override
                        public boolean test(Trip trip) {
                            return !trip.status.equals("processing");
                        }
                    });
                    tripsAdapter.notifyDataSetChanged();
                }
            }.start();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("comingFromService",false).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (!Settings.canDrawOverlays(this)) {
            int REQUEST_CODE = 101;
            Toast.makeText(this, "Allow OVERLAY_PERMISSION for floating Dialog", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            myIntent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(myIntent, REQUEST_CODE);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String tripDirection = data.getStringExtra("radio");
                String start = data.getStringExtra("startPoint");
                String end = data.getStringExtra("endPoint");
                String date = data.getStringExtra("date");
                String time = data.getStringExtra("time");
                String tripName = data.getStringExtra("tripName");
                String userId = data.getStringExtra("userId");
                String status = data.getStringExtra("status");
                Trip addedTrip = new Trip(tripName,start,end,date,time,userId,status,tripDirection);
                processingTripList.add(addedTrip);
                tripsAdapter.notifyDataSetChanged();

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        addedTrip.uid = tripDAO.insert(addedTrip);
                        Log.i(TAG, addedTrip.toString());
                        WorkManager mWorkManger = WorkManager.getInstance(getApplicationContext());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time,formatter);
                        Duration duration = Duration.between(LocalDateTime.now(),dateTime);

                        Log.i(TAG, "onCreate: "+ duration);
                        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                                .setInputData(new Data.Builder().putLong("tripUid", addedTrip.uid).
                                        putString("tripName",addedTrip.tripName)
                                        .build())
                                .setInitialDelay(duration)
                                .build();

                        mWorkManger.enqueue(oneTimeWorkRequest);

                    }
                }.start();
            }
        }
    }
}