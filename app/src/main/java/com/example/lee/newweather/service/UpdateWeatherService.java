package com.example.lee.newweather.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.lee.newweather.listener.UpdateListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lee on 17-5-25.
 */

public class UpdateWeatherService extends Service {


    public static final int HOUR = 1000 *3600;
    private static final int UPDATE_CODE = 001;
    private UpdateListener listener;
    private Timer timer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("service", "startUpdate");
            if (listener != null) {
                listener.startUpdate();
                Toast.makeText(getApplicationContext(), "定时更新", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void setListener(UpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service", "create");
        timer = new Timer();

    }

    @Override
    public void onDestroy() {
        Log.i("service", "destory");
        timer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service", "start");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(UPDATE_CODE);
            }
        }, 4*HOUR,4* HOUR);
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("service", "unbind");
        timer.cancel();
        return false;
    }

    public class MyBinder extends Binder {

        public UpdateWeatherService getService() {
            return UpdateWeatherService.this;
        }
    }


}
