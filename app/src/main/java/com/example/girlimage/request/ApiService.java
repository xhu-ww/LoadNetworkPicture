package com.example.girlimage.request;

import com.example.girlimage.bean.GirlsBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiService {

    @POST("197-1")
    @FormUrlEncoded
    Call<GirlsBean> getGirls(@Field("showapi_appid") String showapi_appid,
                             @Field("showapi_sign") String showapi_sign,
                             @Field("num") int num ,
                             @Field("page") int page);
//    //GET请求
//    @GET("197-1")
//    Call<GirlBean> getGirls(@Query("showapi_appid") String showapi_appid,
//                            @Query("showapi_sign") String showapi_sign,
//                            @Query("num") int num ,
//                            @Query("page") int page);

}
