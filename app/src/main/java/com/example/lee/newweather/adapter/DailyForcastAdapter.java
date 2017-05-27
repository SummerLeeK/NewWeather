package com.example.lee.newweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lee.newweather.R;
import com.example.lee.newweather.utils.DateUtils;
import com.example.lee.newweather.weatherapi.WeatherApi;
import com.example.lee.newweather.weatherbean.DailyForeCastBean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lee on 17-5-23.
 */

public class DailyForcastAdapter extends BaseAdapter {

    private ArrayList<DailyForeCastBean> daily_forecast;
    private Context mContext;
    private LayoutInflater mInflater;

    public DailyForcastAdapter( Context mContext) {
        this.mContext = mContext;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setDaily_forecast(ArrayList<DailyForeCastBean> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    @Override
    public int getCount() {
        return daily_forecast.size();
    }

    @Override
    public Object getItem(int position) {
        return daily_forecast.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dailt_grid_item, null,false);
            holder = new ViewHolder();

            holder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            holder.weather_img = (ImageView) convertView.findViewById(R.id.weather_img);
            holder.date_weather = (TextView) convertView.findViewById(R.id.weather_txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Date date= DateUtils.stringtoDate(daily_forecast.get(position).date,DateUtils.LONG_DATE_FORMAT__);
        holder.date_tv.setText(DateUtils.getWeek(date));
        Glide.with(mContext).load(WeatherApi.IMG_URL+daily_forecast.get(position).cond.code_d+".png")
                .skipMemoryCache(true).placeholder(R.mipmap.ic_launcher).into(holder.weather_img);
        holder.date_weather.setText(daily_forecast.get(position).cond.txt_d);
        return convertView;
    }

    static class ViewHolder {
        TextView date_tv;
        ImageView weather_img;
        TextView date_weather;

    }
}
