package com.example.girlimage.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.girlimage.R;
import com.example.girlimage.adapter.MyAdapter;
import com.example.girlimage.bean.GirlsBean;
import com.example.girlimage.dao.GirlImageDao;
import com.example.girlimage.request.GirlsRequest;
import com.example.girlimage.request.GirlsRequest.RequsetCallback;
import com.example.girlimage.util.Network;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        RequsetCallback<GirlsBean>,OnRefreshListener {
    //是否联网
    boolean isNetwork;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    //下拉刷新上啦加载
    private SwipeRefreshLayout mRefreshLayout;
    private GirlsRequest mRequest;
    //最后一个可见的ITEM时才加载 事件
    private int lastVisibleItem;
    private GridLayoutManager mGridLayoutManager;
    //图片请求的页面
    private static int page = 1;
    //数据库操作
    private GirlImageDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //RecyclerView 设置行列
        mGridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        isNetwork = Network.checkNetworkState(this);
        //注册网络变化广播接收器
        registerNetworkChangeReceiver();
        //得到请求网络类的对象
        mRequest = new GirlsRequest();
        //判断是否联网，选择数据的加载方式
        if (isNetwork) {
            //如果有网络，调用getGirlList方法请求数据 1数据请求时的页面
            mRequest.getGirlList(this,1);
        } else {
            //没有网络则加载数据库的数据
            dao = new GirlImageDao(this);
            List<GirlsBean.ShowapiResBodyBean.NewslistBean> list = dao.queryAllGirls();
            if (list.size() == 0) {
                //集合数据为0则第一次进入也未联网
                Toast.makeText(this,"网络连接失败",Toast.LENGTH_SHORT).show();
            } else {
                //数据库有数据时候
                mAdapter = new MyAdapter(this,list);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        //找到SwipeRefreshLayout
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        //设置刷新进度条的颜色
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );
        //设置刷新监听事件下拉刷新
        mRefreshLayout.setOnRefreshListener(this);
        //上拉加载 mRecyclerView设置滚动事件监听
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //如果没有网络,以及数据则无法加载
                if (!isNetwork && dao.queryAllGirls().size() == 0) {
                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的item时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        //mAdapter.getItemCount()通过适配器得到当前Item的数量
                        lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    Toast.makeText(MainActivity.this,"正在加载...",Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //和下拉刷新时类似
                            page++;
                            //调用getGirlList方法重新请求数据
                            mRequest.getGirlList(new RequsetCallback<GirlsBean>() {
                                @Override
                                public void onFinish(GirlsBean data) {
                                    //适配器中写的方法，添加数据，并通知RecyclerView改变UI
                                    mAdapter.addMoreItem(data.getShowapi_res_body().getNewslist());
                                }

                                @Override
                                public void onError(String msg) {
                                    //吐司错误信息
                                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            },page);
                        }
                    },2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //最后一个可见的item
                lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void onFinish(GirlsBean data) {
        //得到适配器
        mAdapter = new MyAdapter(this,data.getShowapi_res_body().getNewslist());
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        //RecyclerView设置Item的点击事件
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String data) {
                //跳转显示详细图片的Activity
                Intent intent = new Intent(MainActivity.this, ImageShowActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
            }
        });

        //存储数据
        GirlImageDao dao = new GirlImageDao(this);
        dao.addGirlList(data.getShowapi_res_body().getNewslist());
    }

    @Override
    public void onError(String msg) {
        //吐司错误信息
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        //如果没有网络则无法刷新
        if (!isNetwork) {
            Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
            //结束刷新进度条的旋转
            mRefreshLayout.setRefreshing(false);
            return;
        }
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               //理论上应该只是刷新第一页 即参数 还是为 page
               //因为数据比较少，如果只是刷新第一页，无明显效果,所以将page换成随机页面
               int randomPage = (int) (Math.random()*40 + 1);
               //调用getGirlList方法重新请求数据
               mRequest.getGirlList(new RequsetCallback<GirlsBean>() {
                   @Override
                   public void onFinish(GirlsBean data) {
                       //适配器中写的方法，添加数据，并通知RecyclerView改变UI
                       mAdapter.addData(data.getShowapi_res_body().getNewslist());
                   }

                   @Override
                   public void onError(String msg) {
                       //吐司错误信息
                       Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                   }
               },randomPage);
               //结束刷新进度条的旋转
               mRefreshLayout.setRefreshing(false);
           }
       },2000);
    }

    NetworkChangeReceiver networkChangeReceiver;
    //注册网络变化广播接收器
    public void registerNetworkChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //广播的动作类型
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        //注册
        registerReceiver(networkChangeReceiver,intentFilter);
    }
    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //再次判断有无网络
            isNetwork = Network.checkNetworkState(MainActivity.this);
            //如果有网络
            if (isNetwork) {
                Toast.makeText(context,"已连接网络",Toast.LENGTH_SHORT).show();
                //如果有网络，调用getGirlList方法请求数据 1数据请求时的页面
                mRequest.getGirlList(MainActivity.this,1);
                //如果有网络，调用getGirlList方法请求数据 1数据请求时的页面
                Toast.makeText(context,"已断开网络",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //动态注册的广播接收器都要取消
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}

