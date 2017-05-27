package com.example.lee.newweather.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by u on 2017/2/12.
 */

public class RequestParams {

    public ConcurrentMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    public ConcurrentMap<String, Object> fileParams = new ConcurrentHashMap<String, Object>();

    public RequestParams() {

    }

    public RequestParams(Map<String, String> source) {

        if (source == null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }

        });
    }

    public void put(String key, String value) {

        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }


    public void put(String key, Object value) throws FileNotFoundException {
        if (key != null) {
            fileParams.put(key, value);
        }
    }

    public boolean hasParams() {
        if (urlParams.size() > 0 && fileParams.size() > 0){
            return true;
        }
        return false;
    }
}
