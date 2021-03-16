package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.browser.R;
import com.example.browser.adapter.ViewpagerAdapterDownloads;
import com.example.browser.databinding.ActivityDownloadsBinding;
import com.example.browser.fragment.audio_frag;
import com.example.browser.fragment.downloading_frag;
import com.example.browser.fragment.photos_frag;
import com.example.browser.fragment.videos_frag;
import com.example.browser.functions.PopupWindowCustom;
import com.example.browser.utils.GlobalVariables;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Downloads extends AppCompatActivity  {


    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private ArrayList<View> views = new ArrayList<>();
    private ArrayList<TextView> textViews = new ArrayList<>();

    private static final String TAG = "ASASA";

    private static final int PHOTOS_FRAG = 1;
    private static final int VIDEO_FRAG = 2;
    private static final int AUDIO_FRAG = 3;


    ActivityDownloadsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.Browser);

        binding = ActivityDownloadsBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        int openThisFrag = getIntent().getIntExtra("fragIndex", 0);


        binding.privateFolder.setOnClickListener(v -> startActivity(new Intent(Downloads.this, PrivateFolderPasswordEnter.class)));

        final ThreadedRequest tReq = new ThreadedRequest();

        binding.backDownloads.setOnClickListener(v -> onBackPressed());

        StartLoading();
        tReq.start(() -> {
            textViews.add(findViewById(R.id.downloads_subheadline_files));
            textViews.add(findViewById(R.id.pictures_subheadline_files));
            textViews.add(findViewById(R.id.videos_subheadline_files));
            textViews.add(findViewById(R.id.audio_subheadline_files));

            views.add(findViewById(R.id.viewDownloadsSubFragDownloads));
            views.add(findViewById(R.id.viewDownloadsSubFragPictures));
            views.add(findViewById(R.id.viewDownloadsSubFragVideos));
            views.add(findViewById(R.id.viewDownloadsSubFragAudio));


            binding.layoutSubfragDownloads.setOnClickListener(v -> viewPager.setCurrentItem(0, true));
            binding.layoutSubfragPicture.setOnClickListener(v -> viewPager.setCurrentItem(1, true));
            binding.layoutSubfragVideo.setOnClickListener(v -> viewPager.setCurrentItem(2, true));
            binding.layoutSubfragAudio.setOnClickListener(v -> viewPager.setCurrentItem(3, true));

            selectThis(openThisFrag);


            binding.deleteAllImage.setOnClickListener(v -> {
                if (binding.checkBoxAll.getVisibility() == View.VISIBLE) {
                    ((downloading_frag) downloading_frag.mFragment).deletePerformSelected();
                    ((downloading_frag) downloading_frag.mFragment).selectionMode(false);
/*                    ((downloading_frag) downloading_frag.mFragment).isSelectionModeDownloads = false;
                    ((downloading_frag) downloading_frag.mFragment).selectionMode(false);
                    checkAndUpdateCheckbox(false, 0);*/
                } else {
                    ((downloading_frag) downloading_frag.mFragment).selectedItems = 0;
                    ((downloading_frag) downloading_frag.mFragment).isSelectionModeDownloads = true;
                    ((downloading_frag) downloading_frag.mFragment).selectionMode(true);
                    checkAndUpdateCheckbox(true, 0);
                }
            });
            binding.checkBoxAll.setOnClickListener(v -> ((downloading_frag) downloading_frag.mFragment).selectAll(binding.checkBoxAll.isChecked()));


            List<Fragment> fragments = new ArrayList<>();


            fragments.add(new downloading_frag(Downloads.this));
            fragments.add(new photos_frag(Downloads.this));
            fragments.add(new videos_frag(Downloads.this));
            fragments.add(new audio_frag(Downloads.this));
            viewPager = findViewById(R.id.pager);
            pagerAdapter = new ViewpagerAdapterDownloads(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Log.d(TAG, "onPageScrolled: first "+position);
                }

                @Override
                public void onPageSelected(int position) {
                    selectThis(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "onPageScrolled: thired "+state);
                }
            });
            viewPager.setCurrentItem(openThisFrag, true);


            StopLoading();
        });


    }

    private void StopLoading() {
        Log.d("TAGDD1", "StopLoading: ");
        binding.loadingDownloads.setVisibility(View.GONE);

    }

    private void StartLoading() {
        Log.d("TAGDD1", "StartLoading: ");
        binding.loadingDownloads.setVisibility(View.VISIBLE);
    }

    private void selectThis(Integer position) {
        if (position != 0) {
            if (((downloading_frag) downloading_frag.mFragment).isSelectionModeDownloads)
                deseclect();
            binding.checkBoxAll.setVisibility(View.GONE);
            binding.deleteAllImage.setVisibility(View.GONE);
            ((downloading_frag)downloading_frag.mFragment).selectionMode(false);
        } else {
            binding.deleteAllImage.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < 4; i++) {
            if (i != position) {
                views.get(i).setVisibility(View.GONE);
                textViews.get(i).setTextColor(Color.parseColor("#7EFFFFFF"));
            } else {
                views.get(i).setVisibility(View.VISIBLE);
                textViews.get(i).setTextColor(Color.parseColor("#FFFFFFFF"));
            }
        }
    }




    public void checkAndUpdateCheckbox(boolean b, int count) {
        if (b) {
//            int countLocal = ((downloading_frag)downloading_frag.mFragment).getSelectedCount();
            binding.textMyFiles.setText(count + " Selected");
            if (binding.checkBoxAll.getVisibility() == View.GONE) {
                binding.checkBoxAll.setVisibility(View.VISIBLE);
            }
        } else {
            binding.textMyFiles.setText("My Files");
            if (binding.checkBoxAll.getVisibility() == View.VISIBLE) {
                binding.checkBoxAll.setVisibility(View.GONE);
            }
        }
    }

    public static class ThreadedRequest {
        private Handler mHandler;
        private Runnable pRunnable;


        public ThreadedRequest() {

            mHandler = new Handler();
        }

        public void start(Runnable newRun) {
            pRunnable = newRun;
            processRequest.start();
        }

        private Thread processRequest = new Thread() {
            public void run() {
                //Do you request here...
                if (pRunnable == null || mHandler == null) return;
                mHandler.post(pRunnable);
            }
        };
    }


    public void rename(Fragment fragment, String path, String title, Long flag) {
        Dialog dialog = new Dialog(fragment.getContext());
        dialog.setContentView(R.layout.rename_dialog);
        dialog.findViewById(R.id.cancel_btn_rename).setOnClickListener(v -> dialog.dismiss());
        ((EditText) dialog.findViewById(R.id.nameOfFile)).setText(title);
        try {
            dialog.findViewById(R.id.rename_btn_rename).setOnClickListener(v -> {
                if (new File(path + title).renameTo(new File(path + "/" + ((EditText) dialog.findViewById(R.id.nameOfFile)).getText().toString()))) {
                    Toast.makeText(fragment.getContext(), "Renamed", Toast.LENGTH_SHORT).show();
                    if (flag != -1) {
                        ((downloading_frag) fragment).rename(((EditText) dialog.findViewById(R.id.nameOfFile)).getText().toString(), flag);
                    }
                } else
                    Toast.makeText(fragment.getContext(), "Unable To Rename", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        } catch (Exception e) {
            Toast.makeText(fragment.getContext(), "Unable To Rename", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void deleteItem(String path, Fragment fragment, Long flag) {

        Dialog dialog = new Dialog(fragment.getContext());
        dialog.setContentView(R.layout.delete_downloaded_item);
        TextView delete = dialog.findViewById(R.id.delete_btn_delete);
        TextView cancel = dialog.findViewById(R.id.cancel_btn_delete);
        cancel.setOnClickListener(v -> dialog.dismiss());
        try {
            delete.setOnClickListener(v -> {
                if (new File(path).delete()) {
                    if (flag != -1 && flag != -2) {
                        ((downloading_frag) fragment).removeItem(flag);
                    } else if (flag == -2) {
                        ((audio_frag) fragment).removed();
                    }
                    Toast.makeText(fragment.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(fragment.getContext(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        } catch (Exception e) {
            Toast.makeText(fragment.getContext(), "Unable To Delete Item !", Toast.LENGTH_SHORT).show();
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void location(String s, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.location_dialoge);
        ((TextView) dialog.findViewById(R.id.locationText)).setText(s);
        dialog.findViewById(R.id.cancel_btn_downloadLocation).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.copy_btn_downloadLocation).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("location", s);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        if (((downloading_frag) downloading_frag.mFragment).isSelectionModeDownloads) {
            deseclect();
        } else {
            super.onBackPressed();
        }
    }

    private void deseclect() {
        ((downloading_frag) downloading_frag.mFragment).selectionModeDownloadsSetFalse();
        checkAndUpdateCheckbox(false, 0);
        ((downloading_frag) downloading_frag.mFragment).selectionMode(false);
        binding.checkBoxAll.setOnClickListener(null);
    }
}