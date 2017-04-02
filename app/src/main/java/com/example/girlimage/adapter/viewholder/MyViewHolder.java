package com.example.girlimage.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.girlimage.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    //RecyclerView中只包含一个ImageView
    public ImageView mImageView;

    public MyViewHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.item_iv_girls);
    }
}


