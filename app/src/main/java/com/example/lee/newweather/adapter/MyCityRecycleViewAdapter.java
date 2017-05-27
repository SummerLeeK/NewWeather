package com.example.lee.newweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.R;

import java.util.List;

/**
 * Created by lee on 17-5-25.
 */

public class MyCityRecycleViewAdapter extends RecyclerView.Adapter {

    private List<MyCity> myCities;
    private LayoutInflater mInflater;
    private Context mContext;

    public MyCityRecycleViewAdapter(List<MyCity> myCities, Context mContext) {
        this.myCities = myCities;
        mInflater=LayoutInflater.from(mContext);
        this.mContext=mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root=mInflater.inflate(R.layout.city_list_item,parent,false);

        return new MyCityViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"左右滑动即可删除",Toast.LENGTH_SHORT).show();
            }
        });

        ((MyCityViewHolder)holder).bindViewHolder(myCities.get(position));

    }

    @Override
    public int getItemCount() {
        return myCities.size();
    }


    class MyCityViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public MyCityViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.txt_city);
        }

        public void bindViewHolder(MyCity myCity){
            textView.setText(myCity.getCityZh());
        }
    }
}
