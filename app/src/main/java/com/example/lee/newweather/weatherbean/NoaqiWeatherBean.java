package com.example.lee.newweather.weatherbean;

import java.util.ArrayList;

/**
 * Created by lee on 17-5-25.
 */

public class NoaqiWeatherBean extends baseWeather {

    public BasicBean basic;
    public ArrayList<DailyForeCastBean> daily_forecast;
    public ArrayList<HourlyForeCaseBean> hourly_forecast;
    public NowBean now;
    public String status;
    public SuggestionBean suggestion;
}
