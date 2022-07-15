package com.example.dogwalkerandroid.utils;

import android.service.controls.actions.BooleanAction;

public class DogWalkerModel {
    private String id;
    private String name;
    private String availability;
    private String rating;
    private Boolean isEnable;
    private Boolean isReserver;


    public DogWalkerModel(String id, String name, String availability, String rating,Boolean isEnable,Boolean isReserver) {
        this.id = id;
        this.name = name;
        this.availability = availability;
        this.rating = rating;
        this.isEnable = isEnable;
        this.isReserver = isReserver;
    }


    public Boolean getEnable() {
        return isEnable;
    }

    public Boolean getReserver() {
        return isReserver;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvailability() {
        return availability;
    }

    public String getRating() {
        return rating;
    }
}
