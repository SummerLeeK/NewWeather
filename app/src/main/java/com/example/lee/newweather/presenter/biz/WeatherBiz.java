package com.example.lee.newweather.presenter.biz;

import android.util.Log;
import com.example.lee.newweather.okhttp.RequestCenter;
import com.example.lee.newweather.okhttp.listener.onDisposeHandlerListener;
import com.example.lee.newweather.presenter.listener.WeatherHandlerListener;
import com.example.lee.newweather.presenter.listener.WeatherListener;
import java.util.Map;

/**
 * Created by lee on 17-5-22.
 */

public class WeatherBiz implements WeatherListener{

    @Override
    public void GetData(Map<String, String> map, final WeatherHandlerListener listener) {
        RequestCenter.requestAllWeather(new onDisposeHandlerListener() {
            @Override
            public void OnSuccess(Object response) {
                Log.e("weather", "response" + response.toString());
                listener.onSuccess(response);
            }

            @Override
            public void OnFailed(Object reasonObj) {
                listener.onFailed(reasonObj);

            }
        }, map);
    }
}
