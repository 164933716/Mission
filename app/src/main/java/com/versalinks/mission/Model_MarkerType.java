package com.versalinks.mission;

import android.os.Parcel;
import android.os.Parcelable;

import com.contrarywind.interfaces.IPickerViewData;

import io.realm.RealmObject;

public class Model_MarkerType extends RealmObject implements IPickerViewData, Parcelable {
    public String name;

    public Model_MarkerType() {

    }
    public Model_MarkerType(String name) {
        this.name=name;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected Model_MarkerType(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Model_MarkerType> CREATOR = new Parcelable.Creator<Model_MarkerType>() {
        @Override
        public Model_MarkerType createFromParcel(Parcel source) {
            return new Model_MarkerType(source);
        }

        @Override
        public Model_MarkerType[] newArray(int size) {
            return new Model_MarkerType[size];
        }
    };
}
