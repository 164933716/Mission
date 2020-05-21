package com.versalinks.mission;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class OptUtils {
    public String rotateByLeft() {
        return "javascript:optMap(" + getJson("rotateByLeft", "30") + ")";
    }

    public String rotateByRight() {
        return "javascript:optMap(" + getJson("rotateByRight", "30") + ")";
    }

    public String rotateByUp() {
        return "javascript:optMap(" + getJson("rotateByUp", "30") + ")";
    }

    public String rotateByDown() {
        return "javascript:optMap(" + getJson("rotateByDown", "30") + ")";
    }

    public String moveTo(Model_GPS modelGps) {
        return "javascript:optMap(" + getJson("moveTo", modelGps) + ")";
    }

    public String zoomIn() {
        return "javascript:optMap(" + getJson("zoomIn", null) + ")";
    }

    public String zoomOut() {
        return "javascript:optMap(" + getJson("zoomOut", null) + ")";
    }

    public String getJson(String action, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", action);
        if (data != null) {
            map.put("data", data);
        }
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
