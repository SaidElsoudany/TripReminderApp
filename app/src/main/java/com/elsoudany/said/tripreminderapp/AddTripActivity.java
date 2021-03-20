package com.elsoudany.said.tripreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddTripActivity extends AppCompatActivity
{
    //date edit text
    EditText dateText;
    //time edit Text
    EditText timeText;
    //calendar instance
    Calendar calendar;
    //current date variables
    int currentYear,currentMonth,currentDay;
    //current time
    int currentHour,currentMinute;

    //points
    EditText startPoint,endPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_add_trip);

        dateText=findViewById(R.id.txt_date);
        timeText=findViewById(R.id.txt_time);
        startPoint=findViewById(R.id.txt_startPoint);
        endPoint=findViewById(R.id.txt_endPoint);
        calendar=Calendar.getInstance();
        /*-----------------------------------------start point --------------------------*/
        Places.initialize(getApplicationContext(),"AIzaSyCdXqSieoMfWeS3GunOh0FKQzKJnsCWIGM");
        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //initialize place field list
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(AddTripActivity.this);
                startActivityForResult(intent, 100);
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
}