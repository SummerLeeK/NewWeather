package com.example.lee.newweather.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lee.newweather.City;
import com.example.lee.newweather.CityDao;
import com.example.lee.newweather.DaoMaster;
import com.example.lee.newweather.DaoSession;
import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.MyCityDao;
import com.example.lee.newweather.R;
import com.example.lee.newweather.greendao.DBManager;
import com.example.lee.newweather.okhttp.RequestCenter;
import com.example.lee.newweather.okhttp.listener.onDisposeHandlerListener;
import com.example.lee.newweather.utils.PreferenceUtils;
import com.example.lee.newweather.weatherapi.WeatherApi;
import com.example.lee.newweather.weatherbean.PositionBean;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lee on 17-5-22.
 */

public class SplashActivity extends AppCompatActivity {

    public static final int TO_MAIN_CODE = 000;
    public static final int IMPORT_DATABASE_CODE = 001;
    public static final int ERROR_CODE = 002;
    public static final int UPDATE_PROGRESS = 003;
    private MyCity myCity;
    private JSONArray array;
    private ImageView splash_img;
    private ProgressBar progressBar;
    private TextView textView;
    private Context mContext;
    private int status = 0;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case IMPORT_DATABASE_CODE:
                    progressBar.setMax(array.length());
                    importData();
                    break;
                case TO_MAIN_CODE:
                    Intent intent = new Intent(SplashActivity.this, MainActivitys.class);
                    startActivity(intent);
                    finish();
                    break;
                case ERROR_CODE:
                    Toast.makeText(SplashActivity.this, "cuowu", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_PROGRESS:
                    progressBar.setProgress(status);
                    break;
            }
        }
    };

    private void importData() {


        MySyncTask syncTask = new MySyncTask();
        syncTask.execute();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        mContext = this;
        DBManager.initDBWithCity(this);

        textView = (TextView) findViewById(R.id.tips);
        splash_img = (ImageView) findViewById(R.id.splash_img);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        if (DBManager.getAllCity(mContext).size() > 0) {
            showSuccessView();

        } else {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            OkHttpClient okHttpClient = builder.build();

            Request request = new Request.Builder().url(WeatherApi.ALL_CITY_ID).get().build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Toast.makeText(SplashActivity.this, "cuowu" + e, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String result = response.body().string();
                    Log.e("city", result);
                    try {
                        array = new JSONArray(result);
                        handler.sendEmptyMessage(IMPORT_DATABASE_CODE);
                    } catch (JSONException e) {
                        handler.sendEmptyMessage(ERROR_CODE);
                    }

                }
            });
        }
    }

    private void positionCity() {
        if (PreferenceUtils.getPrefBoolean(this, "position", true)) {
            RequestCenter.requestPosition(WeatherApi.POSITION_URL, new onDisposeHandlerListener() {
                @Override
                public void OnSuccess(Object response) {
                    PositionBean bean = (PositionBean) response;

                    String cityZh = bean.city.substring(0, bean.city.length() - 1);
                    InsertMyCity(cityZh);
                }

                @Override
                public void OnFailed(Object reasonObj) {

                }
            });
        }
    }

    private void InsertMyCity(String cityname) {

        PreferenceUtils.setPrefString(this, "currentposition", "当前定位：" + cityname);
        List<City> cities = DBManager.getCityByNameEQ(cityname, mContext);
        DBManager.initDBWithMyCity(this);
        List<MyCity> myCities=DBManager.getAllMycity(this);
        for (int i=0;i<myCities.size();i++){
            if (cities.get(0).getCityZh().equals(myCities.get(i).getCityZh())){
                return;
            }
        }
        MyCity mycity = new MyCity(cities.get(0).getId(), cities.get(0).getCityZh(), cities.get(0).getProvinceZh(), cities.get(0).getLeaderZh());
        DBManager.insertOrReplaceInMyCity(mycity, mContext);

    }


    private void showSuccessView() {

        positionCity();
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        splash_img.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(splash_img, "scaleX", 1, 1.3f)
                , ObjectAnimator.ofFloat(splash_img, "scaleY", 1, 1.3f)
                , ObjectAnimator.ofFloat(splash_img, "alpha", 1, 0.8f));
        set.setDuration(3000).start();

        handler.sendEmptyMessageDelayed(TO_MAIN_CODE, 3000);

    }



    class MySyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject jsonObject = array.getJSONObject(i);
                    City city = new City(jsonObject.getString("id"), jsonObject.getString("cityZh"),
                            jsonObject.getString("provinceZh"), jsonObject.getString("leaderZh"));
                    DBManager.inseryOrReplaceInCity(city, mContext);
                    status = status + 1;
                    publishProgress(status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status <= array.length()) {
                    handler.sendEmptyMessage(UPDATE_PROGRESS);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showSuccessView();
        }
    }
}
