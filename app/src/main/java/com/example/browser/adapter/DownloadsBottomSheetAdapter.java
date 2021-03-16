package com.example.browser.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.browser.R;
import com.example.browser.activities.MainActivity;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.DownloadsBottomSheetModel;
import com.example.browser.utils.GlobalVariables;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DownloadsBottomSheetAdapter extends ArrayAdapter<DownloadsBottomSheetModel> {

    Context context;
    ArrayList<DownloadsBottomSheetModel> list;
    int mResource;


    public DownloadsBottomSheetAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadsBottomSheetModel> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.context = context;
        this.mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(mResource, parent, false);


        if (getItem(position).getUrl() != null) {
            DownloadsBottomSheetModel obj = getItem(position);
            String title = obj.getTitle();
            ArrayList<String> url = obj.getUrl();
            ArrayList<String> size = obj.getSize();
            Bitmap image = obj.getImage();
            List<String> qualities = obj.getQualities();

            ImageView imageView = convertView.findViewById(R.id.downloaditemBottomsheetImage);
            TextView sizev = convertView.findViewById(R.id.sizeitemBottomsheetImage);
            TextView titlev = convertView.findViewById(R.id.titleitemBottomsheetImage);
            ImageView downloadBtn = convertView.findViewById(R.id.downloadbtnBottomSheetItem);
            Spinner quality = convertView.findViewById(R.id.QualityDownloadItem);
            ImageView rename = convertView.findViewById(R.id.renameDownloadItemBottomSheet);

            final int[] index = {0};

            if (qualities != null) {

                ArrayAdapter<String> adp1 = new ArrayAdapter<>(getContext(),
                        R.layout.spinner_item, qualities);
                quality.setAdapter(adp1);
                quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sizev.setText(android.text.format.Formatter.formatFileSize(context, Long.parseLong(size.get(qualities.indexOf(quality.getSelectedItem().toString())))));
                        index[0] = qualities.indexOf(quality.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                quality.setVisibility(View.GONE);
            }

            if (image != null) {
                convertView.findViewById(R.id.lottieLoadingImageDownloadItem).setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(image).centerCrop().into(imageView);
            }


            convertView.findViewById(R.id.sizeAndQuality).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.lottieLoadingTextDownloadItem).setVisibility(View.GONE);
            convertView.findViewById(R.id.textAndRenameLayout).setVisibility(View.VISIBLE);


            if (sizev.getVisibility() != View.VISIBLE) {
                sizev.setVisibility(View.VISIBLE);
                titlev.setVisibility(View.VISIBLE);
                downloadBtn.setVisibility(View.VISIBLE);
                sizev.setText(android.text.format.Formatter.formatFileSize(context, Long.parseLong(size.get(0))));
                titlev.setText(title);
                String name = titlev.getText().toString();
                rename.setOnClickListener(v -> rename(titlev,getContext()));
                downloadBtn.setOnClickListener(v -> ((MainActivity) GlobalVariables.getmActivity()).downloadprocessInit(url.get(index[0]), Long.parseLong(size.get(index[0])), name, extraFunctions.convert(image), "video", true));

            }

        }

        return convertView;
    }


    private void rename(@NotNull TextView tv,Context t) {
        Dialog dialog = new Dialog(GlobalVariables.getmActivity());
        dialog.setContentView(R.layout.rename_dialog);
        EditText nameOfFile = dialog.findViewById(R.id.nameOfFile);
        nameOfFile.setText(tv.getText());
        TextView cancel = dialog.findViewById(R.id.cancel_btn_rename);
        TextView rename = dialog.findViewById(R.id.rename_btn_rename);
        cancel.setOnClickListener(v -> dialog.dismiss());

        rename.setOnClickListener(v -> {
            tv.setText(nameOfFile.getText().toString());
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
