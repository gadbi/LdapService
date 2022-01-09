package com.amdocs.oss.json;

import java.util.HashMap;
import java.util.Map;

public class JsonObject {

   Map<String,String> jsonObjectMap = null;

   public JsonObject(){
       this.jsonObjectMap = new HashMap<>();
   }

    public JsonObject(Map<String,String> map){this.jsonObjectMap = map;}

    public void put(String key,String value) { jsonObjectMap.put(key,value);}
    String json = "";
    @Override
    public String toString() {
        if(jsonObjectMap !=null && jsonObjectMap.size() > 0) {
            json = "{";
            jsonObjectMap.forEach((k, v) -> {
                json += '"' + k + '"' + ':' + '"' + v + '"' + ',';
            });
            return json.substring(0, json.lastIndexOf(',')) + "}";
        }else{return "";}
    }
}
