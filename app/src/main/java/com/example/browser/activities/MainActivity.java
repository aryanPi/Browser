package com.example.browser.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.math.MathUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.Constants;
import com.example.browser.Interface.WebAppInterface;

import com.example.browser.R;
import com.example.browser.adapter.DownloadsBottomSheetAdapter;
import com.example.browser.adapter.NewsAdapter;
import com.example.browser.databinding.ActivityMainBinding;
import com.example.browser.databinding.DownloadRowBinding;
import com.example.browser.databinding.MainContentBinding;
import com.example.browser.fragment.SearchInPage;
import com.example.browser.fragment.SearchToolbarFrag;
import com.example.browser.fragment.SettingFragment;
import com.example.browser.fragment.downloading_frag;
import com.example.browser.fragment.newTabList;
import com.example.browser.functions.AdVideo;
import com.example.browser.functions.ChromeClient;

import com.example.browser.functions.DownloadStatusTask;
import com.example.browser.functions.PopupWindowCustom;
import com.example.browser.functions.WebView_client;
import com.example.browser.functions.extraFunctions;

import com.example.browser.model.DownloadModel;
import com.example.browser.model.DownloadsBottomSheetModel;
import com.example.browser.model.HistoryModel;
import com.example.browser.model.HomePageBookmark;
import com.example.browser.model.ModelImage;
import com.example.browser.model.NewsModel;
import com.example.browser.model.TabModel;
import com.example.browser.model.WebPageDataTempModel;
import com.example.browser.model.extra;
import com.example.browser.threads.downloadService;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.MyApplication;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.reward.AdMetadataListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.monstertechno.adblocker.AdBlockerWebView;
import com.monstertechno.adblocker.util.UrlUtils;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private UnifiedNativeAd nativeAd;

    public static HashMap<Integer, WebView> webViewArrayList = new HashMap<>();


    ArrayList<Integer> bookmarkList = new ArrayList<>();


    public Realm realm;
    Integer nextTab = 0;
    AdView adView;
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 1002;

    ArrayList<Thread> threads;

    @SuppressLint("StaticFieldLeak")

    int height = 0;
    int changeinHeight = 0;


    public ProgressBar progressBar;

    public TextView searchView_toolBar;

    public static LottieAnimationView dragView;
    public static LottieAnimationView active_download_bubble;

    Fragment SearchToolbarFragLocal = new SearchToolbarFrag(MainActivity.this);

    String TAG = "aryan";

    public FrameLayout wraper;

    private Fragment settingFragment;
    Context mContext = this;

    private Integer selectedPopupHomeitemID;
    private Integer selectedPopupHomeitemIndex;


    public RelativeLayout homeFragment;

    @SuppressLint("StaticFieldLeak")
    public static ImageView logoOfSiteInToolbar;

    RelativeLayout cardView;
    RelativeLayout home_body;
    RelativeLayout searchView_home;
    ImageView logo;

    TextView TabNo;
    BottomSheetDialog bottomSheetDialog;
    public ImageView searchBtn_mainSearchBar;
    ImageView speak_image_search_home;

    RecyclerView news;

    ImageView historyOnSearchBar;

    BottomNavigationView bottomNavigationView;

    RelativeLayout dragable;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Browser);
        setContentView(R.layout.activity_main);


/*        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/


        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "External Error !", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            try {
                Thread.sleep(4000); // Let the Toast display before app will get shutdown
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });

        MobileAds.initialize(this, getString(R.string.ADMOB_ADS_UNIT_ID));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.VIBRATE}, 100);
        }

        Intent intent = getIntent();


        if (intent != null) {
            if (intent.getExtras() != null)
                if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getExtras().getString("default") != null) {
                    if (intent.getExtras().getString("default").equals("true")) {
                        Realm.getDefaultInstance().executeTransaction((Realm realm) -> {
                            if (realm.where(extra.class).findFirst() != null)
                                realm.where(extra.class).findFirst().setDefaultBrowser(true);
                        });
                    }
                }
        }


        InitVariables();

        InitListener();

        InitDisplaySettings();


        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();

            if (Intent.ACTION_VIEW.equals(action)) {
                if (intent.getExtras() == null || intent.getExtras().getString("default") == null) {
                    searQuery(intent.getData().toString());
                } else {
                    bottomNavigationView.setSelectedItemId(R.id.settings);
                }
            }

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (type.startsWith("text/")) {
                    searQuery(intent.getStringExtra(Intent.EXTRA_TEXT));
                }

            }
        }
    }


    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void askPermission(String msg) {
        if (!hasPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        }).create().show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                }
            }


        }
    }

    //Initialization --------------------

    private void InitVariables() {
        realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        extra e = realm.where(extra.class).findFirst();
        if (e != null)
            GlobalVariables.setExtraData(realm.copyFromRealm(e));
        else {
            realm.copyToRealm(new extra(Environment.getExternalStorageDirectory().getAbsolutePath() + constants.SAVE_FOLDER_NAME, 0, false, true, true, false, false));
            GlobalVariables.setExtraData(realm.copyFromRealm(realm.where(extra.class).findFirst()));
        }
        realm.commitTransaction();

        GlobalVariables.setHistoryStack(GlobalVariables.getExtraData().getHistoryStack());


        GlobalVariables.setmActivity(this);
        GlobalVariables.setResources(getResources());

        bottomNavigationView = findViewById(R.id.nav_bottom);
        wraper = findViewById(R.id.wraper);
        homeFragment = findViewById(R.id.homeFragment);


        //Glide.with(this).load(R.drawable.background25).centerCrop().into((ImageView) findViewById(R.id.bg));
        settingFragment = new SettingFragment();
//        downloadFragment = new downloadFrag();

        bottomNavigationView.setSelectedItemId(R.id.home);
        historyOnSearchBar = findViewById(R.id.historyOnSearchBar);

        //HOME FRAGMENT

        adView = new AdView(getApplicationContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        searchView_home = findViewById(R.id.search_bar);

        searchBtn_mainSearchBar = findViewById(R.id.searchBtn_mainSearchBar);
        speak_image_search_home = findViewById(R.id.speak_image_search_home);
//        search_home_text = findViewById(R.id.tep);

        dragable = findViewById(R.id.dragable);

        logo = findViewById(R.id.logo);
        cardView = findViewById(R.id.toolbar_head);

        height = cardView.getLayoutParams().height;
        changeinHeight = cardView.getLayoutParams().height / 14;

        searchView_toolBar = findViewById(R.id.toolBar_search_bar);
        home_body = findViewById(R.id.home_body);
        dragView = findViewById(R.id.downloadBtn);
        logoOfSiteInToolbar = findViewById(R.id.logoOfSiteInToolbar);

        dragView.setMinAndMaxProgress(0.27f, 0.9f);
        active_download_bubble = findViewById(R.id.active_download_bubble);

        progressBar = findViewById(R.id.progressBar);
        TabNo = findViewById(R.id.new_tab_no);


        bookmarkList.add(R.id.bookmark2);
        bookmarkList.add(R.id.bookmark3);
        bookmarkList.add(R.id.bookmark4);
        bookmarkList.add(R.id.bookmark5);
        bookmarkList.add(R.id.bookmark6);


        Glide.with(this).load(R.drawable.faceboo_image).centerCrop().circleCrop().into((ImageView) findViewById(R.id.fbImgHomePage));
        Glide.with(this).load(R.drawable.insta).centerCrop().circleCrop().into((ImageView) findViewById(R.id.instaImageHomePage));
        Glide.with(this).load(R.drawable.whatsapp).centerCrop().circleCrop().into((ImageView) findViewById(R.id.whatsappImageHomePage));
        Glide.with(this).load(R.drawable.vimeo_logo).centerCrop().circleCrop().into((ImageView) findViewById(R.id.vimeoImageHomePage));
        Glide.with(this).load(R.drawable.tubidy).centerCrop().circleCrop().into((ImageView) findViewById(R.id.tubidyImageHomePage));


        realm = Realm.getDefaultInstance();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<DownloadModel> downloadModels = realm.where(DownloadModel.class).equalTo("downloadingWith", false).notEqualTo("status", Constants.DOWNLOAD_STATUS.COMPLETE).findAll();
        for (int i = 0; i < downloadModels.size(); i++) {
            DownloadStatusTask downloadStatusTask = new DownloadStatusTask(downloadModels.get(i));
            downloadStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(downloadModels.get(i).getDownloadManagerId()));
        }
        realm.commitTransaction();

/*        List<DownloadModel> downloadModelsLocal = getAllDownloads();
        if (downloadModelsLocal != null) {
            if (downloadModelsLocal.size() > 0) {
                downloadModels.addAll(downloadModelsLocal);
                for (int i = 0; i < downloadModels.size(); i++) {
                    if (downloadModels.get(i).getStatus().equals("Running") && (!downloadModels.get(i).isDownloadingWith())) {
                        DownloadStatusTask downloadStatusTask = new DownloadStatusTask(downloadModels.get(i), this);
                        runTask(downloadStatusTask, "" + downloadModels.get(i).getDownloadId());

                    }
                }
            }
        }*/

        //data_list = findViewById(R.id.data_list);

