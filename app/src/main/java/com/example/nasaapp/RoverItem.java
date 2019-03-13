package com.example.nasaapp;

import android.os.Parcel;
import android.os.Parcelable;

public class RoverItem implements Parcelable {

    private String imageID;
    private String imageSource;
    private String solDay;
    private String fullCameraName;
    private String roverName;
    private String earthDate;

    // Overwrite constructor for parcel
    protected RoverItem(Parcel in) {
        imageID = in.readString();
        imageSource = in.readString();
        solDay = in.readString();
        fullCameraName = in.readString();
        roverName = in.readString();
        earthDate = in.readString();
    }

    public static final Creator<RoverItem> CREATOR = new Creator<RoverItem>() {
        @Override
        public RoverItem createFromParcel(Parcel in) {
            return new RoverItem(in);
        }

        @Override
        public RoverItem[] newArray(int size) {
            return new RoverItem[size];
        }
    };

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getSolDay() {
        return solDay;
    }

    public void setSolDay(String solDay) {
        this.solDay = solDay;
    }

    public String getFullCameraName() {
        return fullCameraName;
    }

    public void setFullCameraName(String fullCameraName) {
        this.fullCameraName = fullCameraName;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageID);
        dest.writeString(imageSource);
        dest.writeString(solDay);
        dest.writeString(fullCameraName);
        dest.writeString(roverName);
        dest.writeString(earthDate);
    }

    // Mandatory constructor
    public RoverItem()
    {
    }

}
