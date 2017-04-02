package com.example.girlimage.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class Network {
    //除了判断是否有网络，还写工具方法判断网络为什么类型 2G、3G、4G、wifi
    public static boolean checkNetworkState(Context context) {
        //得到网络信息
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.e("TAG", "-----------Network----" + manager);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            //当前的网络是连接的
            return true;
        } else {
            return false;
        }
    }
}
