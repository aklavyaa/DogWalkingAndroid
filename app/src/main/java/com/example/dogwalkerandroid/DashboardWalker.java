package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.dogwalkerandroid.utils.DashboardDogWalkerModel;
import com.example.dogwalkerandroid.utils.DogOwnerModel;
import com.example.dogwalkerandroid.utils.DogWalkerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardWalker extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OwnerAdap adap;
    private ArrayList<DashboardDogWalkerModel> listOwner = new ArrayList<>();
    private String TAG = DashboardWalker.class.getSimpleName();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_walker);
        db = FirebaseFirestore.getInstance();
        adap = new OwnerAdap(DashboardWalker.this,listOwner);

        recyclerView = findViewById(R.id.recylcer_dog_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adap);


        ((ImageView)findViewById(R.id.filter)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                SharedPref.clearPreference(DashboardWalker.this);
                startActivity(new Intent(DashboardWalker.this,Splash.class));
                finishAffinity();
            }
        });


        db.collection("DogWalker").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData().get);

                        String id = document.getData().get("id").toString();
                        String username = document.getData().get("user_name").toString();
                        String address = document.getData().get("user_address").toString();
                        String age = document.getData().get("user_age").toString();
//                        String userage = document.getData().get("user_age").toString();
                        String date = document.getData().get("date").toString();
                        String timingFrom = document.getData().get("timingFrom").toString();
                        String timingTo = document.getData().get("timingTo").toString();

                        String userImage = document.getData().get("user_image").toString();
                        Boolean isEnable = (Boolean)document.getData().get("isEnable");
                        Boolean isReserved = (Boolean)document.getData().get("isReserved");

                        listOwner.add(new DashboardDogWalkerModel(id,username,"Available from "+date+" "+timingFrom +" - "+timingTo ,address,age,userImage,"","",""));
                        adap.notifyDataSetChanged();

                    }
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}