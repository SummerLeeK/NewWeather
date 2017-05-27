package com.example.lee.newweather.okhttp;
import com.example.lee.newweather.okhttp.listener.DisposeHandler;
import com.example.lee.newweather.okhttp.listener.onDisposeHandlerListener;
import com.example.lee.newweather.okhttp.request.CommonRequest;
import com.example.lee.newweather.okhttp.request.RequestParams;
import com.example.lee.newweather.weatherapi.WeatherApi;
import com.example.lee.newweather.weatherbean.BaseCityBean;
import com.example.lee.newweather.weatherbean.NoWeatherBean;
import com.example.lee.newweather.weatherbean.PositionBean;
import com.example.lee.newweather.weatherbean.WeatherBean;

import java.util.Map;

/**
 * Created by lee on 17-5-20.
 */

public class RequestCenter {

    private static void postRequest(String url, Map<String,String> map, onDisposeHandlerListener listener,Class<?> mClass){

        CommonOkhttpClient.get(CommonRequest.createGetRequest(url,map),new DisposeHandler(listener,mClass));
    }


    public static void requestAllWeather(onDisposeHandlerListener listener,Map<String,String> map){
        postRequest(WeatherApi.CITY_WEATHER,map,listener, NoWeatherBean.class);
    }

    public static void requestPosition(String url,onDisposeHandlerListener listener){
        postRequest(url,null,listener, PositionBean.class);
    }

}
