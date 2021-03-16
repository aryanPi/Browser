package com.example.browser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.browser.R;
import com.example.browser.activities.WhatsAppActivity;
import com.example.browser.utils.GlobalVariables;

import java.io.File;
import java.util.ArrayList;

public class WhatsAppStoryAdapter   extends BaseAdapter {
    Context context;

    ArrayList<String> arrayList;

    LayoutInflater inflter;

    public WhatsAppStoryAdapter(Context applicationContext, ArrayList<String> a) {
        this.context = applicationContext;
        this.arrayList = a;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.whatsapp_stories_item, null); // inflate the layout

        ImageView image = (ImageView) view.findViewById(R.id.storyImage); // get the reference of ImageView
        ImageView play = (ImageView) view.findViewById(R.id.playImg);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbokImage);
        if (GlobalVariables.isSelectWAFlag()){
            checkBox.setVisibility(View.VISIBLE);
        }
        else{
            checkBox.setVisibility(View.GONE);
        }
        if (GlobalVariables.isSelectAllWA()){
            checkBox.setChecked(true);
            GlobalVariables.addSaveImagespathWA(arrayList.get(i));
        }else{
            checkBox.setChecked(false);
            GlobalVariables.deselectImagePathWA(arrayList.get(i));
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tomar", "onClick: ");
                if (checkBox.isChecked()){
                    GlobalVariables.addSaveImagespathWA(arrayList.get(i));
                }else{
                    GlobalVariables.deselectImagePathWA(arrayList.get(i));
                }
            }
        });
        if (!arrayList.get(i).endsWith(".mp4"))play.setVisibility(View.GONE);
        Glide.with(context).load(new File(arrayList.get(i))).centerCrop().into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.getVisibility()==View.VISIBLE){
                    checkBox.performClick();
                }else{
                    Log.d("tomar", "onClick: "+arrayList.get(i));
                    ((WhatsAppActivity)context).openImage(arrayList.get(i));

                }
            }
        });
        return view;
    }

}
