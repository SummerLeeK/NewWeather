package com.example.lee.newweather.fragment;

/**
 * Created by lee on 17-5-23.
 */

public interface WeatherListener {

    void initView();

    void showLoadingView();

    void hideLoadingView();

    void showSuccessView(Object response);

    void showFailedView(Object season);

    void showNotifiTion();
}
