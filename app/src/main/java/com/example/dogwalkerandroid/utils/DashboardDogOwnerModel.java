package com.example.dogwalkerandroid.utils;

public class DashboardDogOwnerModel {

    private String id;
    private String name;
    private Boolean isEnable;
    private String address;
    private String age;
    private String description;
    private String email;
    private String image;
    private String password;
    private String availability;

    public DashboardDogOwnerModel(String id, String name, Boolean isEnable,
                                  String address,String age,String description,
                                  String email,String image,
                                  String password,String availability) {
        this.id = id;
        this.name = name;
        this.isEnable = isEnable;
        this.address = address;
        this.age = age;
        this.description = description;
        this.email = email;
        this.image = image;
        this.password = password;
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getPassword() {
        return password;
    }
}
