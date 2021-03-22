package com.elsoudany.said.tripreminderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddTripActivity extends AppCompatActivity
{
    //radio buttons
    RadioButton oneDirectionRadio,roundedRadio;
    //date edit text
    EditText dateText;
    //time edit Text
    EditText timeText;
    //add trip button
   FloatingActionButton addTripButton;
   //trip name
    EditText tripName;
    //calendar instance
    Calendar calendar;
    //current date variables
    int currentYear,currentMonth,currentDay;
    //current time
    int currentHour,currentMinute;

    //points
    EditText startPoint,endPoint;
    //api key
   final int AUTOCOMPLETE_REQUEST_CODE=100;
   //flag to know start or end point
    String point;
    //firebase reference to get user id
    FirebaseAuth firebaseAuth;
    String userId ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_add_trip);
        //radio buttons
        oneDirectionRadio =findViewById(R.id.radio_oneDirection);
        roundedRadio=findViewById(R.id.radio_roundTrip);
        addTripButton =findViewById(R.id.btn_addTrip);
        dateText=findViewById(R.id.txt_date);
        timeText=findViewById(R.id.txt_time);
        tripName=findViewById(R.id.txt_tripName);
        startPoint=findViewById(R.id.txt_startPoint);
        endPoint=findViewById(R.id.txt_endPoint);
        //get userid from firebase
       firebaseAuth = FirebaseAuth.getInstance();
       userId = firebaseAuth.getCurrentUser().getUid();
        //instance from calendar to get current date and time
        calendar=Calendar.getInstance();
        /*-----------------------------------------start point --------------------------*/
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCdXqSieoMfWeS3GunOh0FKQzKJnsCWIGM");
        }
        PlacesClient placesClient = Places.createClient(this);


        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                point="start";
                startGoogleAutoComplete();
            }
        });
        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                point="end";
                startGoogleAutoComplete();
            }
        });

   addTripButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
        String radio="";
      if(oneDirectionRadio.isChecked()){
       radio="one";
       }
        if(roundedRadio.isChecked())
        {
            radio="round";


        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("radio",radio);
        returnIntent.putExtra("tripName",tripName.getText().toString());
        returnIntent.putExtra("startPoint",startPoint.getText().toString());
        returnIntent.putExtra("endPoint",endPoint.getText().toString());
        returnIntent.putExtra("date",dateText.getText().toString());
        returnIntent.putExtra("time",timeText.getText().toString());
        returnIntent.putExtra("userId",userId);
        returnIntent.putExtra("status","processing");
        Toast.makeText(AddTripActivity.this, ""+userId, Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
     });

        /*-----------------------------------------date text --------------------------*/
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //get current date from calendar
                currentYear=calendar.get(Calendar.YEAR);
                currentMonth=calendar.get(Calendar.MONTH);
                currentDay=calendar.get(Calendar.DAY_OF_MONTH);

                //lunch datapicker
                DatePickerDialog datePickerDialog=new DatePickerDialog(AddTripActivity.this,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth)
                            {
                                       //set choosen date to datetext
                                       dateText.setText( dayOfMonth+"/"+month+"/"+"/"+year);
                            }
                        },currentYear,currentMonth,currentDay);

                datePickerDialog.show();


            }
        });
        /*-----------------------------------------time text --------------------------*/
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //get current time from calendar
                currentHour=calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute=calendar.get(Calendar.MINUTE);
                //lunch timepicker
                TimePickerDialog timePickerDialog=new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
                    {
                        timeText.setText(hourOfDay+":"+minute);

                    }
                },currentHour,currentMinute,false);

                  timePickerDialog.show();
            }
        });
    }
    //start google autocomplete api
    public void startGoogleAutoComplete()
    {
        //initialize place field list
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(AddTripActivity.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    //result from autocomplete google api
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
            {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //check points request
                switch(point){
                    case "start":startPoint.setText(place.getAddress());
                                     break;
                    case "end":endPoint.setText(place.getAddress());
                        break;
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}