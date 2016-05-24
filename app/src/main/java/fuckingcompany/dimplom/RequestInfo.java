package fuckingcompany.dimplom;


import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class RequestInfo   {
    private String mRequestTheme;
    private String mRequestDescription;
    private String mRequestAuthor;
    private LatLng mRequestLocation;
    private String mRequestPhoto;
    private String mRequestDate;

    RequestInfo() {
        mRequestTheme = "";
        mRequestAuthor = "";
        mRequestDescription = "";
        mRequestPhoto = "";
        mRequestDate = "";
    }

    public void setRequestTheme(String theme) {
        mRequestTheme = theme;
    }

    public void setContent(String description) {
        mRequestDescription = description;
    }

    public void setAuthor(String author) {
        mRequestAuthor = author;
    }

    public void setLocation(LatLng location) {
        mRequestLocation = location;
    }

    public void setPhoto(String photo) {
        mRequestPhoto = photo;
    }

    public void setDate(String date) {
        mRequestDate = date;
    }

    public String getRequestTheme() {
        return this.mRequestTheme;
    }

    public String getRequestAuthor() {
        return this.mRequestAuthor;
    }

    public String getDescription() {
        return this.mRequestDescription;
    }

    public String getPhoto() {
        return this.mRequestPhoto;
    }

    public LatLng getLocation(){
        return this.mRequestLocation;
    }


    public String getDate() {
        return this.mRequestDate;
    }
}
