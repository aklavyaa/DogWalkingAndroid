package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dogwalkerandroid.utils.DashboardDogWalkerModel;
import com.example.dogwalkerandroid.utils.DogOwnerModel;
import com.example.dogwalkerandroid.utils.DogWalkerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DashboardWalker extends AppCompatActivity implements OwnerAdap.clickApplyReject {

    private RecyclerView recyclerView;
    private OwnerAdap adap;
    private ArrayList<DashboardDogWalkerModel> listOwner = new ArrayList<>();
    private String TAG = DashboardWalker.class.getSimpleName();
    private FirebaseFirestore db;
    private HashSet<String> set = new HashSet();
    private ArrayList<DashboardDogWalkerModel> testSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_walker);
        db = FirebaseFirestore.getInstance();
        adap = new OwnerAdap(DashboardWalker.this,listOwner,DashboardWalker.this);

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


        db.collection("DogWalker").document(SharedPref.getString("user_id","")).collection("OwnerRequest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot document = task.getResult();
                    set.clear();
                    testSet.clear();
                    for ( DocumentSnapshot snap : document){
                       String userid = snap.get("user_id").toString();
                       String total_cost = snap.get("total_cost").toString();
                       String total_time = snap.get("total_time").toString();
                       String order_id = snap.get("order_id").toString();
                        String date = snap.get("date").toString();
                        String time = snap.get("time").toString();


                        set.add(userid);
                       testSet.add(new DashboardDogWalkerModel("","","Available on "+date +" at "+time,"","","",total_time,order_id,total_cost));

                    }

                    for (int i = 0; i< set.size();i++){

                       String total_time =  testSet.get(i).getTotalPrice();
                      String total_cost =  testSet.get(i).getTotalHrs();
                      String orderid =  testSet.get(i).getPrice();
                      String availability =  testSet.get(i).getAvailability();




                        db.collection("DogOwner").whereIn("user_id", (ArrayList)set.stream().collect(Collectors.toList())).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
//                                        String date = document.getData().get("date").toString();
//                                        String timingFrom = document.getData().get("timingFrom").toString();
//                                        String timingTo = document.getData().get("timingTo").toString();


//                                        String timing = document.getData().get("timing").toString();

                                        String userImage = document.getData().get("user_image").toString();
                                        Boolean isEnable = (Boolean)document.getData().get("isEnable");
                                        Boolean isReserved = (Boolean)document.getData().get("isReserved");



                                        Log.e(TAG, "totalcoest and time: "+total_cost+" - "+total_time );
                                        listOwner.add(new DashboardDogWalkerModel(id,username,availability,address,age,userImage,total_time,orderid,total_cost));
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
            }
        });





    }

    @Override
    public void clickApply(String userid,String orderid) {
        db.collection("DogWalker").document(SharedPref.getString("user_id","")).collection("OwnerRequest").document(orderid).update("status","Accpeted").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DashboardWalker.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}