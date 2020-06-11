package com.versalinks.mission;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Model_Route extends RealmObject implements Parcelable {
    public long createTime;//创建时间
    public long goDuration;//耗时
    @PrimaryKey
    public String name;//名称
    public String description;//描述
    public String goDifficulty;//难易度
    public double distance;//路程
    public double distanceByAltitude;//起始点高度差
    public double altitudeMin;//最低海拔
    public double altitudeMax;//最高海拔
    public double goUp;//上坡
    public double goDown;//下坡
    public RealmList<Model_GPS> gpsList;//gps
    public RealmList<String> modeList;//gps
    public RealmList<String> goMode;//gps

    @Override
    public String toString() {
        return "Model_Route{" +
                "createTime=" + createTime +
                ", goDuration=" + goDuration +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", goMode='" + goMode + '\'' +
                ", goDifficult='" + goDifficulty + '\'' +
                ", distance=" + distance +
                ", distanceByAltitude=" + distanceByAltitude +
                ", altitudeMin=" + altitudeMin +
                ", altitudeMax=" + altitudeMax +
                ", goUp=" + goUp +
                ", goDown=" + goDown +
                ", gpsList=" + (gpsList == null ? "null" : gpsList.size()) +
                ", modeList=" + (modeList == null ? "null" : modeList.size()) +
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
        dest.writeString(this.goDifficulty);
        dest.writeDouble(this.distance);
        dest.writeDouble(this.distanceByAltitude);
        dest.writeDouble(this.altitudeMin);
        dest.writeDouble(this.altitudeMax);
        dest.writeDouble(this.goUp);
        dest.writeDouble(this.goDown);
        dest.writeList(this.gpsList);
        dest.writeList(this.modeList);
        dest.writeList(this.goMode);
    }

    public Model_Route() {
    }

    protected Model_Route(Parcel in) {
        this.createTime = in.readLong();
        this.goDuration = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.goDifficulty = in.readString();
        this.distance = in.readDouble();
        this.distanceByAltitude = in.readDouble();
        this.altitudeMin = in.readDouble();
        this.altitudeMax = in.readDouble();
        this.goUp = in.readDouble();
        this.goDown = in.readDouble();
        this.gpsList = new RealmList<>();
        this.modeList = new RealmList<>();
        this.goMode = new RealmList<>();
        in.readList(this.gpsList, Model_GPS.class.getClassLoader());
        in.readList(this.modeList, String.class.getClassLoader());
        in.readList(this.goMode, String.class.getClassLoader());
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
