package com.example.girlimage.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.girlimage.bean.GirlsBean;

import java.util.ArrayList;
import java.util.List;

public class GirlImageDao {
    MySqliteOpenHelper mHelper;

    public GirlImageDao(Context context) {
        mHelper = new MySqliteOpenHelper(context);
    }

    //向数据库中添加数据 即NewslistBean对象
    public void addGirl(GirlsBean.ShowapiResBodyBean.NewslistBean bean) {
        //Android提供了一个名为SQLiteDatabase的类，该类封装了一些操作数据库的API，
        //该类可以对数据进行添加(Create)、查询(Retrieve)、更新(Update)和删除(Delete)操作（简称为CRUD）
        SQLiteDatabase db = mHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        //存入图片的Url地址
        values.put("title", bean.getTitle());
        values.put("picUrl", bean.getPicUrl());
        values.put("description", bean.getDescription());
        values.put("ctime", bean.getCtime());
        values.put("url", bean.getUrl());
        //向表中插入数据
        db.insert(MySqliteOpenHelper.tableNmae, null, values);
        db.close();
    }

    //for循环遍历girl集合，将每个图片地址挨个插入数据库
    public void addGirlList(List<GirlsBean.ShowapiResBodyBean.NewslistBean> list) {
        for (GirlsBean.ShowapiResBodyBean.NewslistBean bean : list) {
            addGirl(bean);
        }
    }

    //查询SQLite取出数据存入集合
    public List<GirlsBean.ShowapiResBodyBean.NewslistBean> queryAllGirls() {
        List<GirlsBean.ShowapiResBodyBean.NewslistBean> list = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //Cursor是结果集游标，用于对结果集进行随机访问，Cursor与JDBC中的ResultSet作用很相似。
        Cursor cursor = db.query(MySqliteOpenHelper.tableNmae,null,null,null,null,null,null);
        //moveToNext()方法可以将游标从当前行移到下一行，如果移过了结果集的最后一行，返回结果为false，否则为true
        while (cursor.moveToNext()) {
            GirlsBean.ShowapiResBodyBean.NewslistBean bean =
                    new GirlsBean.ShowapiResBodyBean.NewslistBean();
            //通过key 得到图片地址值value
            bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            bean.setPicUrl(cursor.getString(cursor.getColumnIndex("picUrl")));
            bean.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            bean.setCtime(cursor.getString(cursor.getColumnIndex("ctime")));
            bean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            //将NewslistBean对象加入集合
            list.add(bean);
        }
        return list;
    }
}