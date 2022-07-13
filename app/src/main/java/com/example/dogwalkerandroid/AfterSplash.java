package com.example.dogwalkerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class AfterSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_splash);
        SharedPref.init(AfterSplash.this);

        findViewById(R.id.dogowner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.putString(SharedPref.USER_STATE,SharedPref.DOGOWNER);
                navigateToLogin();
            }
        });

        findViewById(R.id.dogwalker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.putString(SharedPref.USER_STATE,SharedPref.DOGWALKER);
                navigateToLogin();
            }
        });

        findViewById(R.id.admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.putString(SharedPref.USER_STATE,SharedPref.ADMIN);
                navigateToLogin();

            }
        });
    }

    private void navigateToLogin(){
        startActivity(new Intent(AfterSplash.this,Login.class));

    }
}