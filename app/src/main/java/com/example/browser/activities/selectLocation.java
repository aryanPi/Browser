package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.browser.R;
import com.example.browser.adapter.SelectLocationAdapter;
import com.example.browser.adapter.selectedLocationAdapter;
import com.example.browser.model.extra;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.MyApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import io.realm.Realm;

public class selectLocation extends AppCompatActivity {
    ArrayList<String> listItems;
    ArrayList<String> preLocationDir = new ArrayList<>();
    ListView list;
    SelectLocationAdapter adapter;
    selectedLocationAdapter adapterSelectedLocation;
    RecyclerView recyclerView;
    String currentDir = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Browser);
        setContentView(R.layout.activity_select_location);
        preLocationDir.add("Parent");

        list = findViewById(R.id.fileLocationList);

        listItems = new ArrayList<>();


        recyclerView = findViewById(R.id.previourSelectedDir);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        runThis("");


    }

    private void runThis(String extended) {
        if (!extended.equals("")) {
            preLocationDir.add(extended);
        }

        listItems = new ArrayList<>();
        String d = currentDir;
        for (int i = 1; i < preLocationDir.size(); i++) {
            d += "/" + preLocationDir.get(i);
        }
        Log.d("locationDir", "runThis: " + d);
        File file = new File(d);
        String[] files = file.list();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].contains("."))
                    listItems.add(files[i]);
            }

            adapter = new SelectLocationAdapter(this, R.layout.select_file_item, listItems, this);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        } else {
            listItems = new ArrayList<>();
            adapter = new SelectLocationAdapter(this, R.layout.select_file_item, listItems, this);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        adapterSelectedLocation = new selectedLocationAdapter(this, preLocationDir, this);
        recyclerView.setAdapter(adapterSelectedLocation);


    }

    public void selectThisItem(String dirName) {
        runThis(dirName);
    }

    public void topLocationClicked(int position) {
        for (int i = preLocationDir.size() - 1; i > position; i--) {
            Log.d("tomar", "topLocationClicked: " + i + " ; " + preLocationDir.get(i));
            preLocationDir.remove(i);

        }

        runThis("");
    }

    @Override
    public void onBackPressed() {
        if (preLocationDir.size() > 1) {
            preLocationDir.remove(preLocationDir.size() - 1);
            runThis("");
        } else {
            super.onBackPressed();
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void selectbtn(View view) {
        String d = currentDir;
        for (int i = 1; i < preLocationDir.size(); i++) {
            d += "/" + preLocationDir.get(i);
        }
        GlobalVariables.getExtraData().setDownloadLocation(d);
        Realm realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        realm.where(extra.class).findFirst().setDownloadLocation(d);
        realm.commitTransaction();


        Intent intent = new Intent();
        intent.putExtra("downloadLocation", d);
        setResult(RESULT_OK, intent);
        finish();
    }


    }
