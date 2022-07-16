package com.example.dogwalkerandroid.utils;

public class DashboardDogWalkerModel {

    private String id;
    private String name;
    private String availability;
    private String address;
    private String age;
    private String image;
    private String totalHrs;
    private String price;
    private String totalPrice;


    public DashboardDogWalkerModel(String id, String name, String availability,
                                   String address, String age, String image,
                                   String totalHrs, String price, String totalPrice) {
        this.id = id;
        this.name = name;
        this.availability = availability;
        this.address = address;
        this.age = age;
        this.image = image;
        this.totalHrs = totalHrs;
        this.price = price;
        this.totalPrice = totalPrice;
    }


    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getImage() {
        return image;
    }

    public String getTotalHrs() {
        return totalHrs;
    }

    public String getPrice() {
        return price;
    }

    public String getTotalPrice() {
        return totalPrice;
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

}
