package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.browser.R;
import com.example.browser.adapter.ViewPagerImageGalleryAdapter;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.ModelImage;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.PathUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ImageSliderGallery extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    ViewPagerImageGalleryAdapter adapter;

    static ViewPager viewPager;

    @SuppressLint("StaticFieldLeak")
    static TextView indexOfcurrentImage;

    public static ArrayList<ModelImage> arrayList;

    public static void performClick() {
        topRelative.setVisibility(topRelative.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        bottomLinearLayout.setVisibility(bottomLinearLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private static RelativeLayout topRelative;
    private static LinearLayout bottomLinearLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_slider_gallery);

        viewPager = findViewById(R.id.viewPagerImagesGallery);
        topRelative = findViewById(R.id.topRelative);
        bottomLinearLayout = findViewById(R.id.bottomLinearLayout);
        Intent intent = getIntent();


        TextView totalImages = findViewById(R.id.totalIndexOfImages);
        indexOfcurrentImage = findViewById(R.id.currentIndexOfImage);

        findViewById(R.id.backImageSliderGallery).setOnClickListener(v -> onBackPressed());


        String data = intent.getExtras().getString("itemId");

        int position = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getData().equals(data)) {
                position = i;
                break;
            }
        }


        String s = String.valueOf(position + 1);
        totalImages.setText("/" + arrayList.size());
        indexOfcurrentImage.setText(s);

        adapter = new ViewPagerImageGalleryAdapter(this, arrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);


        findViewById(R.id.shareImageImageSliderGallery).setOnClickListener(v -> shareImage());

        findViewById(R.id.menuImageImageSliderGallery).setOnClickListener(this::menu);

        findViewById(R.id.deleteImageImageSliderGallery).setOnClickListener(v -> deleteImage());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indexOfcurrentImage.setText((position + 1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void deleteImage() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_downloaded_item);
        TextView delete = dialog.findViewById(R.id.delete_btn_delete);
        TextView cancel = dialog.findViewById(R.id.cancel_btn_delete);
        cancel.setOnClickListener(v -> dialog.dismiss());
        delete.setOnClickListener(v -> {
            deleteFile();
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void deleteFile() {
        int i = viewPager.getCurrentItem();
        try {
            if ((new File(PathUtil.getPath(this, Uri.parse(arrayList.get(i).getData())))).delete()) {
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void menu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.image_gallery_item_meny);
        popupMenu.show();
    }

    private void shareImage() {
        int i = viewPager.getCurrentItem();
        try {
            extraFunctions.shareFile(PathUtil.getPath(this, Uri.parse(arrayList.get(i).getData())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setAsWallPaperImageSliderGallery:
                setAsWallpaperImage();
                break;
            case R.id.renameImageSliderGallery:
                renameFile();
                break;
            default:
                break;
        }
        return true;
    }

    private void setAsWallpaperImage() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.set_as_wallpaper_dialog);

        TextView homeScreen = dialog.findViewById(R.id.HomeScreenWallpaperDialogBtn);
        TextView lockScreen = dialog.findViewById(R.id.LockScreenWallpaperDialogBtn);

        homeScreen.setOnClickListener(v -> {
            setwallpaperNow(1);
            dialog.dismiss();
        });
        lockScreen.setOnClickListener(v -> {
            setwallpaperNow(2);
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setwallpaperNow(int j) {
        int i = viewPager.getCurrentItem();
        Uri image = Uri.parse(arrayList.get(i).getData());
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());


        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (j == 1) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                }
                Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);//For Lock screen
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lock screen walpaper not supported",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "e.message", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void renameFile() {
        int i = viewPager.getCurrentItem();
        String path;
        try {
            path = PathUtil.getPath(this, Uri.parse(arrayList.get(i).getData()));
        } catch (URISyntaxException e) {
            return;
        }


        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rename_dialog);
        EditText nameOfFile = dialog.findViewById(R.id.nameOfFile);


        String filename = path.substring(path.lastIndexOf("/") + 1);

        nameOfFile.setText(filename);

        TextView cancel = dialog.findViewById(R.id.cancel_btn_rename);
        TextView rename = dialog.findViewById(R.id.rename_btn_rename);
        cancel.setOnClickListener(v -> dialog.dismiss());

        String finalPath = path;
        rename.setOnClickListener(v -> {
            renameBtnClicked(finalPath.substring(0, finalPath.lastIndexOf("/") + 1) + nameOfFile.getText().toString(), finalPath);
            dialog.dismiss();
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        nameOfFile.requestFocus();
        nameOfFile.postDelayed(() -> {
            InputMethodManager keyboard = (InputMethodManager)
                    GlobalVariables.getmActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(nameOfFile, 0);


        }, 300);
    }


    private void renameBtnClicked(String newNameWithPath, String OldNameWithPath) {
        File newFile = new File(newNameWithPath);
        File OldFile = new File(OldNameWithPath);
        boolean sucess = OldFile.renameTo(newFile);
        if (sucess) {
            Toast.makeText(this, "Renamed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrayList = null;
    }


}

