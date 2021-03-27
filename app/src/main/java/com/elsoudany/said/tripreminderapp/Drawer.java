package com.elsoudany.said.tripreminderapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.elsoudany.said.tripreminderapp.auth.Login;
import com.elsoudany.said.tripreminderapp.history.HistoryFragment;
import com.elsoudany.said.tripreminderapp.upcomingtrips.ProcessingTripsActivity;
import com.elsoudany.said.tripreminderapp.upcomingtrips.UpcomingTripsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    Toolbar toolbar;
   TextView userEmail;
   View headerView;
    String email;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        // shared........27/3
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferencesConfig = getSharedPreferences("status", MODE_PRIVATE);
            preferencesConfig.edit().clear().commit();
            Intent intent = new Intent(Drawer.this, Login.class);
            startActivity(intent);
            finish();
        }

       firebaseAuth = FirebaseAuth.getInstance();
       email = firebaseAuth.getCurrentUser().getEmail();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_id);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //access header view in navigation
        headerView=navigationView.getHeaderView(0);
        userEmail=headerView.findViewById(R.id.txt_userEmail);
        userEmail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //selected item
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UpcomingTripsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_Upcoming);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Upcoming:
                // Show Upcoming Trips Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UpcomingTripsFragment()).commit();
                break;
            case R.id.nav_history:
                // Show History Trips Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
                break;
            case R.id.nav_sync:
                // Sync to firebase
                Toast.makeText(this, "nav_sync", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "nav_logout", Toast.LENGTH_SHORT).show();
                //Alert Dialog for logout
                final Dialog dialog = new Dialog(Drawer.this);
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
                        Intent intent = new Intent(Drawer.this, Login.class);
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



                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}