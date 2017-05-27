package com.example.lee.newweather.presenter.listener;

import java.util.Map;

/**
 * Created by lee on 17-5-22.
 */

public interface WeatherListener {
    void GetData(Map<String,String> map,WeatherHandlerListener listener);


}
