package com.versalinks.mission;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Model_Record extends RealmObject implements Serializable {
    public long createTime;
    public long goDuration;
    public String name;
    public String description;
    public String goMode;
    public String goDifficult;
    public double distance;
    public double goUp;
    public double goDown;
    public RealmList<Model_GPS> gpsList;

    @Override
    public String toString() {
        return "Model_Record{" +
                "createTime=" + createTime +
                ", goDuration=" + goDuration +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", goMode='" + goMode + '\'' +
                ", goDifficult='" + goDifficult + '\'' +
                ", distance=" + distance +
                ", goUp=" + goUp +
                ", goDown=" + goDown +
                ", gpsList=" + gpsList +
                '}';
    }
}
