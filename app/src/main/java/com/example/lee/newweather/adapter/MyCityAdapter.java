package com.example.lee.newweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.R;

import java.util.List;

/**
 * Created by lee on 17-5-25.
 */

public class MyCityAdapter extends BaseAdapter {

    private List<MyCity> cities_list;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyCityAdapter(List<MyCity> cities_list, Context mContext) {
        this.cities_list = cities_list;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setCities_list(List<MyCity> cities_list) {
        this.cities_list = cities_list;
    }

    @Override
    public int getCount() {
        return cities_list.size();
    }

    @Override
    public Object getItem(int position) {
        return cities_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.city_list_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.txt_city);

        textView.setText(cities_list.get(position).getCityZh());
        return convertView;
    }
}
