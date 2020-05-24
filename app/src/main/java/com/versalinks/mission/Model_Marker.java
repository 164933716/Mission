package com.versalinks.mission;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Model_Marker extends RealmObject {
    public long createTime;
    public String markerName;
    public Model_MarkerType markerType;
    public Model_GPS gps;
    public RealmList<String> photos;

}
