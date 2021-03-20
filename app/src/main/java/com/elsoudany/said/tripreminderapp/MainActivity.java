package com.elsoudany.said.tripreminderapp;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< Updated upstream
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
=======
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
>>>>>>> Stashed changes

public class MainActivity extends AppCompatActivity {

    Button buttonLogout,buttonDrawer;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< Updated upstream

        buttonLogout=findViewById(R.id.Logout);
        buttonDrawer=findViewById(R.id.drawer);
        btn = findViewById(R.id.RecyclerViewBtn);
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,ProcessingTripsActivity.class);
            startActivity(intent);
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.alertdialogsignoutuser);
                dialog.setCancelable(false);
                dialog.show();
                TextView textViewYesLogout = dialog.findViewById(R.id.text_yes_logout);
                TextView textViewNoLogout = dialog.findViewById(R.id.text_no_logout);
                textViewYesLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences preferencesConfig = getSharedPreferences("status", MODE_PRIVATE);
                        preferencesConfig.edit().clear().commit();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();

                    }
                });
                textViewNoLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        buttonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,Drawer.class);
=======
        Button btn=findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,AddTripActivity.class);
>>>>>>> Stashed changes
                startActivity(intent);
            }
        });
    }
}