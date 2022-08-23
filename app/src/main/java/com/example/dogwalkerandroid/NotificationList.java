package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.dogwalkerandroid.utils.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationList extends AppCompatActivity implements NotificationAdap.clickdelete {


    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private NotificationAdap adap;
    private String TAG = NotificationList.class.getSimpleName();
    private String docId = "";
    private ArrayList<NotificationModel> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        db = FirebaseFirestore.getInstance();


        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                adap.notifyDataSetChanged();
            }
        });
        adap = new NotificationAdap(this,list,this);
         recyclerView = findViewById(R.id.recylcer_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adap);
        db.collection("DogOwner").whereEqualTo("user_id",SharedPref.getString("user_id","")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e(TAG, document.getId() + " => " + document.getData());
                        docId = document.getId();

                    }

                    db.collection("DogOwner").document(docId).collection("WalkerRequest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            for (QueryDocumentSnapshot snap : task.getResult()){
                               String availability = snap.getData().get("availablity").toString();
                               String exp = snap.getData().get("experience").toString();
                               String hrs =  snap.getData().get("hrs").toString();
                               String price =  snap.getData().get("price").toString();
                               String username =  snap.getData().get("user_name").toString();
                               list.add(new NotificationModel(username,exp,hrs,price,availability));
                            }


                            adap.notifyDataSetChanged();

                        }
                    });


                }
            }
        });

    }

    @Override
    public void deleteitem(int position) {
        list.remove(position);
        adap.notifyDataSetChanged();

    }
}