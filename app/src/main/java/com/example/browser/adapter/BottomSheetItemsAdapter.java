package com.example.browser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.example.browser.R;
import com.example.browser.fragment.audio_frag;
import com.example.browser.fragment.downloading_frag;
import com.example.browser.fragment.photos_frag;
import com.example.browser.fragment.videos_frag;

import java.util.List;

public class BottomSheetItemsAdapter extends ArrayAdapter<String> {
    Fragment fragment;
    int frag;

    public BottomSheetItemsAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, Fragment fragment, int frag) {
        super(context, resource, objects);
        this.fragment = fragment;
        this.frag = frag;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.bottom_sheet_items, parent, false);
        ((TextView) convertView.findViewById(R.id.textBottomSheetItem)).setText(getItem(position).substring(0,getItem(position).lastIndexOf("_")));
        int drawable;
        switch (getItem(position).substring(getItem(position).indexOf("_")+1)) {
            case "sh"://share
                drawable = R.drawable.share_drawable;
                break;
            case "de"://delete
                drawable = R.drawable.delete_drawable;
                break;
            case "lo"://location
                drawable = R.drawable.location_drable;
                break;
            case "re"://rename
                drawable = R.drawable.ic_rename;
                break;
            case "ret"://retry
                drawable = R.drawable.resume_download_drawable;
                break;
            case "pab"://play as background
                drawable = R.drawable.play_drawable;
                break;
            case "at"://add to track
                drawable = R.drawable.play_drawable;
                break;
            case "drdm":
                drawable = R.drawable.download_drawable;
                break;
            case "rem":
                drawable = R.drawable.delete_drawable;
                break;
            default:
                drawable = R.drawable.ic_file;
        }
        Glide.with(getContext()).load(drawable).into((ImageView) convertView.findViewById(R.id.imageBottomSheetItem));
        convertView.setOnClickListener(v -> {
            switch (frag) {
                case 0:
                    ((downloading_frag) fragment).click(((TextView) v.findViewById(R.id.textBottomSheetItem)).getText().toString());
                    break;
                case 1:
                    ((videos_frag) fragment).click(((TextView) v.findViewById(R.id.textBottomSheetItem)).getText().toString());
                    break;
                case 2:
                    ((photos_frag) fragment).click(((TextView) v.findViewById(R.id.textBottomSheetItem)).getText().toString());
                    break;
                case 3:
                    ((audio_frag) fragment).click(((TextView) v.findViewById(R.id.textBottomSheetItem)).getText().toString());
                    break;
                default:
                    break;
            }

        });

        return convertView;
    }
}
