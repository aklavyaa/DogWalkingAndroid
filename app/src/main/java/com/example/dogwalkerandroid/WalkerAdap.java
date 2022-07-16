package com.example.dogwalkerandroid;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogwalkerandroid.utils.DashboardDogOwnerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WalkerAdap extends RecyclerView.Adapter<WalkerAdap.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<DashboardDogOwnerModel> arrayList;


    public WalkerAdap(Context context,ArrayList<DashboardDogOwnerModel> arrayList){
        this.mInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_walker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.timing.setText(arrayList.get(position).getAvailability());
        if (arrayList.get(position).getImage()!=null){
            if (!arrayList.get(position).getImage().isEmpty()){
                Picasso.get().load(arrayList.get(position).getImage()).into(holder.profile_image);
            }
        }
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,timing;
        ImageView profile_image;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            timing = itemView.findViewById(R.id.timing);
            profile_image = itemView.findViewById(R.id.profile_image);


        }


    }
}
