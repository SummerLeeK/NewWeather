package com.example.lee.newweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lee.newweather.R;
import com.example.lee.newweather.activity.AboutActivity;
import com.example.lee.newweather.utils.PreferenceUtils;


/**
 * Created by lee on 17-5-24.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private SwitchPreference poisition;
    private SwitchPreference update;
    private SwitchPreference notifi_bar;
    private Preference about;

    private String position_key,update_key,notificationbar_key;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        addPreferencesFromResource(R.xml.preference_setting);

        mContext=getActivity();
        initView();

        initStatus();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView(){
        position_key=getString(R.string.position_key);
        update_key=getString(R.string.update_key);
        notificationbar_key=getString(R.string.notificationbar_key);
        poisition= (SwitchPreference) findPreference(position_key);
        update= (SwitchPreference) findPreference(update_key);
        notifi_bar= (SwitchPreference) findPreference(notificationbar_key);
        about=findPreference(getString(R.string.about_key));

        poisition.setSummary(PreferenceUtils.getPrefString("currentposition","暂无定位",mContext));
        update.setSummary("4小时更新一次");
        poisition.setOnPreferenceChangeListener(this);
        update.setOnPreferenceChangeListener(this);
        notifi_bar.setOnPreferenceChangeListener(this);
        about.setOnPreferenceClickListener(this);
    }

    private void initStatus(){

        poisition.setChecked(PreferenceUtils.getPrefBoolean(mContext,"position",true));
        update.setChecked(PreferenceUtils.getPrefBoolean(mContext,"update",true));
        notifi_bar.setChecked(PreferenceUtils.getPrefBoolean(mContext,"notification",true));

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference.getKey().equals(position_key)){
            if (PreferenceUtils.getPrefBoolean(mContext,"position",true)==true){
                PreferenceUtils.setPrefBoolean(mContext,"position",false);
                poisition.setChecked(false);
            }else {
                PreferenceUtils.setPrefBoolean(mContext,"position",true);
                poisition.setChecked(true);
            }


        }else if (preference.getKey().equals(update_key)){
            if (PreferenceUtils.getPrefBoolean(mContext,"update",true)==true){
                PreferenceUtils.setPrefBoolean(mContext,"update",false);
                update.setChecked(false);
            }else {
                PreferenceUtils.setPrefBoolean(mContext,"update",true);
                update.setChecked(true);
            }

        }else if(preference.getKey().equals(notificationbar_key)){
            if (PreferenceUtils.getPrefBoolean(mContext,"notification",true)==true){
                PreferenceUtils.setPrefBoolean(mContext,"notification",false);
                notifi_bar.setChecked(false);
            }else {
                PreferenceUtils.setPrefBoolean(mContext,"notification",true);
                notifi_bar.setChecked(true);
            }

        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Intent intent=new Intent(mContext, AboutActivity.class);
        mContext.startActivity(intent);
        return false;
    }
}
