package com.example.girlimage.request;

import com.example.girlimage.bean.GirlsBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class GirlsRequest {
    public static final String baseurl = "http://route.showapi.com/";
    public static final String showapi_appid = "34276";
    public static final String showapi_sign = "731d68d6d56b4d789d6571f530ee28ef";
    public static final int num = 20;

    //创建Retrofit对象
    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(baseurl)//请求基地址
            //添加转换器，直接将内容转换成String或者json
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public void getGirlList(final RequsetCallback requsetCallback, int page) {
        //创建请求的接口
        ApiService mApiService = mRetrofit.create(ApiService.class);
        Call<GirlsBean> mCall = mApiService.getGirls(showapi_appid,showapi_sign,num,page);
        //利用Call对象，发起网络请求
        mCall.enqueue(new Callback<GirlsBean>() {
            //请求成功
            @Override
            public void onResponse(Call<GirlsBean> call, Response<GirlsBean> response) {
                //成功获得正确数据
                if (response.body().getShowapi_res_code() == 0) {
                    requsetCallback.onFinish(response.body());
                } else {
                    //错误数控
                    requsetCallback.onError(response.body().getShowapi_res_error());
                }
            }
            //失败
            @Override
            public void onFailure(Call<GirlsBean> call, Throwable t) {
                requsetCallback.onError("网络请求失败");
            }
        });
    }
    //自定义回调接口
    public interface RequsetCallback<T>{
        void onFinish(T data);

        void onError(String msg);
    }
}
