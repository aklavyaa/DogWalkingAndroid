package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth = FirebaseAuth.getInstance();

        ((Button)findViewById(R.id.reset_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((EditText)findViewById(R.id.email)).getText().toString().isEmpty()){
                    auth.sendPasswordResetEmail(((EditText)findViewById(R.id.email)).getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this
                                        , "We have sent an email to your link. Please set the password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(ForgotPassword.this, "Please enter the email id", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}