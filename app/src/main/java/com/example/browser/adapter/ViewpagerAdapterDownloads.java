package com.example.browser.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewpagerAdapterDownloads extends FragmentStatePagerAdapter {

    private List<Fragment> list;

    public ViewpagerAdapterDownloads(@NonNull FragmentManager fm, List<Fragment> list) {
        super(fm);
    this.list = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
