package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.browser.R;
import com.example.browser.adapter.BookmarkAdapter;
import com.example.browser.model.BookmarksModel;

import com.example.browser.model.HomePageBookmark;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.MyApplication;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Bookmarks extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static boolean selectAll = false;
    public static boolean select = false;
    public static ArrayList<String> selectedItems = new ArrayList<>();


    BookmarksModel selectedItem = new BookmarksModel();
    BookmarkAdapter bookmarAdapter;
    ArrayList<BookmarksModel> data;
    RecyclerView bookmarkListView;
    LottieAnimationView noBookmark;
    ImageView clearBookmark;
    CheckBox checkBox;
    static TextView bookmarkTextView;
    ImageView backBookmarkBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Browser);
        setContentView(R.layout.activity_bookmarks);

        select = false;
        selectAll = false;

        noBookmark = findViewById(R.id.noBookmark);
        clearBookmark = findViewById(R.id.clearAllBookmark);
        checkBox = findViewById(R.id.selectAllbookmarks);
        bookmarkTextView = findViewById(R.id.bookmarkTextView);
        backBookmarkBtn = findViewById(R.id.backBtnBookmark);

        data = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<BookmarksModel> bookmarksModels = realm.where(BookmarksModel.class).findAll();
        data.addAll(realm.copyFromRealm(bookmarksModels));
        realm.commitTransaction();

        bookmarkListView = findViewById(R.id.bookmarListView);

        if (data.size() == 0) {
            clearBookmark.setVisibility(View.GONE);
            noBookmark.setVisibility(View.VISIBLE);
            bookmarkListView.setVisibility(View.GONE);
        }


        bookmarAdapter = new BookmarkAdapter(this, this, data);
        bookmarAdapter.setHasStableIds(true);
        bookmarkListView.setLayoutManager(new GridLayoutManager(this, 1));
        bookmarkListView.setAdapter(bookmarAdapter);

        clearBookmark.setOnClickListener(v -> {
            if (select) {
                deleteSelectedItem();
            } else {
                checkBox.setVisibility(View.VISIBLE);
                select = true;
                bookmarAdapter.notifyDataSetChanged();
                clearBookmark.setBackground(getResources().getDrawable(R.drawable.delete_now_drawable));
            }

        });

        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                selectAll = true;
                bookmarAdapter.notifyDataSetChanged();
            } else {
                selectAll = false;
                bookmarAdapter.notifyDataSetChanged();
            }
        });


        backBookmarkBtn.setOnClickListener(v -> onBackPressed());
    }

    private void deleteSelectedItem() {
        Realm realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        for (int i = 0; i < selectedItems.size(); i++) {
            realm.where(BookmarksModel.class).equalTo("url", selectedItems.get(i)).findAll().deleteAllFromRealm();
        }
        realm.commitTransaction();
        bookmarAdapter.notifyDataSetChanged();
        for (int j = 0; j < data.size(); j++) {
            if (selectedItems.contains(data.get(j).getUrl())) {
                data.remove(j);
            }
        }
        select = false;
        selectAll = false;
bookmarAdapter.notifyDataSetChanged();
        bookmarkTextView.setText("Bookmarks");
        checkBox.setVisibility(View.GONE);
        clearBookmark.setBackground(getDrawable(R.drawable.delete_drawable));

    }


    public void pop(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.bookmark_menu_item);
        popupMenu.show();
    }

    public void save(String title, String url, String logo) {
        selectedItem.setTitle(title);
        selectedItem.setUrl(url);
        selectedItem.setLogo(logo);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addtoHomeBookmark:
                addtoHomeBookmark();
                break;
            case R.id.deleteBookmarkItem:
                Log.d("tomar", "onMenuItemClick: ");
                deleteBookmarkItem();
                break;
            default:
                break;
        }
        return false;
    }

    private void deleteBookmarkItem() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(BookmarksModel.class).equalTo("url", selectedItem.getUrl()).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getUrl() == selectedItem.getUrl()) {
                data.remove(i);
                break;
            }
        }

        bookmarAdapter.notifyDataSetChanged();
    }

    private void addtoHomeBookmark() {
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

    public void backBookmarkBtn(View view) {

    }

    public void openThisURL(String url) {
        ((MainActivity) GlobalVariables.getmActivity()).searQuery(url);
        finish();
    }

    public static void addThisItem(String url) {
        if (!selectedItems.contains(url)) {
            selectedItems.add(url);
            bookmarkTextView.setText(selectedItems.size() + " Selected");
        }
    }

    public static void removeThis(String url) {
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.get(i).equals(url)) {
                selectedItems.remove(i);
                break;
            }
        }
        bookmarkTextView.setText(selectedItems.size() + " Selected");
    }

    @Override
    public void onBackPressed() {
        if (checkBox.getVisibility() == View.VISIBLE) {
            checkBox.setVisibility(View.GONE);
            selectAll = false;
            select = false;
            selectedItems.clear();
            bookmarkTextView.setText("Bookmarks");
            bookmarAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }
}