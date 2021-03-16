package com.example.browser.functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.browser.activities.Bookmarks;
import com.example.browser.activities.History;
import com.example.browser.activities.MainActivity;
import com.example.browser.R;
import com.example.browser.fragment.SearchToolbarFrag;
import com.example.browser.model.BookmarksModel;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.MyApplication;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

import static com.example.browser.utils.GlobalVariables.getmActivity;

public class PopupWindowCustom extends android.widget.PopupWindow {

    public void showSortPopupMainMenu(final Activity context, Point p) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.main_menu_popup, null);

        android.widget.PopupWindow changeSortPopUp = new android.widget.PopupWindow(context);
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
//        changeSortPopUp.setEnterTransition(new Slide(Gravity.TOP));

        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        ImageView close = layout.findViewById(R.id.backBtn);

        View backBtn = layout.findViewById(R.id.backBtn);
        View forwardBtn = layout.findViewById(R.id.farwardBtn);
        ImageView bookmarkBtn = layout.findViewById(R.id.bookmarkBtn);

        View shareBtn = layout.findViewById(R.id.shareBtnMainMenu);
        View exitBtn = layout.findViewById(R.id.exitBtn);
        LinearLayout incognotiLayout = layout.findViewById(R.id.incognitoTogglelayout);
        LinearLayout desktopViewLayout = layout.findViewById(R.id.desktopViewLayout);

        androidx.appcompat.widget.SwitchCompat incognitoToggle;
        androidx.appcompat.widget.SwitchCompat desktopViewToggle;
        androidx.appcompat.widget.SwitchCompat darkThemeToggle;

        incognitoToggle = layout.findViewById(R.id.incognitoToggle);
        desktopViewToggle = layout.findViewById(R.id.desktopViewToggle);
        darkThemeToggle = layout.findViewById(R.id.darkModeToggle);

        LinearLayout linearLayout = layout.findViewById(R.id.topItemsMainMenu);
        LinearLayout linearLayout1 = layout.findViewById(R.id.downItemsMainMenu);

        linearLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_menu_top_anim));
        linearLayout1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_menu_bottom_anim));
        incognitoToggle.setChecked(GlobalVariables.isIncognitoMode());

        desktopViewToggle.setChecked(GlobalVariables.isDesktopViewMode());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            darkThemeToggle.setChecked(GlobalVariables.isDarkModeWebview());
            layout.findViewById(R.id.darkModeTogglelayout).setOnClickListener(v -> {
                darkThemeToggle.setChecked(!darkThemeToggle.isChecked());
            });
            darkThemeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                HashMap<Integer, WebView> temp = MainActivity.webViewArrayList;
                GlobalVariables.setDarkModeWebview(isChecked);
                for (Map.Entry<Integer, WebView> set :
                        temp.entrySet()){
                        set.getValue().setForceDarkAllowed(isChecked);
                        set.getValue().getSettings().setForceDark(isChecked?WebSettings.FORCE_DARK_ON:WebSettings.FORCE_DARK_OFF);
                }
            });
        }else{
            layout.findViewById(R.id.darkModeTogglelayout).setVisibility(View.GONE);
        }





        desktopViewToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GlobalVariables.setDesktopViewMode(isChecked);
            extraFunctions.desktopViewChange(isChecked);
        });

        desktopViewLayout.setOnClickListener(v -> {
            desktopViewToggle.setChecked(!desktopViewToggle.isChecked());
        });


        incognitoToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GlobalVariables.setIncognitoMode(isChecked);
        });

        incognotiLayout.setOnClickListener(v -> {
            incognitoToggle.setChecked(!incognitoToggle.isChecked());
        });
        boolean flag = false;
        final boolean[] bookmarkState = {checkBookmark()};


        if (bookmarkState[0]) {
            bookmarkBtn.setImageResource(R.drawable.bookmark_selected_drawable);
        }


        if (MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getVisibility() == View.VISIBLE) {
            flag = true;
        }

        boolean[] flagBackAndFor = {false, false};

        if (MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).canGoBack()) {
            flagBackAndFor[0] = true;
        } else {
            Glide.with(context).load(R.drawable.back_drawable_unable).into((ImageView) backBtn);
        }
        if (MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).canGoForward()) {
            forwardBtn.setRotation(180f);
            flagBackAndFor[1] = true;
        } else {
            Glide.with(context).load(R.drawable.forward_drawable_unable).into((ImageView) forwardBtn);
        }

        boolean finalFlag3 = flag;
        backBtn.setOnClickListener(v -> {
            if (finalFlag3) {
                if (flagBackAndFor[0])
                    MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).goBack();
            }
        });

        boolean finalFlag4 = flag;
        forwardBtn.setOnClickListener(v -> {
            if (finalFlag4) {
                if (flagBackAndFor[1]) {
                    MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).goForward();
            //        changeSortPopUp.dismiss();
                }
            }
        });


        boolean finalFlag5 = flag;
        layout.findViewById(R.id.searInPageBtn).setOnClickListener(v -> {
            if (finalFlag5)
                ((MainActivity)GlobalVariables.getmActivity()).searchInPage();
            changeSortPopUp.dismiss();
        });

        layout.findViewById(R.id.historyBtn).setOnClickListener(v -> {
            Intent intent = new Intent(context, History.class);
            getmActivity().startActivity(intent);
            changeSortPopUp.dismiss();
        });
        boolean finalFlag = flag;
        layout.findViewById(R.id.homeBtnMainMenu).setOnClickListener(v -> {
            if (finalFlag)
                ((MainActivity) getmActivity()).home();
            changeSortPopUp.dismiss();
        });
        boolean finalFlag1 = flag;
        bookmarkBtn.setOnClickListener(v -> {
            if (finalFlag1) {
                if (!bookmarkState[0]) {
                    if (extraFunctions.addBookmarkBtn()) {
                        bookmarkBtn.setImageResource(R.drawable.bookmark_selected_drawable);
                    }
                    bookmarkState[0] = !bookmarkState[0];
                } else {
                    extraFunctions.removeBookmark();
                    bookmarkBtn.setImageResource(R.drawable.bookmark_unselected_drawable);
                    bookmarkState[0] = !bookmarkState[0];
                }
            }
        });
        layout.findViewById(R.id.bookmarksBtnMainMenu).setOnClickListener(v -> {
            Intent intent = new Intent(context, Bookmarks.class);
            getmActivity().startActivity(intent);
            changeSortPopUp.dismiss();
        });

        boolean finalFlag2 = flag;
        layout.findViewById(R.id.copyUrlBtn).setOnClickListener(v -> {
            if (finalFlag2)
                ((MainActivity) getmActivity()).copyUrl();
            changeSortPopUp.dismiss();
        });

        shareBtn.setOnClickListener(v -> extraFunctions.shareFile(MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl()));
        exitBtn.setOnClickListener(v -> System.exit(1));
        close.setOnClickListener(v -> changeSortPopUp.dismiss());

    }


    private boolean checkBookmark() {
        boolean forReturn = false;
        try {
            Realm realm1 = Realm.getInstance(MyApplication.config2);
            realm1.beginTransaction();
            if (!(realm1.where(BookmarksModel.class).equalTo("url", MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl()).findFirst() == null)) {
                forReturn = true;
            }
            realm1.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forReturn;
    }


    public void showSortSortingMenu(final Activity context, Point p) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.main_menu_popup, null);

        android.widget.PopupWindow changeSortPopUp = new android.widget.PopupWindow(context);
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
//        changeSortPopUp.setEnterTransition(new Slide(Gravity.TOP));

        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        ImageView close = layout.findViewById(R.id.backBtn);


    }



}

