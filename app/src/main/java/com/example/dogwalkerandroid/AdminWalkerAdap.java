package com.example.dogwalkerandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogwalkerandroid.utils.DogWalkerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminWalkerAdap extends RecyclerView.Adapter<AdminWalkerAdap.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<DogWalkerModel> list;
    private FirebaseFirestore db;

    private Context context;

    public AdminWalkerAdap(Context context, ArrayList<DogWalkerModel> list){
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.admin_item_walker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.id.setText(list.get(position).getId());
        holder.name.setText(list.get(position).getName());
        holder.timing.setText(list.get(position).getAvailability());
        holder.reserveUser.setChecked((Boolean)list.get(position).getReserver());
        holder.enableUser.setChecked((Boolean)list.get(position).getEnable());

        holder.enableUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                db.collection("DogWalker").whereEqualTo("id",list.get(position).getId().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String docId = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                docId = document.getId();
                            }
                            db.collection("DogWalker").document(docId).update("isEnable",isChecked).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });





            }
            }
        );



        holder.reserveUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                db.collection("DogWalker").whereEqualTo("id",list.get(position).getId().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String docId = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                docId = document.getId();
                            }
                            db.collection("DogWalker").document(docId).update("isReserved",isChecked).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id,name,timing;
        ImageView profileView;
        Switch enableUser,reserveUser;


        ViewHolder(View itemView) {
            super(itemView);
           id = itemView.findViewById(R.id.user_id);
           name = itemView.findViewById(R.id.name);
           timing = itemView.findViewById(R.id.timing);
           profileView = itemView.findViewById(R.id.profile_image);
           enableUser = itemView.findViewById(R.id.enable_user);
           reserveUser = itemView.findViewById(R.id.reserver_user);




        }


    }
}
