package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyClass {
    public static void main(String[] args){

        Schema schema=new Schema(1,"com.example.lee.newweather");

        Entity entity=schema.addEntity("City");

        entity.addStringProperty("id").primaryKey();
        entity.addStringProperty("cityZh");
        entity.addStringProperty("provinceZh");
        entity.addStringProperty("leaderZh");



        Entity entity1=schema.addEntity("MyCity");
        entity1.addStringProperty("id").primaryKey();
        entity1.addStringProperty("cityZh");
        entity1.addStringProperty("provinceZh");
        entity1.addStringProperty("leaderZh");

        try {
            new DaoGenerator().generateAll(schema, "../NewWeather/app/src/main/java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
