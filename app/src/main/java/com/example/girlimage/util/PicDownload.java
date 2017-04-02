package com.example.girlimage.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class PicDownload {
    public static void saveImage(View view, Context context) {
        //手机存储的根目录
        File root = null;
        //判断手机是否有SD卡
        String state = Environment.getExternalStorageState();
        Log.e("TAG","------state------" +state);
        //如果有SD卡
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //得到sd卡根目录
            root = Environment.getExternalStorageDirectory();
            Log.e("TAG","-------sd卡-----" + root.toString());
        }
        //将存储路径设置为工程的名字
        File dicectory = new File(root, "GirlImage");
        Log.e("TAG","-------完整路径-----" + dicectory.toString());
        //创建文件夹
        if (!dicectory.exists()) {
            Log.e("TAG","-------是否创建-----" + dicectory.toString());
            dicectory.mkdirs();
            Log.e("TAG","-------创建是否成功-----" + dicectory.mkdirs());
        }
        // 获取View的cache先要通过setDrawingCacheEnable方法把cache开启，
        // 然后再调用getDrawingCache方法就可 以获得view的cache图片
        Bitmap bitmap = view.getDrawingCache();
        //文件名以保存时的时间命名
        File file = new File(dicectory, new Date().getTime() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //将bitmap以JPG的格式保存在设置的文件夹中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();

            Uri uri = Uri.fromFile(file);
            //保存图片后发送广播通知图库更新图片
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "保存失败", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
