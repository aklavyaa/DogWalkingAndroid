package com.example.dogwalkerandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogwalkerandroid.utils.DashboardDogWalkerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OwnerAdap extends RecyclerView.Adapter<OwnerAdap.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<DashboardDogWalkerModel> listOwner;

    public OwnerAdap(Context context, ArrayList<DashboardDogWalkerModel> listOwner){
        this.mInflater = LayoutInflater.from(context);
        this.listOwner = listOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(listOwner.get(position).getName());
        holder.age.setText(listOwner.get(position).getAge());
        holder.address.setText(listOwner.get(position).getAddress());
        holder.availability.setText(listOwner.get(position).getAvailability());

        if (listOwner.get(position).getImage()!=null){
            if (!listOwner.get(position).getImage().isEmpty()){
                Picasso.get().load(listOwner.get(position).getImage()).into(holder.image);
            }
        }
    }



    @Override
    public int getItemCount() {
        return listOwner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,age,address,availability,totalHrs,totalPrice;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.rating_no);
            address = itemView.findViewById(R.id.address);
            availability = itemView.findViewById(R.id.timing);
            totalHrs = itemView.findViewById(R.id.experience);
            totalPrice = itemView.findViewById(R.id.rate);
            image = itemView.findViewById(R.id.profile_image);

        }


    }
}
