package com.example.dogwalkerandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dogwalkerandroid.utils.DashboardDogOwnerModel;

import java.util.ArrayList;

public class DailyWalker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_walker);

        RecyclerView recyclerView = findViewById(R.id.recylcer_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new WalkerAdap(this));
        recyclerView.setAdapter(new WalkerAdap(this,new ArrayList<DashboardDogOwnerModel>()));


    }
}