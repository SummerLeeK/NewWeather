package com.example.lee.newweather.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.example.lee.newweather.City;
import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.R;
import com.example.lee.newweather.adapter.CityAdapter;
import com.example.lee.newweather.fragment.WeatherListener;
import com.example.lee.newweather.greendao.DBManager;

import java.util.List;

/**
 * Created by lee on 17-5-24.
 */

public class CityActivity extends AppCompatActivity implements WeatherListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private LinearLayout loading;
    private LinearLayout failed;
    private ListView city_list;
    private CityAdapter adapter;
    private List<City> cities;
    private List<City> search_city;

    //是否处于搜索状态
    private boolean isSearch;

    private ImageView back;
    private SearchView searchView;
    private ImageView city;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext=this;

        setContentView(R.layout.city_activity_main);
        initView();

    }

    @Override
    public void initView() {

        isSearch = false;

        DBManager.initDBWithCity(this);

        loading = (LinearLayout) findViewById(R.id.loadingview);
        failed = (LinearLayout) findViewById(R.id.failed_view);
        city_list = (ListView) findViewById(R.id.city_list);

        city_list.setOnItemClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        city = (ImageView) findViewById(R.id.img_city);


        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                searchView.clearFocus();
                if (!query.equals("")) {
                    search_city = DBManager.getCityByNameLike(query,mContext);
                    Log.i("city_list", "搜索" + search_city);
                    adapter.setCities_list(search_city);
                    adapter.notifyDataSetChanged();
                    isSearch = true;

                    return true;
                } else {
                    adapter.setCities_list(cities);
                    adapter.notifyDataSetChanged();
                    isSearch = false;
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!newText.equals("")) {
                    search_city = DBManager.getCityByNameLike(newText,mContext);
                    adapter.setCities_list(search_city);
                    Log.i("city_list", "搜索" + search_city);
                    adapter.notifyDataSetChanged();
                    isSearch = true;
                    return true;
                } else {
                    adapter.setCities_list(cities);
                    adapter.notifyDataSetChanged();
                    isSearch = false;
                }

                return false;
            }
        });


        back.setOnClickListener(this);
        city.setOnClickListener(this);

        showLoadingView();


        cities = DBManager.getAllCity(mContext);


        adapter = new CityAdapter(cities, this);
        city_list.setAdapter(adapter);
        showSuccessView(null);


    }

    @Override
    public void showLoadingView() {

        loading.setVisibility(View.VISIBLE);
        failed.setVisibility(View.GONE);
        city_list.setVisibility(View.GONE);

    }

    @Override
    public void hideLoadingView() {

        loading.setVisibility(View.GONE);

    }

    @Override
    public void showSuccessView(Object response) {

        hideLoadingView();
        city_list.setVisibility(View.VISIBLE);
        failed.setVisibility(View.GONE);
    }

    @Override
    public void showFailedView(Object season) {

        loading.setVisibility(View.GONE);
        failed.setVisibility(View.VISIBLE);
        city_list.setVisibility(View.GONE);

    }

    @Override
    public void showNotifiTion() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.img_city:
                Intent intent = new Intent(CityActivity.this, MyCityActivity.class);
                startActivity(intent);
                break;
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DBManager.initDBWithMyCity(this);
        if (isSearch) {
            //从刷新数据中取数据
            MyCity myCity = new MyCity(search_city.get(position).getId(), search_city.get(position).getCityZh()
                    , search_city.get(position).getProvinceZh(), search_city.get(position).getLeaderZh());

            DBManager.insertOrReplaceInMyCity(myCity,mContext);
            Toast.makeText(CityActivity.this, "您已成功添加" + search_city.get(position).getCityZh(), Toast.LENGTH_SHORT).show();
        } else {
            //从原始数据中拿

            MyCity myCity = new MyCity(cities.get(position).getId(), cities.get(position).getCityZh()
                    , cities.get(position).getProvinceZh(), cities.get(position).getLeaderZh());

            DBManager.insertOrReplaceInMyCity(myCity,mContext);

            Toast.makeText(CityActivity.this, "您已成功添加" + cities.get(position).getCityZh(), Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(CityActivity.this, MainActivitys.class);

        startActivity(intent);

    }


}
