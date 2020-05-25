package com.versalinks.mission;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Model_Marker extends RealmObject {
    public long createTime;
    @PrimaryKey
    public String name;
    public Model_MarkerType type;
    public Model_GPS gps;
    public RealmList<String> photos;

}
