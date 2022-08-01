package com.example.dogwalkerandroid.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class DashboardDogOwnerModel implements Parcelable {

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
    private String lat;
    private String lng;
    private String price;
    private String rating;


    protected DashboardDogOwnerModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        byte tmpIsEnable = in.readByte();
        isEnable = tmpIsEnable == 0 ? null : tmpIsEnable == 1;
        address = in.readString();
        age = in.readString();
        description = in.readString();
        email = in.readString();
        image = in.readString();
        password = in.readString();
        availability = in.readString();
        lat = in.readString();
        lng = in.readString();
        price = in.readString();
        rating = in.readString();
    }

    public static final Creator<DashboardDogOwnerModel> CREATOR = new Creator<DashboardDogOwnerModel>() {
        @Override
        public DashboardDogOwnerModel createFromParcel(Parcel in) {
            return new DashboardDogOwnerModel(in);
        }

        @Override
        public DashboardDogOwnerModel[] newArray(int size) {
            return new DashboardDogOwnerModel[size];
        }
    };

    public String getRating() {
        return rating;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getPrice() {
        return price;
    }

    public DashboardDogOwnerModel(String id, String name, Boolean isEnable,
                                  String address, String age, String description,
                                  String email, String image,
                                  String password, String availability,
                                  String lat, String lng, String price, String rating) {
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
        this.lat = lat;
        this.lng = lng;
        this.price = price;
        this.rating = rating;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (isEnable == null ? 0 : isEnable ? 1 : 2));
        parcel.writeString(address);
        parcel.writeString(age);
        parcel.writeString(description);
        parcel.writeString(email);
        parcel.writeString(image);
        parcel.writeString(password);
        parcel.writeString(availability);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeString(price);
        parcel.writeString(rating);
    }
}
