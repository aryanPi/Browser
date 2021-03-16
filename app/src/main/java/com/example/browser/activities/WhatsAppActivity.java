package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.browser.R;
import com.example.browser.adapter.WhatsAppStoryAdapter;
import com.example.browser.utils.GlobalVariables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class WhatsAppActivity extends AppCompatActivity {

    Fragment OpenImage;
    ListView listStories;
    File[] files;
    GridView simpleGrid;
    ImageView selectbtn;
    CheckBox selectAllCB;
    ImageView saveBtn;
    WhatsAppStoryAdapter customAdapter;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);

        selectbtn = findViewById(R.id.selectBtnWA);
        selectAllCB = findViewById(R.id.selectAll);
        saveBtn = findViewById(R.id.saveBtnWA);

        simpleGrid = findViewById(R.id.listStories);

        customAdapter = new WhatsAppStoryAdapter(this, getData());
        simpleGrid.setAdapter(customAdapter);


        findViewById(R.id.backWABtn).setOnClickListener(v -> {
            if (GlobalVariables.isSelectWAFlag()) {
                selectAllCB.setVisibility(View.GONE);
                GlobalVariables.setSelectAllWA(false);
                GlobalVariables.setSelectWAFlag(false);
                saveBtn.setVisibility(View.GONE);
                customAdapter.notifyDataSetChanged();

            } else {
                finish();
            }
            selectbtn.setVisibility(View.VISIBLE);
        });
        selectbtn.setOnClickListener(v -> {
            selectbtn.setVisibility(View.GONE);
            selectAllCB.setVisibility(View.VISIBLE);
            GlobalVariables.setSelectWAFlag(true);
            saveBtn.setVisibility(View.VISIBLE);

            customAdapter.notifyDataSetChanged();
        });
        selectAllCB.setOnClickListener(v -> {
            if (selectAllCB.isChecked()) {
                GlobalVariables.setSelectAllWA(true);
            } else {
                GlobalVariables.setSelectAllWA(false);
            }
            customAdapter.notifyDataSetChanged();
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> temp = GlobalVariables.getSaveImagespathWA();
                checkDir();
                for (String s : temp) {
                    try {

                        saveImage(new File(s), new File(getSavePath(s)));
                        Toast.makeText(getApplicationContext(), "Saved !", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selectbtn.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    private String getSavePath(String s) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + constants.SAVE_FOLDER_NAME + s.substring(s.lastIndexOf('/') + 1, s.length());
    }

    private void checkDir() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + constants.SAVE_FOLDER_NAME;
        boolean ret = true;
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ret = false;
            }
        }
    }

    public static void saveImage(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    private ArrayList<String> getData() {
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + constants.WA_FOLDER_NAME + "Media/.Statuses";
        File targetDir = new File(targetPath);
        ArrayList<String> path = new ArrayList<>();
        files = targetDir.listFiles();

        if (files != null)
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.getPath().endsWith(".nomedia")) {
                    path.add(file.getAbsolutePath());
                }
            }
        return path;
    }

    public void openImage(String path) {

    }

    @Override
    public void onBackPressed() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (findViewById(R.id.wraperOpenImage).getVisibility() == View.GONE) {
            if (GlobalVariables.isSelectWAFlag()) {
                selectAllCB.setVisibility(View.GONE);
                GlobalVariables.setSelectAllWA(false);
                GlobalVariables.setSelectWAFlag(false);
                saveBtn.setVisibility(View.GONE);
                customAdapter.notifyDataSetChanged();
            } else {
                finish();
            }
        } else {
            findViewById(R.id.wraperOpenImage).setVisibility(View.GONE);
            findViewById(R.id.mainBodyWA).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.selectBtnWA).setVisibility(View.VISIBLE);

    }
}
