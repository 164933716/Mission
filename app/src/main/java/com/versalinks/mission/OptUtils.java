package com.versalinks.mission;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

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

    public static String init() {
        return "javascript:init()";
    }

    public static String updateLocation(Model_GPS modelGps) {
        return "javascript:updateUserLocation(" + getJson(modelGps) + ")";
    }

    public static String flyTo(Model_GPS modelGps) {
        Model_GPS item = new Model_GPS(modelGps.longitude, modelGps.latitude, 12000);
        return "javascript:flyTo(" + getJson(item) + ")";
    }

    public static String flyStart() {
        return "javascript:flyThroughStart2(" + "" + ")";
    }

    public static String flyStop() {
        return "javascript:flyThroughStop2(" + "" + ")";
    }

    public static String flyPause() {
        return "javascript:flyThroughPause2(" + "" + ")";
    }

    public static String getUserTourHeights(String jsonPois) {
        return "javascript:getUserTourHeights(" + jsonPois + ")";
    }

    public static String showPlantLayer(String json) {
        return "javascript:addPlantLayer(" + json + ")";
    }

    public static String removePlantLayer() {
        return "javascript:removePlantLayer(" + "" + ")";
    }

    public static String showMountainPeakLayer(String json) {
        return "javascript:addMountainPeakLayer(" + json + ")";
    }

    public static String removeMountainPeakLayer() {
        return "javascript:removeMountainPeakLayer(" + "" + ")";
    }

    public static String showVillageLayer(String json) {
        return "javascript:addVillageLayer(" + json + ")";
    }

    public static String removeVillageLayer() {
        return "javascript:removeVillageLayer(" + "" + ")";
    }

    public static String showAnimalLayer(String json) {
        return "javascript:addAnimalLayer(" + json + ")";
    }

    public static String removeAnimalLayer() {
        return "javascript:removeAnimalLayer(" + "" + ")";
    }

    public static String showRoadLayer() {
        Map<String, Object> map = new HashMap<>();
        map.put("sroad", "./data/sroad.geojson");
        map.put("ssroad", "./data/ssroad.geojson");
        map.put("road", "./data/道路.geojson");
        return "javascript:addRoadBackgroundLayer(" + getJson(map) + ")";
    }

    public static String removeRoadLayer() {
        return "javascript:removeRoadBackgroundLayer(" + "" + ")";
    }

    public static String updateUserTour(List<Model_GPS> gpsList) {
        return "javascript:updateUserTour(" + getJson(gpsList) + ")";
    }

    public static String showNaturalScienceLayer(String json) {
        return "javascript:addNaturalScienceLayer(" + json + ")";
    }

    public static String removeNaturalScienceLayer() {
        return "javascript:removeNaturalScienceLayer(" + "" + ")";
    }


    public static String showSightseeingLayer(String json) {
        return "javascript:addSightseeingLayer(" + json + ")";
    }

    public static String removeSightseeingLayer() {
        return "javascript:removeSightseeingLayer(" + "" + ")";
    }


    public static String showSpecialTourismLayer(String json) {
        return "javascript:addSpecialTourismLayer(" + json + ")";
    }

    public static String removeSpecialTourismLayer() {
        return "javascript:removeSpecialTourismLayer(" + "" + ")";
    }


    public static String updatePoiLocation(Model_Marker modelMarker) {
        Point point = Point.fromLngLat(modelMarker.gps.longitude, modelMarker.gps.latitude, modelMarker.gps.height);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("名称", modelMarker.name);
        jsonObject.addProperty("type", "POI");
        if (modelMarker.photos.size() > 0) {
            jsonObject.addProperty("thumbnail", modelMarker.photos.get(0));
            jsonObject.addProperty("图片", modelMarker.photos.get(0));
        } else {
            jsonObject.addProperty("thumbnail", "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg");
            jsonObject.addProperty("图片", "http://tiles.pano.vizen.cn/6A96E59B1701491990DB44C603664DFB/sphere/thumb.jpg");
        }
        String json = FeatureCollection.fromFeature(Feature.fromGeometry(point, jsonObject)).toJson();
        LogUtils.e(json);
        return "javascript:updatePoiLocation(" + json + ")";
    }

    public static String clearPoiDetail() {
        return "javascript:clearPoiDetail()";
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
