package com.example.lee.newweather.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lee.newweather.R;
import com.example.lee.newweather.weatherbean.OneSuggestionBean;

import java.util.ArrayList;

/**
 * Created by lee on 17-5-23.
 */

public class SuggestionAdapter extends BaseAdapter {

    private ArrayList<OneSuggestionBean> suggestions;


    private Context mContext;

    private LayoutInflater mInflater;

    private String[] suggestion_tv = new String[]{
            "洗车", "穿衣", "感冒",
            "运动", "旅游", "紫外线"
    };

    private int[] suggestion_img_id = new int[]{
            R.mipmap.cw, R.mipmap.drsg, R.mipmap.flu,
            R.mipmap.sport, R.mipmap.trav, R.mipmap.uv
    };

    public void setSuggestions(ArrayList<OneSuggestionBean> suggestions) {
        this.suggestions = suggestions;
    }

    public SuggestionAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.suggestion_grid_item, null);
            holder = new ViewHolder();

            holder.suggestion_tv = (TextView) convertView.findViewById(R.id.suggetion_tv);
            holder.suggestion_img = (ImageView) convertView.findViewById(R.id.suggestion_img);
            holder.suggestion_desc = (TextView) convertView.findViewById(R.id.suggestion_txt);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.suggestion_tv.setText(suggestion_tv[position]);
        holder.suggestion_img.setImageResource(suggestion_img_id[position]);
        holder.suggestion_desc.setText(suggestions.get(position).txt);


        return convertView;
    }

    static class ViewHolder {
        TextView suggestion_tv;
        ImageView suggestion_img;
        TextView suggestion_desc;
    }


}
