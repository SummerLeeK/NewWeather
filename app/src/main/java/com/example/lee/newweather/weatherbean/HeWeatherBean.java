package com.example.lee.newweather.weatherbean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lee on 17-5-21.
 */

public class HeWeatherBean extends baseWeather{

    public AQIBean aqi;
    public BasicBean basic;
    public ArrayList<DailyForeCastBean>  daily_forecast;
    public ArrayList<HourlyForeCaseBean> hourly_forecast;
    public NowBean now;
    public String status;
    public SuggestionBean suggestion;
}
