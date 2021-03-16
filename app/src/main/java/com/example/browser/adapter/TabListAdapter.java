package com.example.browser.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.browser.activities.MainActivity;
import com.example.browser.R;
import com.example.browser.fragment.newTabList;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.TabModel;
import com.example.browser.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabListAdapter extends ArrayAdapter<TabModel> {

    Context mContext;
    Activity mActivity;
    int mResource;
    Fragment fragment;

    public TabListAdapter(Context context, Activity activity, int resource, Fragment fragment) {
        super(context, resource, GlobalVariables.getTabModels());
        mContext = context;
        mResource = resource;
        mActivity = activity;
        this.fragment = fragment;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id = getItem(position).getId();
        String title = getItem(position).getTitle();
        String url = getItem(position).getUrl();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        if (GlobalVariables.getWebPageDatas(id) != null)
            if (!GlobalVariables.getWebPageDatas(id).getFrvicon().equals(""))
                Glide.with(mContext).load(extraFunctions.convert(GlobalVariables.getWebPageDatas(id).getFrvicon())).circleCrop().into((ImageView) convertView.findViewById(R.id.tabIconOfPage));

        TextView tvTitle = convertView.findViewById(R.id.titleOfPage);
        TextView tvUrl = convertView.findViewById(R.id.urlOfSite);
        ImageView close = convertView.findViewById(R.id.close);


        if (id == GlobalVariables.getActiveWebView()) {
            tvTitle.setTextColor(Color.BLUE);
            tvUrl.setTextColor(Color.BLUE);
        }

        tvTitle.setText(title);
        if (!url.equals("")) {
            tvUrl.setText(url);
        }else tvUrl.setVisibility(View.GONE);


        close.setOnClickListener(v -> {
            ((MainActivity) mActivity).closeTab(id);
            if (GlobalVariables.getTabModels().size() == 1) {
                ((newTabList) fragment).close();
            }
            notifyDataSetChanged();
        });

        convertView.setOnClickListener(v -> {
            ((MainActivity) mActivity).openThisTab(id);
        notifyDataSetChanged();
        });


        return convertView;
    }
}
