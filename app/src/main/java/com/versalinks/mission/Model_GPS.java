package com.versalinks.mission;

import androidx.annotation.NonNull;

import com.amap.api.maps.model.LatLng;

import io.realm.RealmObject;

public class Model_GPS extends RealmObject {
    public double latitude;
    public double longitude;
    public double altitude;

    public Model_GPS(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
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
                ", altitude=" + altitude +
                '}';
    }

    @NonNull
    public String toShow() {
        return "{" + latitude + ", " + longitude + ", " + altitude + '}';
    }
}
