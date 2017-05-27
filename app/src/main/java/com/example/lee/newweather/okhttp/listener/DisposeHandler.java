package com.example.lee.newweather.okhttp.listener;

/**
 * Created by lee on 17-5-20.
 */

public class DisposeHandler {
    public onDisposeHandlerListener listener;
    public Class<?> mClass;

    public DisposeHandler(onDisposeHandlerListener listener, Class<?> mClass) {
        this.listener = listener;
        this.mClass = mClass;
    }

}
