package com.example.browser.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.browser.R;
import com.example.browser.activities.selectLocation;

import java.util.ArrayList;

public class SelectLocationAdapter extends ArrayAdapter<String> {

    Context mContext;

    int mResource;
    ArrayList<String> objects;
Activity mActivity;

    public SelectLocationAdapter(Context context, int resource, ArrayList<String> objects,Activity mActivity) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
this.objects = objects;
this.mActivity = mActivity;
    }


    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String DirName = objects.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView dir = convertView.findViewById(R.id.textViewSelectFileItem);
        dir.setText(DirName);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tomar", "onClick: HERE");
                ((selectLocation)mActivity).selectThisItem(DirName);
            }
        });

        return convertView;
    }



}

