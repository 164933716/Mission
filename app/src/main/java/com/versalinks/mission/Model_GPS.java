package com.versalinks.mission;

import com.amap.api.maps.model.LatLng;

public class Model_GPS {
    public double latitude;
    public double longitude;
    public double altitude;

    public Model_GPS(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    public LatLng getLatLng(){
        return new LatLng(latitude,longitude);
    }
}
