package com.byulvi.shushasocial.ui;

import android.content.Context;
import android.widget.ImageView;

import com.byulvi.shushasocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class UniversalImageAdapter {
    Context context;

    public UniversalImageAdapter(Context context) {
        this.context = context;
    }
    public ImageLoaderConfiguration getconfig(){
        DisplayImageOptions defaultopt = new DisplayImageOptions.Builder().showImageForEmptyUri(context.getDrawable(R.drawable.transperent)).showImageOnFail(R.drawable.transperent).showImageOnLoading(R.drawable.transperent).cacheOnDisk(true).cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(200)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(defaultopt).diskCacheSize(100*1024*1024).build();
        return config;
    }
    public static  void setimage(String imgurl, ImageView iv){
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgurl,iv);

    }
}
