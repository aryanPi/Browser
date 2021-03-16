package com.example.browser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.browser.R;

import java.util.ArrayList;

public class FirstTimeActivity extends AppCompatActivity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Browser);
        setContentView(R.layout.activity_first_time);

        pager = findViewById(R.id.startUpPager);

        ViewPagerFirstTimeAdapter adapter = new ViewPagerFirstTimeAdapter(this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    //                  ((Button) findViewById(R.id.buttonStartupPagebtn)).setText("Finish");
                    Glide.with(getBaseContext()).load(R.drawable.ic_baseline_check_24).into((ImageView) findViewById(R.id.changeAbleImageFirstTime));
                    findViewById(R.id.skipFirstTime).setVisibility(View.INVISIBLE);
                } else {
//                    ((Button) findViewById(R.id.buttonStartupPagebtn)).setText("Next");

                    Glide.with(getBaseContext()).load(R.drawable.right_arraw).into((ImageView) findViewById(R.id.changeAbleImageFirstTime));
                    findViewById(R.id.skipFirstTime).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void buttonStartupPage(View view) {
        if (pager.getCurrentItem() == 2) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    public void skip(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    public class ViewPagerFirstTimeAdapter extends PagerAdapter {

        private Context mContext;

        public ViewPagerFirstTimeAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            ModelObject modelObject = ModelObject.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
            if (layout.findViewById(R.id.image1FirstTime) != null)
                Glide.with(mContext).load(R.drawable.first1).sizeMultiplier(0.8f).into((ImageView) layout.findViewById(R.id.image1FirstTime));
            if (layout.findViewById(R.id.image3firsttime) != null)
                Glide.with(mContext).load(R.drawable.first3).sizeMultiplier(0.8f).into((ImageView) layout.findViewById(R.id.image3firsttime));
            if (layout.findViewById(R.id.image2firsttime) != null)
                Glide.with(mContext).load(R.drawable.first2).sizeMultiplier(0.8f).into((ImageView) layout.findViewById(R.id.image2firsttime));
            if (layout.findViewById(R.id.first4image) != null)
                Glide.with(mContext).load(R.drawable.first4).sizeMultiplier(0.7f).into((ImageView) layout.findViewById(R.id.first4image));
            if (layout.findViewById(R.id.first5image) != null)
                Glide.with(mContext).load(R.drawable.first5).sizeMultiplier(0.7f).into((ImageView) layout.findViewById(R.id.first5image));

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return ModelObject.values().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //ModelObject customPagerEnum = ModelObject.values()[position];
            return "";
        }

    }


    public enum ModelObject {

        RED(R.layout.page1_first_time),
        BLUE(R.layout.page2_first_time),
        GREEN(R.layout.page3_first_time);

        //        private final int mTitleResId;
        private final int mLayoutResId;

        ModelObject(/*int titleResId, */int layoutResId) {
//            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }


        public int getLayoutResId() {
            return mLayoutResId;
        }

    }


}