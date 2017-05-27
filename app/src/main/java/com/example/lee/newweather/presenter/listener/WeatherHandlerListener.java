package com.example.lee.newweather.presenter.listener;

/**
 * Created by lee on 17-5-22.
 */

public interface WeatherHandlerListener {

    void onSuccess(Object response);

    void onFailed(Object season);
}
