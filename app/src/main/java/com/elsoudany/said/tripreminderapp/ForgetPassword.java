package com.elsoudany.said.tripreminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elsoudany.said.tripreminderapp.auth.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText editTextEmail;
    Button buttonResetPass;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        editTextEmail=findViewById(R.id.enterEmail);
        buttonResetPass=findViewById(R.id.resetPass);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        auth=FirebaseAuth.getInstance();
        buttonResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String email=editTextEmail.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email Required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter valid email");
            editTextEmail.requestFocus();
            editTextEmail.setText("");
            return;
        }

        progressBar.setVisibility(View.INVISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgetPassword.this, "Please Check Your Email", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgetPassword.this, Login.class));
                }
                else {
                    Toast.makeText(ForgetPassword.this, "Try Again", Toast.LENGTH_LONG).show();
                    editTextEmail.setText("");
                }
            }
        });
    }
}