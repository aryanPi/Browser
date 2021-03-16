package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.browser.R;
import com.example.browser.adapter.ViewpagerAdapterDownloads;
import com.example.browser.databinding.ActivityDownloadsBinding;
import com.example.browser.databinding.ActivityPrivateFolderBinding;
import com.example.browser.databinding.ActivityPrivateFolderMainBinding;
import com.example.browser.fragment.PhototsFragPrivate;
import com.example.browser.fragment.VideosFragPrivate;
import com.example.browser.fragment.audio_frag;
import com.example.browser.fragment.downloading_frag;
import com.example.browser.fragment.photos_frag;
import com.example.browser.fragment.videos_frag;

import java.util.ArrayList;
import java.util.List;

public class PrivateFolderMain extends AppCompatActivity {


    private PagerAdapter pagerAdapter;

    ActivityPrivateFolderMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrivateFolderMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        List<Fragment> fragments = new ArrayList<>();


        fragments.add(new PhototsFragPrivate());
        fragments.add(new VideosFragPrivate());

        pagerAdapter = new ViewpagerAdapterDownloads(getSupportFragmentManager(), fragments);

        binding.pagerPrivateFolder.setAdapter(pagerAdapter);

        binding.pagerPrivateFolder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Log.d(TAG, "onPageScrolled: first "+position);
            }

            @Override
            public void onPageSelected(int position) {
                selectThis(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "onPageScrolled: thired "+state);
            }
        });

        binding.pagerPrivateFolder.setCurrentItem(0, true);

    }


    private void selectThis(Integer position) {
        if (position == 0) {
            binding.subViewVideosTextPrivateFolder.setTextColor(Color.parseColor("#7EFFFFFF"));
            binding.subViewVideosPrivateFolder.setVisibility(View.GONE);
            binding.subViewPhotoPrivateFolder.setVisibility(View.VISIBLE);
            binding.subViewPhotoTextPrivateFolder.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else {
            binding.subViewPhotoTextPrivateFolder.setTextColor(Color.parseColor("#7EFFFFFF"));
            binding.subViewVideosPrivateFolder.setVisibility(View.VISIBLE);
            binding.subViewPhotoPrivateFolder.setVisibility(View.GONE);
            binding.subViewVideosTextPrivateFolder.setTextColor(Color.parseColor("#FFFFFFFF"));        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}