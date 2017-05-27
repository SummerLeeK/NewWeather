package com.example.lee.newweather.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lee.newweather.R;
import com.example.lee.newweather.fragment.SettingFragment;

/**
 * Created by lee on 17-5-24.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main_activity);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);

        title.setText("设置");
        back.setOnClickListener(this);

        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.set_frame, new SettingFragment());

        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
