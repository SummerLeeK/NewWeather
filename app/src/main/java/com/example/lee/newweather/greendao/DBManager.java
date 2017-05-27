package com.example.lee.newweather.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.lee.newweather.City;
import com.example.lee.newweather.CityDao;
import com.example.lee.newweather.DaoMaster;
import com.example.lee.newweather.DaoSession;
import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.MyCityDao;

import java.util.List;

/**
 * Created by lee on 17-5-23.
 */

public class DBManager {

    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase database;
    private static DaoMaster master;
    private static DaoSession session;
    private static CityDao cityDao;
    private static MyCityDao myCityDao;

    /***
     * 初始化我的城市的基础类
     * @param mContext
     */

    public static void initDBWithMyCity(Context mContext) {

        helper = new DaoMaster.DevOpenHelper(mContext, "mycity.db");
        database = helper.getWritableDatabase();
        master = new DaoMaster(database);
        session = master.newSession();
        myCityDao = session.getMyCityDao();
    }

    /***
     * 初始化所有城市的基础类
     * @param mContext
     */
    public static void initDBWithCity(Context mContext) {

        helper = new DaoMaster.DevOpenHelper(mContext, "allcities.db");
        database = helper.getWritableDatabase();
        master = new DaoMaster(database);
        session = master.newSession();
        cityDao = session.getCityDao();
    }

    /**
     * 返回所有我添加的城市
     *
     * @return
     */

    public static List<MyCity> getAllMycity(Context mContext) {
        if (myCityDao == null) {
            DBManager.initDBWithMyCity(mContext);
        }

        return myCityDao.queryBuilder().list();
    }

    /***
     * 返回所有城市
     * @return
     */

    public static List<City> getAllCity(Context mContext) {
        if (cityDao == null) {
            DBManager.initDBWithCity(mContext);
        }
        return cityDao.queryBuilder().list();

    }

    /**
     * 模糊查询某个城市从所有城市中
     *
     * @param name
     * @return
     */

    public static List<City> getCityByNameLike(String name, Context mContext) {

        if (cityDao == null) {
            DBManager.initDBWithCity(mContext);
        }
        return cityDao.queryBuilder().whereOr(CityDao.Properties.CityZh.like("%" + name + "%")
                , CityDao.Properties.LeaderZh.like("%" + name + "%"))
                .list();
    }

    /**
     * 精确查询某个城市
     * @param name
     * @param mContext
     * @return
     */
    public static List<City> getCityByNameEQ(String name, Context mContext) {

        if (cityDao == null) {
            DBManager.initDBWithCity(mContext);
        }
        return cityDao.queryBuilder().where(CityDao.Properties.CityZh.eq(name)).list();
    }


    /**
     * 插入一条我的城市信息
     *
     * @param myCity
     */
    public static void insertOrReplaceInMyCity(MyCity myCity, Context mContext) {
        if (myCityDao == null) {
            DBManager.initDBWithMyCity(mContext);
        }
        myCityDao.insertOrReplace(myCity);
    }


    /**
     * 插入一条城市信息到所有城市表
     * @param city
     * @param mContext
     */

    public static void inseryOrReplaceInCity(City city, Context mContext) {
        if (cityDao == null) {
            DBManager.initDBWithCity(mContext);
        }
        cityDao.insertOrReplace(city);
    }

    /***
     * 删除一条数据从我的城市表中
     * @param myCity
     * @param mContext
     */

    public static void deleteFromMyCity(MyCity myCity,Context mContext){
        if (myCityDao==null){
            DBManager.initDBWithMyCity(mContext);
        }
        myCityDao.delete(myCity);
    }
}
