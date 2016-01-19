package com.coinblesk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConverter {
    
    private static final Gson gson;
    
    static {
        gson = new GsonBuilder().create();
    }
    
    public static String toJson(Object o) {
        return gson.toJson(o);
    }
    
    public static <T>  T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