/*        downloadAdapter = new DownloadAdapter(getApplicationContext(), downloadModels, this, this);
        data_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        data_list.setAdapter(downloadAdapter);*/

        List<HistoryModel> temp;
        realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        temp = realm.copyFromRealm(realm.where(HistoryModel.class).equalTo("stateOfPage", true).findAll());
        realm.commitTransaction();
        GlobalVariables.setStateOfRestoreTabs(temp.size());

        for (int i = 0; i < temp.size(); i++) {
            newTabOpen();
            if (temp.get(i).getUrl().equals("about:blank")) {
                GlobalVariables.setStateOfRestoreTabs(GlobalVariables.isStateOfRestoreTabs() - 1);
            } else {
                searQuery(temp.get(i).getUrl());
            }
        }

        if (temp.size() == 0) {
            newTabOpen();
        }
        updateHomePageIcons();

    }

    private void fetchNewsData() {
        String url = "http://newsapi.org/v2/top-headlines?country=in&apiKey=aa2e8da8152645169bd0f306495aa715";
        RequestQueue b = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {

                    try {
                        JSONArray jsonArray = response.getJSONArray("articles");
                        ArrayList<NewsModel> arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NewsModel newsModel = new NewsModel(
                                    jsonObject.getString("url"),
                                    jsonObject.getString("title"),
                                    jsonObject.getString("urlToImage"),
                                    jsonObject.getString("author"));
                            Log.d(TAG, "onResponse: " + jsonObject.getString("author"));
                            arrayList.add(newsModel);
                        }
                        Log.d(TAG, "onResponse: ");
                        //  news.setAdapter(new NewsAdapter(getApplicationContext(), arrayList));

                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: error " + e.getMessage());
                        e.printStackTrace();
                    }

                }, error -> Log.d(TAG, "fetchNewsData: Error"));

        b.add(jsonObjectRequest);

        //        singaltonCls.Companion.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        SingalentonClass.Companion.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }


    @SuppressLint("ClickableViewAccessibility")
    private void InitListener() {


        MobileAds.initialize(this, initializationStatus -> {
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case (R.id.home): {
                    getSupportFragmentManager().beginTransaction().remove(settingFragment).commit();
                    cardView.setVisibility(View.VISIBLE);
                    dragable.setVisibility(View.GONE);
                    wraper.setVisibility(View.GONE);
                    homeFragment.setVisibility(View.VISIBLE);
                    bottomNavigationView.setSelected(true);
                    break;
                }
                case (R.id.downloads): {
                    startActivity(new Intent(MainActivity.this, Downloads.class));
                    bottomNavigationView.setSelected(false);
                    break;
                }
                case (R.id.settings): {
                    cardView.setVisibility(View.GONE);
                    dragable.setVisibility(View.GONE);
                    setFragment(settingFragment);
                    bottomNavigationView.setSelected(true);
                    break;
                }

                default: {
                    return false;
                }
            }
            return bottomNavigationView.isSelected();
        });


        findViewById(R.id.tep).setOnClickListener(v -> searchBtn_mainSearchBar.callOnClick());

        searchBtn_mainSearchBar.setOnClickListener(v -> setFragment(SearchToolbarFragLocal));

        searchView_toolBar.setOnClickListener(v -> {
            Toast.makeText(mContext, "Call clicked !", Toast.LENGTH_SHORT).show();
            GlobalVariables.setSearchQToolbar(webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl());
            searchBtn_mainSearchBar.callOnClick();
        });

        findViewById(R.id.layout_sear_toolbar).setOnClickListener(v -> searchView_toolBar.callOnClick());

        findViewById(R.id.speak_image_search_home).setOnClickListener(v -> {
            GlobalVariables.setSearchCallFromHome(true);
            setFragment(SearchToolbarFragLocal);
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDragViewOnTouchListenere() {
        dragable.setOnTouchListener(null);
        dragable.setOnTouchListener(new View.OnTouchListener() {
            int dX;
            int dY;
            int eventX;
            int eventY;
            int lastAction;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dX = (int) (v.getX() - event.getRawX());
                    dY = (int) (v.getY() - event.getRawY());
                    eventX = (int) event.getRawX();
                    eventY = (int) event.getRawY();
                    lastAction = MotionEvent.ACTION_DOWN;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.setY(event.getRawY() + dY);
                    v.setX(event.getRawX() + dX);
                    lastAction = MotionEvent.ACTION_MOVE;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (abs(eventX - (int) event.getRawX()) < 10) {
                        downloadBtn();
                    }
                    if (v.getX() > (GlobalVariables.getBtnXEnd() - GlobalVariables.getBtnXSt()) / 2) {
                        v.setX(GlobalVariables.getBtnXEnd());
                    } else {
                        v.setX(GlobalVariables.getBtnXSt());
                    }
                    if (v.getY() > GlobalVariables.getBtnYTop()) {
                        v.setY(GlobalVariables.getBtnYTop());

                    } else if (v.getY() < GlobalVariables.getBtnYSt()) {
                        v.setY(GlobalVariables.getBtnYSt());
                    }
                }
                return true;
            }
        });
    }


    private void InitDisplaySettings() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int w = displayMetrics.widthPixels, h = displayMetrics.heightPixels;

        Toast.makeText(this, "repeast" + w + " : " + h, Toast.LENGTH_SHORT).show();
        GlobalVariables.setDisplayMetrics(displayMetrics);

        GlobalVariables.setBtnXSt(-w / 14);
        GlobalVariables.setBtnXEnd(w - (int) (w * 0.27));
        GlobalVariables.setBtnYTop(h - 400);


        Display getOrient = this.getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() == getOrient.getHeight()) {
            w = displayMetrics.widthPixels;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                w = displayMetrics.widthPixels;
            } else {
                w = displayMetrics.heightPixels;
            }
        }


        GlobalVariables.setBtnYSt(200);
        findViewById(R.id.dragable).setLayoutParams(new RelativeLayout.LayoutParams((w / 3), (w / 3)));
        findViewById(R.id.active_download_bubble).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dragView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout.LayoutParams temp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        temp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        temp.setMargins(0, w / 30, 0, 0);
        findViewById(R.id.downloadImageInside).setLayoutParams(temp);
        final int padding = w / 12;
        findViewById(R.id.downloadImageInside).setPadding(padding, padding, padding, padding);
        setDragViewOnTouchListenere();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    private void WebViewSettings(WebView temp) {


        temp.getSettings().setPluginState(WebSettings.PluginState.ON);
        temp.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        temp.getSettings().setSupportMultipleWindows(true);
        temp.getSettings().setSupportZoom(true);
        temp.getSettings().setBuiltInZoomControls(true);
        temp.getSettings().setAllowFileAccess(true);
        temp.getSettings().setDisplayZoomControls(true);
        temp.getSettings().setDomStorageEnabled(true);
        temp.getSettings().setUseWideViewPort(true);
        temp.getSettings().setLoadWithOverviewMode(true);

        registerForContextMenu(temp);

/*
        temp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult hitTestResult = temp.getHitTestResult();
                String DownloadImageUrl = hitTestResult.getExtra();
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE || hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    if (URLUtil.isNetworkUrl(DownloadImageUrl)) {

                        */
/*       menu.setHeaderTitle("Download Image from Below");
                        menu.add(0, 1, 0, "Download Image")
                                .setOnMenuItemClickListener(item -> {
                                    downloadImage(DownloadImageUrl);
                                    Log.d(TAG, "onCreateContextMenu: " + hitTestResult.getType());
*//*
         */
/*                            downloadprocessInit(DownloadImageUrl,0, URLUtil.guessFileName(DownloadImageUrl,null,"image/*"),"",null,false);
    downloadProcess();*//*
         */
        /*
         *//*
         */
/*
                            /*
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(DownloadImageUrl));
                            startActivity(i);
                            *//*
         */
/*

                                    return true;
                                });
                    }*//*

                    } else if (hitTestResult.getType() == WebView.HitTestResult.ANCHOR_TYPE
                            || hitTestResult.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
                        // Menu options for a hyperlink.
cop                        Log.d(TAG, "onCreateContextMenu: " + hitTestResult.getExtra());
            */
/*            menu.setHeaderTitle(hitTestResult.getExtra());
            menu.add("Save Link")
                    .setOnMenuItemClickListener(handler);
            menu.add("Share Link")
                    .setOnMenuItemClickListener(handler);*//*

                    }
                }
                return true;
            }
        });
*/

        temp.setWebViewClient(new WebView_client());

        temp.getSettings().setJavaScriptEnabled(true);

        temp.addJavascriptInterface(new WebAppInterface(getApplicationContext(), this), "Android");

        temp.setWebChromeClient(new ChromeClient());

        temp.getSettings().setAllowFileAccessFromFileURLs(true);
        temp.getSettings().setAllowUniversalAccessFromFileURLs(true);
        temp.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        if (GlobalVariables.isDarkModeWebview()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                temp.setForceDarkAllowed(true);
                temp.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
            }
        }


        temp.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if (contentLength == 0) {
                Toast.makeText(getApplicationContext(), "Content Length Is Not Defined !", Toast.LENGTH_SHORT).show();
            } else {
                downloadProcess(url, contentDisposition, mimetype, contentLength);
            }
        });


        new AdBlockerWebView.init(this).initializeWebView(temp);

        temp.setOnTouchListener(new View.OnTouchListener() {
            final RelativeLayout.LayoutParams cardViewlayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int margin;
            int[] times = {0};

            float yst = 0f;
            float ystTop = 0f;
            float ystBottom = 0f;

            final int CHANGE = 13;


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!((event.getY() > ystTop) && (event.getY() < ystBottom))) {
                        margin = ((RelativeLayout.LayoutParams) cardView.getLayoutParams()).topMargin;
                        if (yst < event.getY()) {
                            if (margin <= -7)
                                if (times[0] < 20) {
                                    cardViewlayoutParams.setMargins(0, margin + changeinHeight, 0, 0);
                                    cardView.setLayoutParams(cardViewlayoutParams);
                                    times[0]++;
                                }
                        } else if (yst > event.getY()) {
                            if ((height - 10) > (margin * -1)) {
                                if (times[0] > -20) {
                                    cardViewlayoutParams.setMargins(0, margin - changeinHeight, 0, 0);
                                    cardView.setLayoutParams(cardViewlayoutParams);
                                    times[0]--;
                                }
                            }
                        }

                        yst = event.getY();
                        ystTop = yst - CHANGE;
                        ystBottom = yst + CHANGE;

                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    yst = event.getY();
                    ystTop = yst - CHANGE;
                    ystBottom = yst + CHANGE;
                }

                temp.onTouchEvent(event);
                return true;
            }
        });


    }

    // Functions ---------------------

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        wraper.setVisibility(View.VISIBLE);
        fragmentTransaction.replace(R.id.wraper, fragment);

        fragmentTransaction.commit();
        if (fragment == settingFragment || home_body.getVisibility() == View.VISIBLE)
            homeFragment.setVisibility(View.GONE);

    }


    private int getNewWebView() {
        searchView_toolBar.setText("");

        RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        webViewParams.addRule(RelativeLayout.BELOW, cardView.getId());
        WebView newWebView = new WebView(this);

        newWebView.setLayoutParams(webViewParams);
        newWebView.setVisibility(View.GONE);
        ++nextTab;
        newWebView.setId(nextTab);
        webViewArrayList.put(nextTab, newWebView);


        return nextTab;
    }

    public void newTabOpen() {
        progressBar.setVisibility(View.GONE);

        Glide.with(this).load(R.drawable.web_drawable).into(logoOfSiteInToolbar);

        if (webViewArrayList.get(GlobalVariables.getActiveWebView()) != null)
            webViewArrayList.get(GlobalVariables.getActiveWebView()).onPause();

        int newWebViewID = getNewWebView();
        homeFragment.setVisibility(View.VISIBLE);

        homeFragment.addView(webViewArrayList.get(newWebViewID));

        WebViewSettings(webViewArrayList.get(newWebViewID));

        if (GlobalVariables.getActiveWebView() != 0)
            webViewArrayList.get(GlobalVariables.getActiveWebView()).setVisibility(View.GONE);

        home_body.setVisibility(View.VISIBLE);
        dragable.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        logo.setVisibility(View.VISIBLE);
        findViewById(R.id.layout_sear_toolbar).setVisibility(View.GONE);
        GlobalVariables.setActiveHomeFragment(newWebViewID);
        extraFunctions.resetDownloadBtn();

        GlobalVariables.addNewTabModel(new TabModel(newWebViewID, "HOME PAGE", ""));

        TabNo.setText((Integer.toString(webViewArrayList.size())));
        GlobalVariables.setWebPageDatas(newWebViewID, new WebPageDataTempModel(newWebViewID, "", "HOME PAGE", "", false, new ArrayList<>()));

    }

    public void openThisTab(int id) {
        int activePage = GlobalVariables.getActiveWebView();
        if (id != activePage) {
            if (!GlobalVariables.getWebPageDatas(id).getFrvicon().equals("")) {
                Glide.with(this).load(extraFunctions.convert(GlobalVariables.getWebPageDatas(id).getFrvicon())).circleCrop().into(logoOfSiteInToolbar);
                //logoOfSiteInToolbar.setVisibility(View.VISIBLE);
            } else {
                Glide.with(this).load(R.drawable.web_drawable).into(logoOfSiteInToolbar);
//                logoOfSiteInToolbar.setVisibility(View.GONE);
            }

            //searchView_toolBar.setText(GlobalVariables.getWebPageDatas(webViewArrayList.get(GlobalVariables.getActiveWebView()).getId()).getTitle());

            if (webViewArrayList.get(id) != null) {
                if (webViewArrayList.get(activePage).getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                webViewArrayList.get(activePage).onPause();
                if (webViewArrayList.get(id).getUrl() == null || GlobalVariables.getThisIdTableModel(id).getTitle().equals("HOME PAGE")) {
                    home_body.setVisibility(View.VISIBLE);
                    dragable.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    findViewById(R.id.layout_sear_toolbar).setVisibility(View.GONE);
                    logo.setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.layout_sear_toolbar).setVisibility(View.VISIBLE);
                    webViewArrayList.get(id).onResume();
                    webViewArrayList.get(id).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_sear_toolbar).setVisibility(View.VISIBLE);
                    searchView_toolBar.setText(webViewArrayList.get(id).getTitle());
                }
            }
            webViewArrayList.get(activePage).setVisibility(View.GONE);
            GlobalVariables.setActiveHomeFragment(id);
            extraFunctions.resetDownloadBtn();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void closeTab(int id) {

        if (webViewArrayList.size() == 1) {
            if (webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl() == null) return;
            else if (!webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl().equals("")) {
                newTabOpen();
                extraFunctions.resetDownloadBtn();
            } else return;
            webViewArrayList.get(GlobalVariables.getActiveWebView()).setVisibility(View.GONE);
            home_body.setVisibility(View.VISIBLE);
            dragable.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            logo.setVisibility(View.VISIBLE);
            findViewById(R.id.layout_sear_toolbar).setVisibility(View.GONE);

        } else if (id == GlobalVariables.getActiveWebView()) {
            ArrayList<TabModel> temp = GlobalVariables.getTabModels();
            int ids = 0;
            if (id == temp.get(0).getId()) ids = temp.get(1).getId();
            else if (id == temp.get(temp.size() - 1).getId())
                ids = temp.get(temp.size() - 2).getId();
            else {
                boolean state = false;
                for (TabModel t : temp) {
                    if (state) {
                        ids = t.getId();
                        break;
                    }
                    if (t.getId() == id) state = true;
                }
            }
            GlobalVariables.setActiveHomeFragment(ids);
            if (webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl() == null) {
                home_body.setVisibility(View.VISIBLE);
                dragable.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.layout_sear_toolbar).setVisibility(View.GONE);
                searchView_toolBar.setText("");
                logo.setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.layout_sear_toolbar).setVisibility(View.VISIBLE);
                searchView_toolBar.setText(webViewArrayList.get(GlobalVariables.getActiveWebView()).getTitle());
                webViewArrayList.get(GlobalVariables.getActiveWebView()).setVisibility(View.VISIBLE);
                if (!GlobalVariables.getWebPageDatas(GlobalVariables.getActiveWebView()).getFrvicon().equals(""))
                    Glide.with(this).load(extraFunctions.convert(GlobalVariables.getWebPageDatas(GlobalVariables.getActiveWebView()).getFrvicon())).circleCrop().into(logoOfSiteInToolbar);
            }
        }

        if (webViewArrayList.get(id) != null) {
            Realm.getDefaultInstance().executeTransaction(realm -> {
                if (realm.where(HistoryModel.class).equalTo("webviewID", id).findFirst() != null) {
                    RealmResults<HistoryModel> results = realm.where(HistoryModel.class).equalTo("webviewID", id).findAll();
                    for (HistoryModel h : results) h.setStateOfPage(false);
                }
            });
        }


        webViewArrayList.get(GlobalVariables.getActiveWebView()).onResume();
        homeFragment.removeView(webViewArrayList.get(id));
        GlobalVariables.removeWebPageDatas(id);
        GlobalVariables.removeTabModel(id);
        webViewArrayList.remove(id);
        TabNo.setText((Integer.toString(webViewArrayList.size())));

    }


    @SuppressLint("SetTextI18n")
    private void downloadBtn() {

        if (dragView.getSpeed() == -1) {
            showNoDownloadsALert(null);
            return;
        }

        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.download_bottomsheet_layout, findViewById(R.id.bottomSheet));

        final DownloadsBottomSheetAdapter[] adapter = new DownloadsBottomSheetAdapter[1];
        ArrayList<String> links = GlobalVariables.getWebPageDatas(GlobalVariables.getActiveWebView()).getDownloadLinks();
        ListView downloadsItemList = sheetView.findViewById(R.id.allDownloadsBottomSheet);
        ArrayList<DownloadsBottomSheetModel> list = new ArrayList<>();
        ArrayList<String> arrayListUrls = new ArrayList<>();
        ArrayList<String> qualities = new ArrayList<>();
        ArrayList<String> sizes = new ArrayList<>();

        if (GlobalVariables.getFlag() == 3) {

            list.add(new DownloadsBottomSheetModel(null, null, null, null, null));
            adapter[0] = new DownloadsBottomSheetAdapter(getApplicationContext(), R.layout.download_item_bottom_sheet_view, list);
            downloadsItemList.setAdapter(adapter[0]);


            VimeoExtractor.getInstance().fetchVideoWithURL(webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl(), null, new OnVimeoExtractionListener() {
                @Override
                public void onSuccess(VimeoVideo video) {

                    Map<String, String> streams = video.getStreams();

                    list.get(0).setTitle(video.getTitle());
                    for (Map.Entry<String, String> set :
                            streams.entrySet()) {
                        arrayListUrls.add(set.getValue());
                        qualities.add(set.getKey());
                        sizes.add("00");
                    }

                    new Thread(() -> {
                        try {
                            URL url = new URL(video.getThumbs().get("base"));
                            HttpURLConnection connection;
                            connection = (HttpURLConnection) url.openConnection();
                            connection.connect();
                            InputStream inputStream = connection.getInputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                            runOnUiThread(() -> {
                                list.get(0).setImage(bitmap);
                                adapter[0].notifyDataSetChanged();
                            });
                        } catch (Exception ignored) {
                        }
                        for (int i = 0; i < arrayListUrls.size(); i++) {
                            try {
                                HttpURLConnection connection = ((HttpURLConnection) (new URL(arrayListUrls.get(i))).openConnection());
                                connection.connect();
                                int len = connection.getContentLength();
                                sizes.set(i, len + "");
                            } catch (Exception e) {
                                Toast.makeText(mContext, "Error while getting length of File !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        runOnUiThread(() -> {
                            list.get(0).setSize(sizes);
                            adapter[0].notifyDataSetChanged();
                        });
                    }).start();
                    runOnUiThread(() -> {
                        list.set(0, new DownloadsBottomSheetModel(null, video.getTitle(), arrayListUrls, sizes, qualities));
                        adapter[0].notifyDataSetChanged();
                    });

                }

                @Override
                public void onFailure(Throwable throwable) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Unable To Fetch Video !", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        }
                    });
                }
            });


        } else {

            for (int i = 0; i < links.size(); i++)
                list.add(new DownloadsBottomSheetModel(null, null, null, null, null));

            adapter[0] = new DownloadsBottomSheetAdapter(this, R.layout.download_item_bottom_sheet_view, list);

            threads = new ArrayList<>();
            int len = Math.min(links.size(), 3);

            for (int i = 0; i < len; i++) {
                int finalI = i;

                Thread thread = new Thread(() -> {
                    try {
                        URL url1 = new URL(links.get(finalI));
                        HttpURLConnection httpConn = (HttpURLConnection) url1.openConnection();
                        int responseCode = httpConn.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            String fileName = "";
                            String disposition = httpConn.getHeaderField("Content-Disposition");
                            //String contentType = httpConn.getContentType();
                            int contentLengthLocal = httpConn.getContentLength();
                            if (disposition != null) {
                                // extracts file name from header field
                                int index = disposition.indexOf("filename=");
                                if (index > 0) {
                                    fileName = disposition.substring(index + 10,
                                            disposition.length() - 1);
                                }
                            } else {
                                fileName = URLUtil.guessFileName(links.get(finalI), null, "video/mp4");
                            }

                            Bitmap image = null;
                            arrayListUrls.add(links.get(finalI));
                            sizes.add(contentLengthLocal + "");

                            String finalFileName = fileName;
                            runOnUiThread(() -> {
                                list.set(finalI, new DownloadsBottomSheetModel(image, finalFileName, arrayListUrls, sizes, qualities));
                                adapter[0].notifyDataSetChanged();
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                Thread thread1 = new Thread(() -> {
                    Bitmap image;
                    try {
                        image = extraFunctions.retriveVideoFrameFromVideo(links.get(finalI));
                    } catch (Exception e) {
                        image = null;
                        e.printStackTrace();
                    }

                    Bitmap finalImage = image;
                    runOnUiThread(() -> {
                        list.get(finalI).setImage(finalImage);
                        adapter[0].notifyDataSetChanged();
                    });
                });
                threads.add(thread1);
                threads.add(thread);
                thread.start();
                thread1.start();
            }


            downloadsItemList.setAdapter(adapter[0]);
            bottomSheetDialog.setOnDismissListener(this::onDismiss);
            TextView total = sheetView.findViewById(R.id.totalItemsForDownload);
            total.setText(links.size() + " Item(s)");
            ImageView wifi = sheetView.findViewById(R.id.wifi_RedirectDownload);
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            boolean isWiFi = nInfo.getType() == ConnectivityManager.TYPE_WIFI;
            if (!isWiFi) Glide.with(mContext).load(R.drawable.wifi_off_drawable).into(wifi);
        }

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();

    }


    private void showNoDownloadsALert(ArrayList<String> links) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.no_download_alert_dialoge_context);

        TextView textView = dialog.findViewById(R.id.reportToUsDownloadDialog);
        LottieAnimationView active = dialog.findViewById(R.id.activeAnimationNodownloadsAlertDialog);
        LottieAnimationView inactive = dialog.findViewById(R.id.inactiveAnimationNodownloadsAlertDialog);

        active.setMinAndMaxProgress(0.27f, 0.9f);
        inactive.setMinAndMaxProgress(0.27f, 0.9f);

        inactive.setProgress(0.27f);
        active.setSpeed(1f);

        active.playAnimation();


        dialog.findViewById(R.id.okayNoDownloadsDialog).setOnClickListener(v -> dialog.dismiss());

        textView.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void downloadProcess(String url, String contentDisposition, String mimetype, long contentLength) {
        String fileName;
        try {
            int index = contentDisposition.indexOf("filename=");
            if (index > 0)
                fileName = contentDisposition.substring(index + 10, contentDisposition.length() - 1);
            else fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
        } catch (Exception e) {
            fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.download_item_bottom_sheet_view);

        ImageView imageView = dialog.findViewById(R.id.downloaditemBottomsheetImage);
        TextView sizev = dialog.findViewById(R.id.sizeitemBottomsheetImage);
        TextView titlev = dialog.findViewById(R.id.titleitemBottomsheetImage);
        ImageView downloadBtn = dialog.findViewById(R.id.downloadbtnBottomSheetItem);

        String icon = "";
        if (mimetype.startsWith("application/pdf")) {
            icon = constants.PDF_IMAGE;
        } else if (mimetype.startsWith("image")) {
            icon = constants.IMG_IMAGE;
        } else if (mimetype.startsWith("application/zip") || fileName.endsWith(".zip")) {
            icon = constants.ZIP_IMAGE;
        } else if (mimetype.startsWith("audio/")) {
            icon = constants.MUSICAL_IMAGE;
        } else if (mimetype.startsWith("video/")) {
            //doNothing
        } else {
            icon = constants.FILE_IMAGE;
        }


        final String[] image = {""};

        if (!icon.equals("")) image[0] = icon;
        else {
            Thread thread = new Thread(() -> {
                Bitmap bitmap;
                try {
                    bitmap = extraFunctions.retriveVideoFrameFromVideo(url);
                } catch (Exception e) {
                    bitmap = null;
                    e.printStackTrace();
                }
                Bitmap finalBitmap = bitmap;
                runOnUiThread(() -> {
                    if (dialog.isShowing()) {
                        dialog.findViewById(R.id.lottieLoadingImageDownloadItem).setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        Glide.with(this).load(finalBitmap).centerCrop().into(imageView);
                    }
                });
                if (dialog.isShowing())
                    if (bitmap != null)
                        image[0] = extraFunctions.convert(bitmap);
            });
            dialog.setOnDismissListener(dialog1 -> {
                if (thread.isAlive()) {
                    thread.interrupt();
                }
            });
            thread.start();
        }

        if (!(image[0].equals(""))) {
            dialog.findViewById(R.id.lottieLoadingImageDownloadItem).setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setPadding(15, 15, 15, 15);
            switch (image[0]) {
                case constants.PDF_IMAGE:
                    Glide.with(this).load(R.drawable.ic_pdf_image).fitCenter().into(imageView);
                    break;
                case constants.IMG_IMAGE:
                    Glide.with(this).load(R.drawable.ic_image).fitCenter().into(imageView);
                    break;
                case constants.MUSICAL_IMAGE:
                    Glide.with(this).load(R.drawable.ic_musical).fitCenter().into(imageView);
                    break;
                case constants.ZIP_IMAGE:
                    Glide.with(this).load(R.drawable.ic_zip).fitCenter().into(imageView);
                    break;
                default:
                    Glide.with(this).load(R.drawable.ic_file).fitCenter().into(imageView);
                    break;
            }
        }

        dialog.findViewById(R.id.lottieLoadingTextDownloadItem).setVisibility(View.GONE);
        dialog.findViewById(R.id.textAndRenameLayout).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.renameDownloadItemBottomSheet).setOnClickListener(v -> rename(titlev));

        if (sizev.getVisibility() != View.VISIBLE) {
            sizev.setVisibility(View.VISIBLE);
            titlev.setVisibility(View.VISIBLE);
            downloadBtn.setVisibility(View.VISIBLE);
            sizev.setText(android.text.format.Formatter.formatFileSize(this, contentLength));
            titlev.setText(fileName);

            String name = titlev.getText().toString();

            String finalImage = image[0];
            downloadBtn.setOnClickListener(v -> {
                downloadprocessInit(url, contentLength, name, finalImage, mimetype, false);
                dialog.dismiss();
            });
        }

        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_popup_custom));
        dialog.show();

    }

    private void rename(@NotNull TextView tv) {
        Dialog dialog = new Dialog(this);
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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!testPermission()) {
                    Toast.makeText(this, "Permission was not granted by your device !!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
                GlobalVariables.setIsPermission(false);
            }
        }
    }

    private boolean testPermission() {
        try {
            File file = new File(GlobalVariables.getExtraData().getDownloadLocation() + "/test.temp");
            if (file.createNewFile())
                GlobalVariables.setIsPermission(file.delete());
            else GlobalVariables.setIsPermission(false);

        } catch (IOException e) {
            GlobalVariables.setIsPermission(false);
        }
        return GlobalVariables.isIsPermission();
    }

    public void downloadprocessInit(String url, long contentLength, String fileName, String ImageUri, @Nullable String mimeType, boolean isFromBtn) {
        if (isFromBtn) bottomSheetDialog.dismiss();
        askPermission("Storage permission is needed to Write Downloading data !");
        testPermission();
        String downloadPath = GlobalVariables.getExtraData().getDownloadLocation();
        final DownloadModel downloadModelPass = new DownloadModel();
        downloadModelPass.setDownloadingWith(true);
        realm.beginTransaction();
        Number currentnum = realm.where(DownloadModel.class).max("id");
        realm.commitTransaction();
        long nextId;
        if (currentnum == null) nextId = 1L;
        else nextId = currentnum.longValue() + 5L;
        if (mimeType != null)
            if ((mimeType.startsWith("video") || fileName.endsWith(".mp4")) && ImageUri.equals(""))
                new Thread(() -> {
                    Bitmap image;
                    try {
                        image = extraFunctions.retriveVideoFrameFromVideo(url);
                    } catch (Exception e) {
                        image = null;
                        e.printStackTrace();
                    }
                    Bitmap finalImage = image;
                    runOnUiThread(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                        if (realm.where(DownloadModel.class).equalTo("id", nextId).findFirst() != null)
                            realm.where(DownloadModel.class).equalTo("id", nextId).findFirst().setUri(extraFunctions.convert(finalImage));
                    }));
                }).start();
        downloadModelPass.setId(nextId);
        downloadModelPass.setStatus(Constants.DOWNLOAD_STATUS.RUNNING);
        downloadModelPass.setTitle(fileName);
        downloadModelPass.setFile_size(0L);
        downloadModelPass.setProgress(0);
        downloadModelPass.setFile_path(downloadPath);
        downloadModelPass.setUri(ImageUri);
        downloadModelPass.setUrl(url);
        downloadModelPass.setTotal_file_Size(contentLength);
        downloadModelPass.setMimeType(mimeType);
        realm.beginTransaction();
        realm.copyToRealm(downloadModelPass);
        realm.commitTransaction();
        if (GlobalVariables.isDownloadFragState())
            ((downloading_frag) downloading_frag.mFragment).addNewDownloadItem(downloadModelPass);
        startDownload(downloadModelPass, false);

    }


    public void startDownload(DownloadModel downloadModel, boolean resume) {
        Toast.makeText(this, "HE", Toast.LENGTH_SHORT).show();
        downloadService.downloadModel = new DownloadModel(downloadModel.getId(), downloadModel.getUrl(), downloadModel.getTitle(), downloadModel.getFile_path(), downloadModel.getProgress(), downloadModel.getStatus(), downloadModel.getFile_size(), downloadModel.getUri(), downloadModel.getMimeType(), downloadModel.getTotal_file_Size(), downloadModel.isDownloadingWith());
        downloadService.isResume = resume;
        Intent serviceIntent = new Intent(this, downloadService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void searQuery(String query) {
        webViewArrayList.get(GlobalVariables.getActiveWebView()).loadUrl(extraFunctions.ValidateUrl(query));
        home_body.setVisibility(View.GONE);
        dragable.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        findViewById(R.id.layout_sear_toolbar).setVisibility(View.VISIBLE);

        home_body.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        findViewById(R.id.layout_sear_toolbar).setVisibility(View.VISIBLE);

        Objects.requireNonNull(webViewArrayList.get(GlobalVariables.getActiveWebView())).setVisibility(View.VISIBLE);
        searchView_home.setVisibility(View.VISIBLE);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(webViewArrayList.get(GlobalVariables.getActiveWebView()).getApplicationWindowToken(), 0);
    }


    @Override
    public void onBackPressed() {

        if (wraper.getVisibility() == View.VISIBLE) {
            if (settingFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(settingFragment).commit();
            } else if (SearchToolbarFragLocal.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(SearchToolbarFragLocal).commit();

            }
            dragView.setVisibility(View.VISIBLE);
            wraper.setVisibility(View.GONE);
            homeFragment.setVisibility(View.VISIBLE);

            wraper.setVisibility(View.GONE);
        } else if (Objects.requireNonNull(webViewArrayList.get(GlobalVariables.getActiveWebView())).getVisibility() == View.VISIBLE) {
            if (Objects.requireNonNull(webViewArrayList.get(GlobalVariables.getActiveWebView())).canGoBack()) {
                Objects.requireNonNull(webViewArrayList.get(GlobalVariables.getActiveWebView())).goBack();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void copyUrl() {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("url", webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl());
        myClipboard.setPrimaryClip(myClip);
        wraper.setVisibility(View.GONE);
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
    }

    public void home() {


        int id = GlobalVariables.getActiveWebView();

        if (webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl() == null) {
            return;
        } else if (!webViewArrayList.get(GlobalVariables.getActiveWebView()).getUrl().equals("")) {
            newTabOpen();
            extraFunctions.resetDownloadBtn();
        } else {
            return;
        }
        webViewArrayList.get(GlobalVariables.getActiveWebView()).setVisibility(View.GONE);
        home_body.setVisibility(View.VISIBLE);
        dragable.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        logo.setVisibility(View.VISIBLE);
        findViewById(R.id.layout_sear_toolbar).setVisibility(View.GONE);
        GlobalVariables.removeTabModel(id);

    /*    if (webViewArrayList.get(id) != null) {
            Realm.getDefaultInstance().executeTransaction(realm -> realm.where(HistoryModel.class).equalTo("stateOfPage",true).equalTo("webviewID",id).findFirst().setStateOfPage(false));
        }*/

        homeFragment.removeView(webViewArrayList.get(id));
        GlobalVariables.removeWebPageDatas(id);
        webViewArrayList.remove(id);
        TabNo.setText((Integer.toString(webViewArrayList.size())));

        /*webViewArrayList.get(GlobalVariables.getActiveHomeFragment()).loadUrl("");


        Objects.requireNonNull(webViewArrayList.get(GlobalVariables.getActiveHomeFragment())).setVisibility(View.GONE);

        homeFragment.setVisibility(View.VISIBLE);
        if (GlobalVariables.getActiveHomeFragment() != 0)
            findViewById(GlobalVariables.getActiveHomeFragment()).setVisibility(View.GONE);

        home_body.setVisibility(View.VISIBLE);

        logo.setVisibility(View.VISIBLE);
        findViewById(R.id.layout_sear_toolbar).setVisibility(View.GONE);


        wraper.setVisibility(View.GONE);*/

    }

    public final void OpenFB(View view) {
        searQuery("https://m.facebook.com");
    }

    public final void OpenInsta(View view) {
        searQuery("https://www.instagram.com");
    }

    public final void OpenVimeo(View view) {
        searQuery("https://vimeo.com/watch");
    }

    public final void OpenTubidy(View view) {
//        searQuery("https://tubidy.mobi");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=9968740045")));
        //whatsapp://send?phone=9968740045
    }

    public final void OpenWhatsAppStatudDownloader(View view) {
        askPermission("Storage permission is needed to Read WhatsApp Stories !");
        startActivity(new Intent(MainActivity.this, WhatsAppActivity.class));
    }

    public void refreshPage(View view) {
        if (webViewArrayList.get(GlobalVariables.getActiveWebView()).getVisibility() == View.VISIBLE) {
            webViewArrayList.get(GlobalVariables.getActiveWebView()).reload();
        }
    }

    public void OpenMainMenuHomePage(View view) {
        PopupWindowCustom popupWindowView = new PopupWindowCustom();
        popupWindowView.showSortPopupMainMenu(this, new Point((int) view.getX(), (int) view.getY()));
    }


    public void NewTabBtn(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        wraper.setVisibility(View.VISIBLE);
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_top, R.anim.slide_to_bottom);
        fragmentTransaction.replace(R.id.wraper, new newTabList());
        fragmentTransaction.commit();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfrombookmarks:
                Intent intent = new Intent(this, Bookmarks.class);
                startActivity(intent);
                break;
            case R.id.addfromhistory:
                Intent intent2 = new Intent(this, History.class);
                startActivity(intent2);
                break;
            case R.id.removeBookmarkfromHome:
                removeFromHomepage();
                break;
            default:
                break;
        }
        return false;
    }

    private void removeFromHomepage() {
        realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        realm.where(HomePageBookmark.class).equalTo("id", selectedPopupHomeitemID).findFirst().deleteFromRealm();
        realm.commitTransaction();
        Glide.with(this).load(R.drawable.add_drawable).into((ImageView) findViewById(bookmarkList.get(selectedPopupHomeitemIndex)));
        findViewById(bookmarkList.get(selectedPopupHomeitemIndex)).setOnLongClickListener(null);
        updateHomePageIcons();
    }


    public void updateHomePageIcons() {
        GlobalVariables.resetHomePageIcon();
        realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        RealmResults<HomePageBookmark> results = realm.where(HomePageBookmark.class).findAll().sort("id");

        for (int i = 0; i < 5; i++) {
            if (i < results.size()) {
                if (!results.get(i).getUri().equals(""))
                    Glide.with(this).load(extraFunctions.convert(results.get(i).getUri())).circleCrop().into((ImageView) findViewById(bookmarkList.get(i)));
                else
                    Glide.with(this).load(R.drawable.web_drawable).circleCrop().into((ImageView) findViewById(bookmarkList.get(i)));
                GlobalVariables.setHomePageicons(new HomePageBookmark(i, results.get(i).getUrl(), results.get(i).getTitle(), results.get(i).getUri()));
                int finalI = i;
                findViewById(bookmarkList.get(i)).setOnLongClickListener(v -> {
                    if (results.size() >= finalI)
                        if (results.get(finalI) != null)
                            popup(v, finalI, results.get(finalI).getId());
                    return true;
                });
            } else {
                Glide.with(this).load(R.drawable.add_drawable).circleCrop().into((ImageView) findViewById(bookmarkList.get(i)));
            }
        }
        realm.commitTransaction();
    }

    private void popup(View v, int i, int id) {
        selectedPopupHomeitemID = id;
        selectedPopupHomeitemIndex = i;
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.long_click_home_bookmark);
        popupMenu.show();
    }


    @SuppressLint("NonConstantResourceId")
    public void bookmarkClick(View view) {
        switch (view.getId()) {
            case R.id.bookmark2:
                openThisBookmark(0);
                break;
            case R.id.bookmark3:
                openThisBookmark(1);
                break;
            case R.id.bookmark4:
                openThisBookmark(2);
                break;
            case R.id.bookmark5:
                openThisBookmark(3);
                break;
            case R.id.bookmark6:
                openThisBookmark(4);
            default:
                break;
        }

    }

    private void openThisBookmark(int id) {
        if (GlobalVariables.getHomePageicons().get(id) != null) {
            newTabOpen();
            searQuery(GlobalVariables.getHomePageicons().get(id).getUrl());
        } else {
            PopupMenu popupMenu = new PopupMenu(this, findViewById(bookmarkList.get(id)));
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.home_page_unregistered_bookmarks);
            popupMenu.show();
        }
    }


    public void showAd() {
        RewardedVideoAd rewardedVideoAd;
        stateChangeLoadingAd(true);
        MobileAds.initialize(this, "ca-app-pub-4164380086863414~1599073812");
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        AdVideo temp = new AdVideo(rewardedVideoAd);

        rewardedVideoAd.setRewardedVideoAdListener(temp);
        temp.loadAds();
    }

    public void stateChangeLoadingAd(boolean state) {
        findViewById(R.id.ladingLayout).setVisibility(state ? View.VISIBLE : View.GONE);
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView
            adView) {

//        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.iconViewAd));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        //adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            Glide.with(this).load(nativeAd.getIcon().getDrawable()).into((ImageView) adView.getIconView());
            Log.d(TAG, "populateUnifiedNativeAdView: tt");
            //((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }
        /*
        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());

        }
*/
        /*if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }*/
        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);

        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }

    private void refreshAd(int frameLayoutID) {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ADMOB_ADS_UNIT_ID));
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            FrameLayout frameLayout = findViewById(frameLayoutID);
            if (frameLayout == null) {
                return;
            }
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = unifiedNativeAd;

            @SuppressLint("InflateParams") UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);

            populateUnifiedNativeAdView(unifiedNativeAd, adView);

            frameLayout.removeAllViews();
            frameLayout.addView(adView);

        });
        NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    private void downloadImage(String downloadImageUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(downloadImageUrl);
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);

                runOnUiThread(() -> {
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File myDir = new File(root + "/DownloaderBrow");
                    myDir.mkdirs();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists()) file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void closeAllTab() {
        int s = webViewArrayList.size() + 2;
        for (int i = 0; i < s; i++) {
            closeTab(GlobalVariables.getActiveWebView());
        }

    }

    public void updateDownloadFromAsyncTaskDownloadManager(Long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(DownloadModel.class).equalTo("id", id).findFirst().setStatus(Constants.DOWNLOAD_STATUS.COMPLETE);
        realm.commitTransaction();

    }

    public void failedDialoge(DownloadModel downloadModel) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.failed_dialoge_downloading);
        dialog.findViewById(R.id.cancel_btn_failed_download_dialoge).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.delete_btn_failed_download_dialoge).setOnClickListener(v -> {
            extraFunctions.downloadWithDownloadManager(downloadModel);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    String testResultExtra;

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Toast.makeText(mContext, "Imaget", Toast.LENGTH_SHORT).show();
        WebView.HitTestResult hitTestResult = webViewArrayList.get(GlobalVariables.getActiveWebView()).getHitTestResult();
        testResultExtra = hitTestResult.getExtra();
        Log.d(TAG, "onCreateContextMenu: " + testResultExtra + " : \n" + hitTestResult.getType());
        if (v.getId() == webViewArrayList.get(GlobalVariables.getActiveWebView()).getId()) {


            if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE || hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                if (URLUtil.isNetworkUrl(testResultExtra)) {
                    contextMenu.add(0, R.id.downloadmageId, 0, "Download Image");
                    contextMenu.add(0, R.id.openInNewTabId, 0, "Open In New Tab");
                    contextMenu.add(0, R.id.shareLinkId, 0, "Share Link");
                    contextMenu.add(0, R.id.copyLinkId, 0, "Copy Link Address");
                }
            } else if (hitTestResult.getType() == WebView.HitTestResult.ANCHOR_TYPE
                    || hitTestResult.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
                contextMenu.add(0, R.id.openInNewTabId, 0, "Open In New Tab");
                contextMenu.add(0, R.id.shareLinkId, 0, "Share Link");
                contextMenu.add(0, R.id.copyLinkId, 0, "Copy Link Address");
            }
        }

        super.onCreateContextMenu(contextMenu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.downloadmageId) {
            downloadProcess(testResultExtra, null, "image", 0);
        } else if (item.getItemId() == R.id.openInNewTabId) {
            newTabOpen();
            searQuery(testResultExtra);
        } else if (item.getItemId() == R.id.shareLinkId) {
            extraFunctions.shareFile(testResultExtra);
        } else if (item.getItemId() == R.id.copyLinkId) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copied Link", testResultExtra);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public void searchInPage() {
        setFragment(new SearchInPage());
    }

    private void onDismiss(DialogInterface dialog) {
        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
            }
        }
        threads.clear();
    }
}
