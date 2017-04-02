package com.example.girlimage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.girlimage.R;
import com.example.girlimage.adapter.viewholder.MyViewHolder;
import com.example.girlimage.bean.GirlsBean;

import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    //NewslistBean 包含有关图片的所有信息
    private List<GirlsBean.ShowapiResBodyBean.NewslistBean> mList;

    public MyAdapter(Context context, List<GirlsBean.ShowapiResBodyBean.NewslistBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_image, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //Glide设置图片(未加载则用粉色替代)
        final String imageUrl = mList.get(position).getPicUrl();
        Glide.with(mContext)
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.colorAccent)
                .into(holder.mImageView);
        //将数据保存在mImageView的TAG中，以便需要时取出
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果接口对象不为空
                if (mOnItemClickListener != null) {
                    //将点击事件转移给自定义的接口
                    mOnItemClickListener.onItemClick(imageUrl);
                    Log.e("TAG","-----------------adapter---" +  imageUrl);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //添加数据 用于下拉刷新
    public void addData(List<GirlsBean.ShowapiResBodyBean.NewslistBean> newDatas) {
        //向一个新的集合，添加以前所有的数据
        newDatas.addAll(mList);
        //以前集合的数据全部移除
        mList.removeAll(mList);
        //添加最新的集合里的数据
        mList.addAll(newDatas);
        //数据改变，通知RecyclerView改变视图
        notifyDataSetChanged();
    }

    //用于上拉加载
    public void addMoreItem(List<GirlsBean.ShowapiResBodyBean.NewslistBean> newDatas) {
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }

    //自定义的接口 用于传递图片地址给跳转的Activity
    public interface OnItemClickListener {
        //data 用于获得图片的网址
        void onItemClick(String data);
    }

    //声明接口变量
    private OnItemClickListener mOnItemClickListener = null;

    //暴露接口给外部
    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        this.mOnItemClickListener = OnItemClickListener;
    }
}
