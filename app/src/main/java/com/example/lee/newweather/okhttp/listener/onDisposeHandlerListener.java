package com.example.lee.newweather.okhttp.listener;

import java.util.Objects;

/**
 * Created by lee on 17-5-20.
 */

public interface onDisposeHandlerListener {

    void OnSuccess(Object response);

    void OnFailed(Object reasonObj);
}
