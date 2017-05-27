package com.example.lee.newweather.okhttp;

import com.example.lee.newweather.okhttp.listener.DisposeHandler;
import com.example.lee.newweather.okhttp.response.CommonJsonCallBack;
import com.example.lee.newweather.okhttp.utils.HttpUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lee on 17-5-20.
 */

public class CommonOkhttpClient {

    private static final int TIME_OUT=30;

    public static OkHttpClient okHttpClient;


    static {
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT,TimeUnit.SECONDS);
        builder.connectTimeout(TIME_OUT,TimeUnit.SECONDS);

        builder.followRedirects(true);
        builder.sslSocketFactory(HttpUtils.initSSLSocketFactory());

        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        okHttpClient=builder.build();
    }


    public static Call sendRequest(Request request, CommonJsonCallBack callBack){
        Call call=okHttpClient.newCall(request);
        call.enqueue(callBack);

        return call;
    }


    public static Call get(Request request,DisposeHandler handler){
        Call call=okHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallBack(handler));

        return call;
    }
}
