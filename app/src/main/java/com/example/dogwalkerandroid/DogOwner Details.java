package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class OwnerDescription extends AppCompatActivity {


    private String userid = "";
    private FirebaseFirestore db;
    String availability = "";
    String price = "";
    String hrs = "";
    String address = "";
    String orderid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_description);


       userid=  getIntent().getStringExtra("userid");
       orderid = getIntent().getStringExtra("orderid");
       availability = getIntent().getStringExtra("availability");
       price = getIntent().getStringExtra("price");
        hrs = getIntent().getStringExtra("hrs");
        address = getIntent().getStringExtra("address");


        ((Button)findViewById(R.id.accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.collection("DogWalker").document(SharedPref.getString("user_id","")).collection("OwnerRequest").document(orderid).update("status","Accpeted").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(DashboardWalker.this, "Updated", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(OwnerDescription.this,RequestSent.class));


                        db.collection("DogWalker").document(SharedPref.getString("user_id","")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                ;
//                               ;
//hrs;
//        price;
//        availability;


                                Map<String, Object> docData = new HashMap<>();
                                docData.put("user_name", task.getResult().getData().get("user_name"));
                                docData.put("experience",  task.getResult().getData().get("experience"));
                                docData.put("hrs", hrs);
                                docData.put("price", price);
                                docData.put("availablity", availability);



                                db.collection("DogOwner").document(userid).collection("WalkerRequest").add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                });





                            }
                        });


                    }
                });

            }
        });



        db = FirebaseFirestore.getInstance();
        db.collection("DogOwner").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                   DocumentSnapshot snaphot =  task.getResult();





Picasso.get().load(snaphot.getData().get("user_image").toString()).into((ImageView) findViewById(R.id.image));
                    ((TextView)findViewById(R.id.price)).setText(snaphot.getData().get("user_name").toString());
                    ((TextView)findViewById(R.id.rating)).setText(availability);
                    ((TextView)findViewById(R.id.name)).setText("$"+price);
                    ((TextView)findViewById(R.id.availability)).setText(hrs+" hrs");
                    ((TextView)findViewById(R.id.description_heading)).setText(address);




                }
            }
        });


    }
}
