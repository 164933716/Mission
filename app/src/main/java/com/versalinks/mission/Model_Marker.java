package com.versalinks.mission;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Model_Marker extends RealmObject implements Parcelable {
    public long createTime;
    @PrimaryKey
    public String name;
    public Model_MarkerType type;
    public Model_GPS gps;
    public RealmList<String> photos;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeString(this.name);
        dest.writeParcelable(this.type, flags);
        dest.writeParcelable(this.gps, flags);
        dest.writeList(this.photos);
    }

    public Model_Marker() {
    }

    protected Model_Marker(Parcel in) {
        this.createTime = in.readLong();
        this.name = in.readString();
        this.type = in.readParcelable(Model_MarkerType.class.getClassLoader());
        this.gps = in.readParcelable(Model_GPS.class.getClassLoader());
        this.photos = new RealmList<>();
        in.readList(this.photos, String.class.getClassLoader());
    }

    public static final Parcelable.Creator<Model_Marker> CREATOR = new Parcelable.Creator<Model_Marker>() {
        @Override
        public Model_Marker createFromParcel(Parcel source) {
            return new Model_Marker(source);
        }

        @Override
        public Model_Marker[] newArray(int size) {
            return new Model_Marker[size];
        }
    };
}
