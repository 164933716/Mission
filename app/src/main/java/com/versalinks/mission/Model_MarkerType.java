package com.versalinks.mission;

import com.contrarywind.interfaces.IPickerViewData;

import io.realm.RealmObject;

public class Model_MarkerType extends RealmObject implements IPickerViewData {
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
}
