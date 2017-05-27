package com.example.lee.newweather.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.R;
import com.example.lee.newweather.adapter.WeatherPagerAdapter;
import com.example.lee.newweather.fragment.WeatherFragment;
import com.example.lee.newweather.greendao.DBManager;
import com.example.lee.newweather.listener.OnFragmentUpdateListener;
import com.example.lee.newweather.listener.UpdateListener;
import com.example.lee.newweather.service.UpdateWeatherService;
import com.example.lee.newweather.utils.PreferenceUtils;
import com.example.lee.newweather.weatherapi.WeatherApi;

import java.util.ArrayList;


/**
 * Created by lee on 17-5-22.
 */

public class MainActivitys extends AppCompatActivity implements View.OnClickListener {


    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private ImageView drawer_img;
    private TextView title;

    private NavigationView navigationView;
    private MyCity myCity;

    private ArrayList<MyCity> myCities;
    private ArrayList<WeatherFragment> fragments;
    private WeatherPagerAdapter adapter;
    private long firstClick;

    private boolean canNoti = false;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    UpdateWeatherService updateWeatherService;
    private Intent intentService;
    //是否绑定
    private boolean isBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.i("service", "connection success");
            UpdateWeatherService.MyBinder binder = (UpdateWeatherService.MyBinder) service;
            updateWeatherService = binder.getService();
            binder.getService().setListener(new UpdateListener() {
                @Override
                public void startUpdate() {
                    if (fragments.size() > 0) {
                        for (int i = 0; i < fragments.size(); i++) {
                            fragments.get(i).startFragmentUpdate();
                        }
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            updateWeatherService = null;
            Log.i("service", "connection failed");
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isBinder = false;
        intentService = new Intent(MainActivitys.this, UpdateWeatherService.class);
        myCities = new ArrayList<>();
        fragments = new ArrayList<>();
        initData();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;

                switch (item.getItemId()) {
                    case R.id.city:
                        intent = new Intent(MainActivitys.this, CityActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.setting:
                        intent = new Intent(MainActivitys.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.exit:
                        dialog = builder.create();
                        dialog.show();

                        break;
                }
                return true;
            }
        });
        builder = new AlertDialog.Builder(this);
        builder.setMessage("您确定要退出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_img = (ImageView) findViewById(R.id.drawer_switch);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        title = (TextView) findViewById(R.id.title);
        drawer_img.setOnClickListener(this);

        for (int i = 0; i < myCities.size(); i++) {
            WeatherFragment fragment = new WeatherFragment();
            fragment.setMyCity(myCities.get(i), i);
            fragments.add(fragment);
        }

        title.setText(myCities.get(0).getCityZh());

        adapter = new WeatherPagerAdapter(getSupportFragmentManager(), fragments, myCities);

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                title.setText(myCities.get(position).getCityZh());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initData() {

        if (DBManager.getAllMycity(this).size() <= 0) {
            myCity = new MyCity(WeatherApi.BEIJING_ID, WeatherApi.BEIJING_ZH, WeatherApi.BEIJING_PROVINCEZh, WeatherApi.BEIJING_LEADERZh);
            DBManager.insertOrReplaceInMyCity(myCity, this);
            myCities.add(myCity);

        } else {
            myCities = (ArrayList<MyCity>) DBManager.getAllMycity(this);
        }

    }

    @Override
    public void onClick(View v) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume() {

        Log.i("activity", "onResume");
        if (PreferenceUtils.getPrefBoolean(this, "update", true) && !isBinder) {
            bindService(intentService, connection, BIND_AUTO_CREATE);
            isBinder = true;
        } else if (PreferenceUtils.getPrefBoolean(this, "update", true) == false) {
            if (updateWeatherService != null && intentService != null) {
                unbindService(connection);
                isBinder = false;
            }
        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - firstClick > 2000) {
                firstClick = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                System.exit(0);
            }
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {

        Log.i("activity", "activity destory");
        updateWeatherService.onUnbind(intentService);
        isBinder = false;
        super.onDestroy();
    }
}
