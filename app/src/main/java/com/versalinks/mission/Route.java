package com.versalinks.mission;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Route extends RealmObject {
    public long time;
    public String routeName;
    public RealmList<Model_GPS> gpsList;
}
