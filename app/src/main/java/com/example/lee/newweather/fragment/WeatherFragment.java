package com.example.lee.newweather.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.R;
import com.example.lee.newweather.activity.MainActivitys;
import com.example.lee.newweather.adapter.DailyForcastAdapter;
import com.example.lee.newweather.adapter.SuggestionAdapter;
import com.example.lee.newweather.databinding.FragmentMainBinding;
import com.example.lee.newweather.presenter.MainPresenter;
import com.example.lee.newweather.utils.DateUtils;
import com.example.lee.newweather.utils.PreferenceUtils;
import com.example.lee.newweather.weatherapi.WeatherApi;
import com.example.lee.newweather.weatherbean.NoWeatherBean;
import com.example.lee.newweather.weatherbean.OneSuggestionBean;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.lee.newweather.utils.DateUtils.LONG_TIME_FORMAT;

/**
 * Created by lee on 17-5-22.
 */

public class WeatherFragment extends Fragment implements WeatherListener, SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private MyCity myCity;
    private MainPresenter presenter;
    private Map<String, String> map;
    private ArrayList<OneSuggestionBean> suggestionBeen;
    private NoWeatherBean bean;
    private View root;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    private SwipeRefreshLayout swipeRefreshLayout;

    private FragmentMainBinding binding;
    private LineChart lineChart;

    private GridView daily_forcast;
    private DailyForcastAdapter dailyForcastAdapter;

    private ListView suggestion_list;
    private SuggestionAdapter suggestionAdapter;

    private NotificationManager manager;
    private Notification notification;
    private int canNoti;
    private boolean isResume = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        presenter = new MainPresenter(this);
        map = new HashMap<>();

        if (myCity.getId() != null) {
            map.put("key", WeatherApi.APP_KEY);
            map.put("city", myCity.getId());
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, null, false);
        root = binding.getRoot();

        initView();

        presenter.RequestData(map);
        return root;
    }

    public void setMyCity(MyCity myCity, int canNoti) {
        this.myCity = myCity;
        this.canNoti = canNoti;
    }

    @Override
    public void initView() {

        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();

        linearLayout = (LinearLayout) root.findViewById(R.id.linear);
        progressBar = (ProgressBar) root.findViewById(R.id.progress);
        lineChart = (LineChart) root.findViewById(R.id.linechart);

        daily_forcast = (GridView) root.findViewById(R.id.daily_forcast);
        suggestion_list = (ListView) root.findViewById(R.id.suggestion_list);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.lightgrey, R.color.gray);
        swipeRefreshLayout.setOnRefreshListener(this);
        lineChart.setDescription(null);
        lineChart.setNoDataText("数据怎么还没加载出来啊");
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setBorderWidth(20);

        //设置X轴样式
        XAxis xAxis = lineChart.getXAxis();
        //是否绘制X轴线
        xAxis.setDrawAxisLine(false);
        //是否绘制X轴网格线
        xAxis.setDrawGridLines(false);
        //设置X轴的间距单位
        xAxis.setAxisLineWidth(10);
        //设置X轴展示的最大个数
        xAxis.setAxisMaximum(6);
        //是否设置X轴标签
        xAxis.setDrawLabels(true);


        //设置左Y轴样式
        YAxis left = lineChart.getAxisLeft();
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawLabels(false);
        left.setLabelCount(2, false);


        //设置右Y轴样式
        YAxis right = lineChart.getAxisRight();
        right.setDrawAxisLine(false);
        right.setDrawGridLines(false);
        right.setDrawLabels(false);
        right.setLabelCount(2, false);


        dailyForcastAdapter = new DailyForcastAdapter(mContext);
        suggestionAdapter = new SuggestionAdapter(mContext);


    }

    @Override
    public void showLoadingView() {

        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoadingView() {

        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void showSuccessView(Object response) {


        bean = (NoWeatherBean) response;


        binding.setWeather(bean.HeWeather5.get(0));
        suggestionBeen = new ArrayList<>();
        setLineChartData();


        dailyForcastAdapter.setDaily_forecast(bean.HeWeather5.get(0).daily_forecast);

        daily_forcast.setAdapter(dailyForcastAdapter);


        suggestionBeen.add(bean.HeWeather5.get(0).suggestion.cw);
        suggestionBeen.add(bean.HeWeather5.get(0).suggestion.drsg);
        suggestionBeen.add(bean.HeWeather5.get(0).suggestion.flu);
        suggestionBeen.add(bean.HeWeather5.get(0).suggestion.sport);
        suggestionBeen.add(bean.HeWeather5.get(0).suggestion.trav);
        suggestionBeen.add(bean.HeWeather5.get(0).suggestion.uv);

        suggestionAdapter.setSuggestions(suggestionBeen);

        suggestion_list.setAdapter(suggestionAdapter);


    }

    @Override
    public void showFailedView(Object season) {

        Toast.makeText(mContext, "" + season, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onDestroy() {
        manager.cancel(2);
        super.onDestroy();
    }


    @Override
    public void onResume() {
        if (isResume) {
            if (PreferenceUtils.getPrefBoolean(mContext, "notification", true)) {
                initNoti();
                manager.notify(2, notification);
            } else {
                manager.cancel(2);
            }
        }
        isResume=true;
        super.onResume();
    }

    private void initNoti() {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_bar);
        remoteViews.setTextViewText(R.id.title, bean.HeWeather5.get(0).now.cond.txt);
        remoteViews.setTextViewText(R.id.tmp, bean.HeWeather5.get(0).now.tmp + "°");
        remoteViews.setTextViewText(R.id.hum, "湿度 | " + bean.HeWeather5.get(0).now.hum + "%");
        remoteViews.setTextViewText(R.id.city, bean.HeWeather5.get(0).basic.city);
        remoteViews.setTextViewText(R.id.time, "更新: " + DateUtils.getCurrDate(LONG_TIME_FORMAT) + "");
        notification.icon = R.mipmap.air;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notification.when = System.currentTimeMillis();
        Intent intent = new Intent(mContext, MainActivitys.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentView = remoteViews;
        notification.contentIntent = pendingIntent;
    }

    @Override
    public void showNotifiTion() {
        if (canNoti == 0) {
            if (PreferenceUtils.getPrefBoolean(mContext, "notification", true)) {
                initNoti();
                manager.notify(2, notification);
            } else {
                manager.cancel(2);
            }
        }

    }


    public void setLineChartData() {

        ArrayList<Entry> max_tmp = new ArrayList<>();
        ArrayList<Entry> min_tmp = new ArrayList<>();
        final ArrayList<String> xValues = new ArrayList<>();


        for (int j = 0; j < bean.HeWeather5.get(0).daily_forecast.size(); j++) {
            Date date = DateUtils.stringtoDate(bean.HeWeather5.get(0).daily_forecast.get(j).date, DateUtils.LONG_DATE_FORMAT__);

            xValues.add(DateUtils.getWeek(date));

            max_tmp.add(new Entry(j, (float) Integer.parseInt(bean.HeWeather5.get(0).
                    daily_forecast.get(j).tmp.max)));
            min_tmp.add(new Entry(j, (float) Integer.parseInt(bean.HeWeather5.get(0).
                    daily_forecast.get(j).tmp.min)));
        }

        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValues.get((int) value);
            }
        });
        LineDataSet max_data = new LineDataSet(max_tmp, "最高温");

        max_data.setCircleColor(Color.parseColor("#DC143C"));
        max_data.setLineWidth(2f);
        max_data.setCircleRadius(2.5f);
        max_data.setValueTextSize(12);

        LineDataSet min_data = new LineDataSet(min_tmp, "最低温");
        min_data.setCircleColor(Color.parseColor("#FFA500"));
        min_data.setLineWidth(2f);
        min_data.setCircleRadius(2.5f);
        min_data.setValueTextSize(12);


        ArrayList<ILineDataSet> all_data = new ArrayList<>();
        all_data.add(max_data);
        all_data.add(min_data);


//        BarData data=new BarData(xValues,all_data);
        LineData data = new LineData(all_data);
        lineChart.setData(data);

        lineChart.invalidate();
    }

    @Override
    public void onRefresh() {

        presenter.RequestData(map);

        swipeRefreshLayout.setRefreshing(false);

    }


    public void startFragmentUpdate() {

        presenter.RequestData(map);

    }


}
