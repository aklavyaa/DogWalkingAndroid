package com.example.dogwalkerandroid.utils;

public class DogOwnerModel {
    private String id;
    private String name;
    private String age;
    private String address;
    private String imageUrl;
    private Boolean isEnable;


    public DogOwnerModel(String id, String name, String age, String address, String imageUrl,Boolean isEnable) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.imageUrl = imageUrl;
        this.isEnable = isEnable;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }
}
