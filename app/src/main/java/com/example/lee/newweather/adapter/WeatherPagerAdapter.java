package com.example.lee.newweather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.fragment.WeatherFragment;

import java.util.ArrayList;


/**
 * Created by lee on 17-5-22.
 */

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<WeatherFragment> fragments;

    private ArrayList<MyCity> myCities;


    public WeatherPagerAdapter(FragmentManager fm, ArrayList<WeatherFragment> fragments, ArrayList<MyCity> myCities) {
        super(fm);
        this.fragments = fragments;
        this.myCities = myCities;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return myCities.get(position).getCityZh();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
