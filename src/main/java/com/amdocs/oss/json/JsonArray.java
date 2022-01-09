package com.amdocs.oss.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonArray {

    private Map<String,JsonObject> listOfJsonObject = new HashMap<>();

    public Map<String, JsonObject> getListOfJsonObject() {
        return listOfJsonObject;
    }

    public void setListOfJsonObject(String key,JsonObject json) {
        this.listOfJsonObject.put(key, json);
    }
}
