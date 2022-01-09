package com.amdocs.oss.common;

import com.amdocs.oss.json.JsonObject;

public class Resualt {

    private String message;
    private boolean success;
    private JsonObject json;
    private String response;
    private int code;

    public Resualt(){}

    public Resualt(String message,String response,boolean success, int code) {
        this.message = message;
        this.success = success;
        this.code = code;
        this.response = response;
    }

    public Resualt(String message, boolean success, JsonObject json, String response, int code) {
        this.message = message;
        this.success = success;
        this.json = json;
        this.response = response;
        this.code = code;
    }

    public Resualt(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String messge) {
        this.message = messge;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public JsonObject getJson() {
        return json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }

    public String getResponse() {
        if(json == null){
            json = new JsonObject();
            json.put("message",this.message);
            json.put("code",String.valueOf(this.code));
            json.put("response",this.response);
            json.put("success",String.valueOf(this.success));
        }
        return json.toString();
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
