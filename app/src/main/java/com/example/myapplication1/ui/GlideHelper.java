package com.example.myapplication1.ui;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication1.R;

import okhttp3.OkHttpClient;

public class GlideHelper {
    private static final int DEFAULT_IMAGE = R.drawable.load;//设置加载前和加载错误时的默认图片

    private Context context;

    public OkHttpClient okHttpClient;
    public GlideHelper(Context context){
        this.context = context;
    }

    public void setGilde(String url, ImageView imageView){//设置的是默认缓存策略

        Glide.with(context)
                .load(url)
                .placeholder(DEFAULT_IMAGE)
                .error(DEFAULT_IMAGE)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))//设置图片圆角
                .into(imageView);
    }
}
