package com.example.girlimage.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.girlimage.R;
import com.example.girlimage.util.PicDownload;

import uk.co.senab.photoview.PhotoView;

public class ImageShowActivity extends AppCompatActivity{


    PhotoView mPhotoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageshow);

        mPhotoView = (PhotoView) findViewById(R.id.iv_imageshow);
        //得到intent传来的值
        String imageUrl = getIntent().getStringExtra("data");

        //Glide设置图片(未加载则用粉色替代)
        Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.colorAccent)
                .into(mPhotoView);

        //设置为true后，可以通过view.getDrawingCache()获得view的cache(缓存)
        mPhotoView.setDrawingCacheEnabled(true);
        //View的长按事件
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //new一个对话框
                new AlertDialog.Builder(ImageShowActivity.this)
                        .setMessage("保存图片")
                        //消极的 否定的 即对话框的取消选项
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface anInterface, int i) {
                                //让Dialog对话框 从屏幕上消失
                                anInterface.dismiss();
                            }
                        })
                        //积极的 确定的 即对话框的确定选项
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface anInterface, int i) {
                                anInterface.dismiss();
                                //保存图片
                                saveImage();
                            }
                        }).show();
                return true;
            }
        });
    }
    //自定义的权限请求码
    private final static int REQUESTCODE = 101;
    public void saveImage(){
        //如果版本高于23 即Android6.0
        if (Build.VERSION.SDK_INT >= 23) {
            //检查权限
            int checkWriteContactsPermission = checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //如果没有权限
            if (checkWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //请求权限 REQUESTCODE 自定义的权限请求码
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        ,REQUESTCODE);
                return;
            } else {
                //如果有权限了
                //执行保存图片的方法
                PicDownload.saveImage(mPhotoView, ImageShowActivity.this);
            }
        } else {
            //如果版本低于Android6.0则直接使用
            //执行保存图片的方法
            PicDownload.saveImage(mPhotoView, ImageShowActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //回调
        if (requestCode == REQUESTCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //执行保存图片的方法
                PicDownload.saveImage(mPhotoView, ImageShowActivity.this);
            } else {
                Toast.makeText(this,"您已禁止该权限，需要重新开启",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
