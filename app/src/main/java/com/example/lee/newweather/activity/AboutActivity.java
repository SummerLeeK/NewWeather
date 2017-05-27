package com.example.lee.newweather.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lee.newweather.R;
import com.example.lee.newweather.weatherapi.WeatherApi;

/**
 * Created by lee on 17-5-24.
 */

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView title;

    private RelativeLayout weibo;
    private RelativeLayout project;
    private TextView update;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_activity);

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        weibo = (RelativeLayout) findViewById(R.id.weibo);
        project = (RelativeLayout) findViewById(R.id.project);

        title.setText("关于");
        back.setOnClickListener(this);
        weibo.setOnClickListener(this);
        project.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.weibo:
                startToBrower(WeatherApi.WEIBO);
                break;
            case R.id.project:
                startToBrower(WeatherApi.GITHUB);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void startToBrower(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
