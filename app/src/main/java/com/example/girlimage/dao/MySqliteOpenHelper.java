package com.example.girlimage.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySqliteOpenHelper extends SQLiteOpenHelper{
    //数据库名称
    private static final String name = "girl.db";
    //数据库版本
    private static final int version = 1;

    public static final String tableNmae = "girls";

    public MySqliteOpenHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, name, null, version);
    }
    //用于初次使用软件时生成数据库表
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 图片的所以属性
         * title : 清纯气质韩国美女沙滩写真
         * picUrl : http://t1.du114.com/uploads/151201/10-1512011521415N.jpg
         * description : 114美女
         * ctime : 2016-03-06 14:11
         * url : http://www.du114.com/qingchun/66307.html
         */

        //执行SQL语句 创建表 此处注意table后的空格 不然语句就成了tablegirls而报错
        db.execSQL("create table " + tableNmae + "(id integer primary key autoincrement,title text," +
                "picUrl text,description text,ctime text,url text)");
    }
    //用于升级软件时更新数据库表结构
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
