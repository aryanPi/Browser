package com.example.browser.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.browser.activities.History;
import com.example.browser.R;
import com.example.browser.model.HistoryModel;

import java.util.ArrayList;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context mContext;
    Activity mActivity;
    int mResource;
ArrayList<HistoryModel> obj;

    public HistoryAdapter(Context context,Activity activity, int resource, ArrayList<HistoryModel> objects) {
        mContext = context;
        mResource = resource;
        mActivity = activity;
        this.obj = objects;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Long stack = obj.get(position).getStack();
        String title = obj.get(position).getTitle();
        String url = obj.get(position).getUrl();
        String logo = obj.get(position).getLogo();

/*
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);
*/

        TextView tvTitle = holder.titleHistory;
        TextView tvUrl = holder.urlHistory;
        ImageView logoImage = holder.logoHistory;
        ImageView menuItem = holder.menuItemHistory;

        CheckBox checkBox = holder.checkbokHistory;


        if (logo.equals("")){
//            logoImage.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(),R.drawable.web_drawable));
        }else{
            logoImage.setImageBitmap(convert(logo));
        }

        if (History.select){
            checkBox.setVisibility(View.VISIBLE);
            if (History.selectAll){
                checkBox.setChecked(true);
                History.addThisItem(stack);
            }else{
                checkBox.setChecked(false);
                ((History)mActivity).removeThis(stack);
            }
        }else{
            checkBox.setVisibility(View.GONE);
        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    History.addThisItem(stack);
                }else{
                    ((History)mActivity).removeThis(stack);
                }
            }
        });

        menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((History)mActivity).save(title,url,logo,stack);
                ((History)mActivity).pop(menuItem);
            }
        });

        tvTitle.setText(title);
        tvUrl.setText(url);


        holder.mainBodyItemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.getVisibility()==View.GONE){
                    ((History)mActivity).openThisURL(url);
                    notifyDataSetChanged();
                }else{
                    checkBox.performClick();
                }
            }
        });


    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbokHistory;
        ImageView logoHistory;
        TextView titleHistory;
        TextView urlHistory;
        ImageView menuItemHistory;
        RelativeLayout mainBodyItemHistory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainBodyItemHistory = itemView.findViewById(R.id.mainBodyItemHistory);
            checkbokHistory = itemView.findViewById(R.id.checkbokHistory);
            logoHistory = itemView.findViewById(R.id.logoHistory);
            titleHistory = itemView.findViewById(R.id.titleHistory);
            urlHistory = itemView.findViewById(R.id.urlHistory);
            menuItemHistory = itemView.findViewById(R.id.menuItemHistory);
        }
    }


    @Override
    public int getItemCount() {
        return obj.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
