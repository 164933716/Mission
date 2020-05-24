package com.versalinks.mission;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Model_Route extends RealmObject {
    public long time;
    public String routeName;
    public RealmList<Model_GPS> gpsList;


    @Override
    public String toString() {
        return "Model_Route{" +
                "time=" + time +
                ", routeName='" + routeName + '\'' +
                ", gpsList=" + gpsList +
                '}';
    }
}
