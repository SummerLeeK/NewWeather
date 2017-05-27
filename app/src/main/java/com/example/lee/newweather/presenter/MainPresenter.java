package com.example.lee.newweather.presenter;

import android.os.Handler;

import com.example.lee.newweather.fragment.WeatherListener;
import com.example.lee.newweather.presenter.biz.WeatherBiz;
import com.example.lee.newweather.presenter.listener.WeatherHandlerListener;


import java.util.Map;

/**
 * Created by lee on 17-5-22.
 */

public class MainPresenter {
    private WeatherBiz weatherBiz;
    private WeatherListener listener;
    private Handler handler=new Handler();

    public MainPresenter(WeatherListener listener) {
        this.listener=listener;
        weatherBiz=new WeatherBiz();
    }


    public void RequestData(Map<String,String> map){

        listener.showLoadingView();

        weatherBiz.GetData(map, new WeatherHandlerListener() {
            @Override
            public void onSuccess(final Object response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.hideLoadingView();
                        listener.showSuccessView(response);
                        listener.showNotifiTion();
                    }
                });

            }

            @Override
            public void onFailed(final Object season) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        listener.hideLoadingView();
                        listener.showFailedView(season);
                    }
                });

            }
        });

    }




}
