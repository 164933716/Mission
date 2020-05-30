package com.versalinks.mission;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;

import io.realm.RealmObject;

public class Model_GPS extends RealmObject implements Parcelable {
    public double latitude;
    public double longitude;
    public double height;

    public Model_GPS(double longitude,double latitude,  double height) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
    }

    public Model_GPS() {
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return "Model_GPS{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                '}';
    }

    @NonNull
    public String toShow() {
        return "{" + latitude + ", " + longitude+ '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.height);
    }

    protected Model_GPS(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.height = in.readDouble();
    }

    public static final Parcelable.Creator<Model_GPS> CREATOR = new Parcelable.Creator<Model_GPS>() {
        @Override
        public Model_GPS createFromParcel(Parcel source) {
            return new Model_GPS(source);
        }

        @Override
        public Model_GPS[] newArray(int size) {
            return new Model_GPS[size];
        }
    };
}
