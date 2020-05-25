package com.versalinks.mission;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Model_Route extends RealmObject implements Parcelable {
    public long createTime;
    public long goDuration;
    @PrimaryKey
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
        return "Model_Route{" +
                "createTime=" + createTime +
                ", goDuration=" + goDuration +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", goMode='" + goMode + '\'' +
                ", goDifficult='" + goDifficult + '\'' +
                ", distance=" + distance +
                ", goUp=" + goUp +
                ", goDown=" + goDown +
                ", gpsList=" + (gpsList==null?"null":gpsList.size()) +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeLong(this.goDuration);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.goMode);
        dest.writeString(this.goDifficult);
        dest.writeDouble(this.distance);
        dest.writeDouble(this.goUp);
        dest.writeDouble(this.goDown);
        dest.writeList(this.gpsList);
    }

    public Model_Route() {
    }

    protected Model_Route(Parcel in) {
        this.createTime = in.readLong();
        this.goDuration = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.goMode = in.readString();
        this.goDifficult = in.readString();
        this.distance = in.readDouble();
        this.goUp = in.readDouble();
        this.goDown = in.readDouble();
        this.gpsList = new RealmList<Model_GPS>();
        in.readList(this.gpsList, Model_GPS.class.getClassLoader());
    }

    public static final Parcelable.Creator<Model_Route> CREATOR = new Parcelable.Creator<Model_Route>() {
        @Override
        public Model_Route createFromParcel(Parcel source) {
            return new Model_Route(source);
        }

        @Override
        public Model_Route[] newArray(int size) {
            return new Model_Route[size];
        }
    };
}
