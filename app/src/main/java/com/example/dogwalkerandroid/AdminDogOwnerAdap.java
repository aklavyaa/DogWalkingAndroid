package com.example.dogwalkerandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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

import com.example.dogwalkerandroid.utils.DogOwnerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminDogOwnerAdap  extends RecyclerView.Adapter<AdminDogOwnerAdap.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<DogOwnerModel> list;
    private FirebaseFirestore db;
    private Context context;

    public AdminDogOwnerAdap(Context context,ArrayList<DogOwnerModel> list){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.admin_item_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.id.setText(list.get(position).getId());
        holder.name.setText(list.get(position).getName());
        holder.yrs.setText(list.get(position).getAge());
        holder.address.setText(list.get(position).getAddress());

        if (list.get(position).getEnable()!=null){
            holder.enable_disable.setChecked((Boolean) list.get(position).getEnable());
        }
        holder.enable_disable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.collection("DogOwner").whereEqualTo("id",list.get(position).getId().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String docId = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                docId = document.getId();
                            }
                            db.collection("DogOwner").document(docId).update("isEnable",isChecked).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        if (!list.get(position).getImageUrl().isEmpty()){
            Picasso.get().load(list.get(position).getImageUrl()).into(holder.profile_image);

        }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView yrs;
        TextView address;
        ImageView profile_image;
        Switch enable_disable;

        ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.dogid);
            name = itemView.findViewById(R.id.name);
            yrs = itemView.findViewById(R.id.rating_no);
            address = itemView.findViewById(R.id.address);
            profile_image = itemView.findViewById(R.id.profile_image);
            enable_disable = itemView.findViewById(R.id.enable_disable);


        }


    }
}
