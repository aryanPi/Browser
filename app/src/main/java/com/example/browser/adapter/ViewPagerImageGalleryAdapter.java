package com.example.browser.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.browser.activities.Activity_Player;
import com.example.browser.activities.ImageSliderGallery;
import com.example.browser.model.ModelImage;

import java.util.ArrayList;

public class ViewPagerImageGalleryAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerImageGalleryAd";

    private Context context;
    ArrayList<ModelImage> imageArrayList;


    public ViewPagerImageGalleryAdapter(Context context, ArrayList<ModelImage> arrayList) {
        this.context = context;
        this.imageArrayList = arrayList;

    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(context);
        imageView.setImage(ImageSource.uri(imageArrayList.get(position).getData()));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSliderGallery.performClick();
            }
        });
        container.addView(imageView, 0);
        return imageView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((SubsamplingScaleImageView) object);
    }


}



