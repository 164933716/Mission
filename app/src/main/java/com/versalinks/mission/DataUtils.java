package com.versalinks.mission;

public class DataUtils {
    private static DataUtils dataUtils;

    private DataUtils() {

    }

    public static DataUtils getInstance() {
        if (dataUtils == null) {
            synchronized (DataUtils.class) {
                if (dataUtils == null) {
                    dataUtils = new DataUtils();
                }
            }
        }
        return dataUtils;
    }
}
