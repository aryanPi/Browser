package com.example.browser.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.browser.activities.Bookmarks;
import com.example.browser.R;
import com.example.browser.activities.History;
import com.example.browser.model.BookmarksModel;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {

    Context mContext;
    Activity mActivity;
ArrayList<BookmarksModel> obj;

    public BookmarkAdapter(Context context, Activity activity, ArrayList<BookmarksModel> objects) {
        mContext = context;
        mActivity = activity;
        this.obj = objects;
    }


    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @NonNull
    @Override
    public BookmarkAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_item, parent, false);
        return new BookmarkAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.MyViewHolder holder, int position) {
        String title = obj.get(position).getTitle();
        String url = obj.get(position).getUrl();
        String logo = obj.get(position).getLogo();

        TextView tvTitle = holder.titleBookmark;
        TextView tvUrl = holder.urlBookmark;
        ImageView logoImage = holder.logoBookmark;
        ImageView menu = holder.menuItemBookmark;

        CheckBox checkBox = holder.checkbokBookmark;

        if (Bookmarks.select){
            checkBox.setVisibility(View.VISIBLE);
            if (Bookmarks.selectAll){
                checkBox.setChecked(true);
                Bookmarks.addThisItem(url);
            }else{
                checkBox.setChecked(false);
                Bookmarks.removeThis(url);
            }
        }else{
            checkBox.setVisibility(View.GONE);
        }


        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()){
                Bookmarks.addThisItem(url);
            }else{
                Bookmarks.removeThis(url);
            }
        });


        tvTitle.setText(title);
        tvUrl.setText(url);
        logoImage.setImageBitmap(convert(logo));

        menu.setOnClickListener(v -> {
            ((Bookmarks)mActivity).save(title,url,logo);
            ((Bookmarks)mActivity).pop(menu);
        });


        holder.mainBodyItemBookmark.setOnClickListener(v -> {
            if (checkBox.getVisibility()==View.GONE){
                ((Bookmarks)mActivity).openThisURL(url);
            }else{
                checkBox.performClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbokBookmark;
        ImageView logoBookmark;
        TextView titleBookmark;
        TextView urlBookmark;
        ImageView menuItemBookmark;
        RelativeLayout mainBodyItemBookmark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainBodyItemBookmark = itemView.findViewById(R.id.mainBodybookmarkItem);
            checkbokBookmark = itemView.findViewById(R.id.checkBookmark);
            logoBookmark = itemView.findViewById(R.id.logoBookmark);
            titleBookmark = itemView.findViewById(R.id.titleBookmark);
            urlBookmark= itemView.findViewById(R.id.urlBookmark);
            menuItemBookmark = itemView.findViewById(R.id.menuItemBookmark);
        }
    }


}
