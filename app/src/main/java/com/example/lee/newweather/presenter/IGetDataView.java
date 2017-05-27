package com.example.lee.newweather.presenter;

import java.util.Map;

/**
 * Created by lee on 17-5-20.
 */

public interface IGetDataView  {


    void showSuccessView(final Object response);

    void showFailedView(final Object season);
}
