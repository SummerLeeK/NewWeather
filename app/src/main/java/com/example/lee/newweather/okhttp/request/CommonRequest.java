package com.example.lee.newweather.okhttp.request;

import android.util.Log;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by lee on 17-5-20.
 */

public class CommonRequest {

    public static Request createPostRequest(String url,RequestParams params){

        FormBody.Builder builder=new FormBody.Builder();

        if (params!=null){
            for (Map.Entry<String,String> entry:params.urlParams.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        FormBody formBody=builder.build();

        return new Request.Builder().post(formBody).url(url).build();

    }


    public static Request createGetRequest(String url,Map<String,String> map){
        StringBuilder sb=new StringBuilder(url).append("?");

        if (map!=null){
            for (Map.Entry<String,String> entry:map.entrySet()){
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                sb.append("&");
            }
        }
        Log.e("weather","url"+sb.substring(0,sb.length()-1));
        return new Request.Builder().get().url(sb.substring(0,sb.length()-1)).build();
    }
}
