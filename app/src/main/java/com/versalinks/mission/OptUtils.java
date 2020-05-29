package com.versalinks.mission;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptUtils {
    public static String rotateByLeft() {
        return "javascript:optMap(" + getJson("rotateByLeft", "30") + ")";
    }

    public static String rotateByRight() {
        return "javascript:optMap(" + getJson("rotateByRight", "30") + ")";
    }

    public static String rotateByUp() {
        return "javascript:optMap(" + getJson("rotateByUp", "30") + ")";
    }

    public static String rotateByDown() {
        return "javascript:optMap(" + getJson("rotateByDown", "30") + ")";
    }

    public static String pointToNorth() {
        return "javascript:optMap(" + getJson("pointToNorth", null) + ")";
    }

    public static String zoomIn() {
        return "javascript:optMap(" + getJson("zoomIn", null) + ")";
    }

    public static String zoomOut() {
        return "javascript:optMap(" + getJson("zoomOut", null) + ")";
    }

    public static String recenter() {
        return "javascript:recenter()";
    }

    public static String updateLocation(Model_GPS modelGps) {
        return "javascript:updateUserLocation(" + getJson(modelGps) + ")";
    }

    public static String flyTo(Model_GPS modelGps) {
        return "javascript:flyTo(" + getJson(modelGps) + ")";
    }

    public static String flyStart() {
        return "javascript:flyThroughStart(" + "200,45" + ")";
    }

    public static String flyStop() {
        return "javascript:flyThroughStop(" + "" + ")";
    }

    public static String getUserTourHeights(String jsonPois) {
        return "javascript:getUserTourHeights(" + jsonPois + ")";
    }

    public static String showRoadLayer() {
        Map<String, Object> map = new HashMap<>();
        map.put("sroad", "./sroad.geojson");
        map.put("ssroad", "./ssroad.geojson");
        return "javascript:addRoadBackgroundLayer(" + getJson(map) + ")";
    }

    public static String hideRoadLayer() {
        return "javascript:removeRoadBackgroundLayer(" + "" + ")";
    }

    public static String updateUserTour(List<Model_GPS> gpsList) {
        return "javascript:updateUserTour(" + getJson(gpsList) + ")";
    }

    public static String updatePoiLocation(Model_Marker modelMarker) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name", modelMarker.name);
        map.put("latitude", modelMarker.gps.latitude);
        map.put("longitude", modelMarker.gps.longitude);
        map.put("thumbnail", "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg");
        list.add(map);
        String json = getJson(list);
        return "javascript:updatePoiLocation(" + json + ")";
    }

    public static String clearPoiLocation() {
        return "javascript:clearPoiLocation()";
    }

    public static String clearUserTour() {
        return "javascript:clearUserTour()";
    }

    public static String getJson(Object model) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
//        LogUtils.e(json);
        return json;
    }

    public static String getJson(String action, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", action);
        if (data != null) {
            map.put("data", data);
        }
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
