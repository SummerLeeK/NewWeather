package com.example.lee.newweather.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.lee.newweather.okhttp.ResponseEntityToModule;
import com.example.lee.newweather.okhttp.listener.DisposeHandler;
import com.example.lee.newweather.okhttp.listener.onDisposeHandlerListener;
import com.example.lee.newweather.weatherbean.NoWeatherBean;
import com.example.lee.newweather.weatherbean.NoaqiWeatherBean;
import com.example.lee.newweather.weatherbean.WeatherBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lee on 17-5-20.
 */

public class CommonJsonCallBack implements Callback {

    private Handler mHandler;
    private onDisposeHandlerListener listener;

    private Class<?> mClass;


    public CommonJsonCallBack(DisposeHandler handler) {
        this.listener = handler.listener;
        mHandler = new Handler(Looper.getMainLooper());
        mClass = handler.mClass;
    }

    @Override
    public void onFailure(Call call, final IOException e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.OnFailed(e);
            }
        });


    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {

        final String result = response.body().string();

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                Log.e("weather", "response.body()" + result);
                handResponse(result);

            }
        });

    }

    private void handResponse(String result) {

        Log.e("weather", "result" + result);

        if (result == null || result.trim().equals("")) {
            listener.OnFailed("JSON数据为null");
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);

                Object obj = ResponseEntityToModule.parseJsonObjectToModule(jsonObject, mClass);
                if (obj != null) {
                    listener.OnSuccess(obj);
                } else {
                    listener.OnFailed("JSON解析失败！");
                }


            } catch (JSONException e) {
                e.printStackTrace();
                listener.OnFailed("JSON解析失败002！");
            }
        }

    }


}
