package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.browser.R;
import com.example.browser.adapter.HistoryAdapter;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.BookmarksModel;
import com.example.browser.model.HistoryModel;
import com.example.browser.model.HomePageBookmark;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class History extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    public static boolean select = false;
    public static boolean selectAll = false;
    public static ArrayList<Long> selectedItems = new ArrayList<>();

    private static Context context;
    HistoryModel selectedItem = new HistoryModel();
    ArrayList<HistoryAdapter> historyAdapter = new ArrayList<>();
    //RecyclerView historyList;
    ArrayList<HistoryModel> data;
    LottieAnimationView noHistory;
    ImageView clearHistory;
    CheckBox checkBox;
    static TextView historyTextView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Browser);
        setContentView(R.layout.activity_history);


        select = false;
        selectAll = false;

        data = new ArrayList<>();

        context = this;


        noHistory = findViewById(R.id.noHistory);
        clearHistory = findViewById(R.id.clearAllHistory);
        checkBox = findViewById(R.id.selectallBtnHistory);
        historyTextView = findViewById(R.id.historyTextView);


        initialeData();


        clearHistory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (select) {
                    deleteSelectedItem();
                } else {
                    checkBox.setVisibility(View.VISIBLE);
                    select = true;
                    for (HistoryAdapter adapter : historyAdapter) adapter.notifyDataSetChanged();
                    clearHistory.setBackground(getResources().getDrawable(R.drawable.delete_now_drawable));
                }

            }
        });

        checkBox.setOnClickListener(v -> {
            selectAll = checkBox.isChecked();
            for (HistoryAdapter adapter : historyAdapter) adapter.notifyDataSetChanged();
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addToBookmarkHistory:
                addToBookMark();
                break;
            case R.id.deleteHistoryItem:
                deleteItemHistory();
                break;
            case R.id.openInNewTabHistory:
                openInNewTabHistory();
                break;
            case R.id.addToHomeHistory:
                addToHome();
                break;
            default:
                break;
        }
        return true;
    }

    private void addToHome() {
        Realm realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        Number size = realm.where(HomePageBookmark.class).findAll().max("id");
        HomePageBookmark obj = new HomePageBookmark();
        obj.setTitle(selectedItem.getTitle());
        obj.setUri(selectedItem.getLogo());
        obj.setUrl(selectedItem.getUrl());
        try {
            obj.setId(size.intValue() + 1);
        } catch (Exception e) {
            obj.setId(0);
            e.printStackTrace();
        }

        realm.copyToRealm(obj);

        realm.commitTransaction();

        ((MainActivity) GlobalVariables.getmActivity()).updateHomePageIcons();
    }

    public void openThisURL(String url) {
        ((MainActivity) GlobalVariables.getmActivity()).searQuery(url);
        finish();
    }

    private void openInNewTabHistory() {
        ((MainActivity) GlobalVariables.getmActivity()).newTabOpen();
        ((MainActivity) GlobalVariables.getmActivity()).searQuery(selectedItem.getUrl());
        finish();
    }

    private void deleteItemHistory() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<HistoryModel> histories = realm.where(HistoryModel.class).equalTo("stack", selectedItem.getStack()).findAll();
        histories.deleteAllFromRealm();
        realm.commitTransaction();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getStack() == selectedItem.getStack()) {
                data.remove(i);
                break;
            }
        }
        if (data.size() == 0) {
            clearHistory.setVisibility(View.GONE);
            noHistory.setVisibility(View.VISIBLE);
        } else {
            initialeData();
        }
    }

    private void addToBookMark() {
        BookmarksModel bookmarksModel = new BookmarksModel();
        bookmarksModel.setLogo(selectedItem.getLogo());
        bookmarksModel.setTitle(selectedItem.getTitle());
        bookmarksModel.setUrl(selectedItem.getUrl());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(bookmarksModel);
        realm.commitTransaction();
        Toast.makeText(this, "Added To Bookmark", Toast.LENGTH_SHORT).show();
    }

    public void pop(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.history_item_menu);
        popupMenu.show();
    }

    public void save(String title, String url, String logo, Long stack) {
        selectedItem.setStack(stack);
        selectedItem.setTitle(title);
        selectedItem.setUrl(url);
        selectedItem.setLogo(logo);
    }

    public void backHistoryBtn(View view) {
        onBackPressed();
    }


    public void removeThis(Long stack) {
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.get(i).equals(stack)) {
                selectedItems.remove(i);
                break;
            }
        }

        selectAll = false;
        checkBox.setChecked(false);
        historyTextView.setText(selectedItems.size() + " Selected");
    }

    private void deleteSelectedItem() {
        Realm realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        if (selectAll) {
            realm.where(HistoryModel.class).findAll().deleteAllFromRealm();
            data.clear();
        } else {
            for (int i = 0; i < selectedItems.size(); i++) {
                realm.where(HistoryModel.class).equalTo("stack", selectedItems.get(i)).findAll().deleteAllFromRealm();
            }

            for (int j = 0; j < data.size(); j++) {
                if (selectedItems.contains(data.get(j).getStack())) {
                    data.remove(j);
                }
            }
        }
        realm.commitTransaction();

        select = false;
        selectAll = false;
        selectedItems.clear();

        initialeData();

        historyTextView.setText("History");
        checkBox.setVisibility(View.GONE);

        if (data.size() == 0) clearHistory.setBackground(getDrawable(R.drawable.delete_drawable));

    }

    public void initialeData() {
        data.clear();
        historyAdapter.clear();
        data = new ArrayList<>();
        historyAdapter = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        data.addAll(realm.copyFromRealm(realm.where(HistoryModel.class).sort("date").findAll()));
        realm.commitTransaction();

        ArrayList<HistoryModel> newLst = new ArrayList<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            newLst.add(data.get(i));
        }

        ArrayList<ArrayList<HistoryModel>> historyListModel = new ArrayList<>();
        for (HistoryModel h : newLst) {
            boolean flag1 = true;
            for (ArrayList<HistoryModel> al : historyListModel) {
                if (al.get(0) != null)
                    if (al.get(0).getDate().getDate() == h.getDate().getDate()) {
                        al.add(h);
                        flag1 = false;
                    }
            }
            if (flag1) {
                ArrayList<HistoryModel> t = new ArrayList<>();
                t.add(h);
                historyListModel.add(t);
            }
        }


        LinearLayout listLayout = findViewById(R.id.recycler_view_list);
        listLayout.removeAllViews();
        listLayout.setPadding(0, 10, 0, 0);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        formatter.format(date);

        for (ArrayList<HistoryModel> models : historyListModel) {
            RecyclerView recyclerView = new RecyclerView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);
            recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            TextView t = new TextView(this);
            t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            t.setPadding(15, 10, 5, 5);
            t.setTextColor(getColor(R.color.lightWhite));
            t.setTextSize(16f);
            if (models.get(0).getDate().getDate() == date.getDate()) t.setText("Today");
            else if (models.get(0).getDate().getDate() == date.getDate() - 1)
                t.setText("Yesterday");
            else
                t.setText(models.get(0).getDate().getDate() + " " + extraFunctions.getMonth(models.get(0).getDate().getMonth()).substring(0, 3) + " " + (models.get(0).getDate().getYear() + 1900));
            listLayout.addView(t);
            HistoryAdapter adapter = new HistoryAdapter(this, this, R.layout.history_item, models);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);
            historyAdapter.add(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            listLayout.addView(recyclerView);
        }
    }


    public static void addThisItem(Long l) {
        if (!selectedItems.contains(l)) {
            selectedItems.add(l);
            historyTextView.setText(selectedItems.size() + " Selected");
        }
    }


    @Override
    public void onBackPressed() {
        if (checkBox.getVisibility() == View.VISIBLE) {
            checkBox.setVisibility(View.GONE);

            selectAll = false;
            select = false;
            selectedItems.clear();
            historyTextView.setText("History");
            for (HistoryAdapter adapter : historyAdapter) adapter.notifyDataSetChanged();

        } else {
            super.onBackPressed();
        }
    }
}