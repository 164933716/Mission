package com.versalinks.mission;

import com.google.gson.Gson;

import java.util.HashMap;
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

    public static String recenter(Model_GPS modelGps) {
        return "javascript:recenter(" + getJson(modelGps) + ")";
    }

    public static String updateLocation(Model_GPS modelGps) {
        return "javascript:updateUserLocation(" + getJson(modelGps) + ")";
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

    public static String updateUserTour(String jsonPois) {
        return "javascript:updateUserTour(" + jsonPois + ")";
    }

    public static String clearUserTour() {
        return "javascript:clearUserTour()";
    }

    public static String getJson(Object model) {
        Gson gson = new Gson();
        return gson.toJson(model);
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
