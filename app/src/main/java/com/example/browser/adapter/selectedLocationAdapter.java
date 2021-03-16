package com.example.browser.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.browser.R;
import com.example.browser.activities.selectLocation;
import com.example.browser.utils.ItemClickListener;

import java.util.ArrayList;

public class selectedLocationAdapter extends RecyclerView.Adapter <selectedLocationAdapter.ViewHolder>{
    private ArrayList<String> pathSeg;
Activity mActivity;
    private Context context;
    public selectedLocationAdapter(Context context, ArrayList<String> pathSeg, Activity mActivity) {
        super();
        this.context = context;
        this.pathSeg = pathSeg;
        this.mActivity = mActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pre_location_item, viewGroup, false);
        return new ViewHolder(v,mActivity);}

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(pathSeg.get(i));
        viewHolder.pos = i;
    }
    @Override
    public int getItemCount() {
        return pathSeg.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        Integer pos;
        TextView textView;
        private ItemClickListener clickListener;
        Activity mActivity;
        ViewHolder(View itemView,Activity mActivity) {
            super(itemView);
            this.mActivity = mActivity;
            textView = itemView.findViewById(R.id.preLocatiomTextView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            ((selectLocation)mActivity).topLocationClicked(pos);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
