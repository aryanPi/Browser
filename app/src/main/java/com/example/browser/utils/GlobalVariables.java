package com.example.browser.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.browser.model.DownloadModel;
import com.example.browser.model.HomePageBookmark;
import com.example.browser.model.TabModel;
import com.example.browser.model.WebPageDataTempModel;
import com.example.browser.model.extra;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;

public class GlobalVariables {


    public static Activity mActivity;
    public static ArrayList<TabModel> tabModels = new ArrayList<>();

    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static int btnXEnd, btnYTop, btnXSt, btnYSt;
    public static String url = "NONE";

    public static int activeHomeFragment = 0;
    public static boolean videoState = false;

    public static int flag = 0;

    public static boolean selectWAFlag = false;
    public static boolean selectAllWA = false;
    public static ArrayList<String> saveImagespathWA = new ArrayList<>();

    public static boolean darkModeWebview = false;

    public static Long historyStack = 0L;

    public static Resources resources;

    public static int stateOfRestoreTabs = 0;

    public static boolean incognitoMode = false;

    public static boolean desktopViewMode = false;

    public static boolean downloadFragState = false;

    public static List<DownloadModel> downloadModels = new ArrayList<>();

    public static HashMap<Long, Long> downloadingItem = new HashMap<>();

        public static boolean isPermission = false;

    public static Fragment downloadingFrag;

    public static Fragment getDownloadingFrag() {
        return downloadingFrag;
    }

    public static HashMap<Integer, WebPageDataTempModel> WebPageDatas = new HashMap<>();


    public static boolean isDarkModeWebview() {
        return darkModeWebview;
    }

    public static void setDarkModeWebview(boolean darkModeWebview) {
        GlobalVariables.darkModeWebview = darkModeWebview;
    }

    public static boolean isIsPermission() {
        return isPermission;
    }

    public static void setIsPermission(boolean isPermission) {
        GlobalVariables.isPermission = isPermission;
    }

    public static WebPageDataTempModel getWebPageDatas(Integer id) {
        return WebPageDatas.get(id);
    }

    public static void setWebPageDatas(Integer id, WebPageDataTempModel webPageDatas) {
        WebPageDatas.put(id, webPageDatas);
    }

    public static void removeWebPageDatas(Integer id) {
        WebPageDatas.remove(id);
    }

    public static void setDownloadingFrag(Fragment downloadingFrag) {
        GlobalVariables.downloadingFrag = downloadingFrag;
    }


    public static Long getDownloadingItem(Long id) {
        return downloadingItem.get(id);
    }
    public static HashMap<Long,Long> getDownloadingItemHashMap(){
        return downloadingItem;
    }

    public static boolean searchCallFromHome = false;
    public static String searchQToolbar = "";

    public static String getSearchQToolbar() {
        return searchQToolbar;
    }

    public static void setSearchQToolbar(String searchQToolbar) {
        GlobalVariables.searchQToolbar = searchQToolbar;
    }

    public static boolean isSearchCallFromHome() {
        return searchCallFromHome;
    }

    public static void setSearchCallFromHome(boolean searchCallFromHome) {
        GlobalVariables.searchCallFromHome = searchCallFromHome;
    }

    public static void setDownloadingItem(Long id, Long val) {
        GlobalVariables.downloadingItem.put(id, val);
    }


    public static void setDownloadModels(List<DownloadModel> downloadModels) {
        GlobalVariables.downloadModels = downloadModels;
    }

    public static boolean isDownloadFragState() {
        return downloadFragState;
    }

    public static void setDownloadFragState(boolean downloadFragState) {
        GlobalVariables.downloadFragState = downloadFragState;
    }

    public static boolean isDesktopViewMode() {
        return desktopViewMode;
    }

    public static void setDesktopViewMode(boolean desktopViewMode) {
        GlobalVariables.desktopViewMode = desktopViewMode;
    }

    public static boolean isIncognitoMode() {
        Log.d("tomar", "setIncognitoMode: ret" + incognitoMode);
        return incognitoMode;
    }

    public static void setIncognitoMode(boolean incognitoMode) {
        GlobalVariables.incognitoMode = incognitoMode;
        Log.d("tomar", "setIncognitoMode: " + incognitoMode);
    }

    public static HashMap<Integer, HomePageBookmark> homePageicons = new HashMap<>();


    public static HashMap<Integer, HomePageBookmark> getHomePageicons() {
        return homePageicons;
    }

    public static String video = "";

    public static Long lastVideoSeek = 0L;

    public static Long getLastVideoSeek() {
        return lastVideoSeek;
    }

    public static void setLastVideoSeek(Long lastVideoSeek) {
        GlobalVariables.lastVideoSeek = lastVideoSeek;
    }

    public static String getVideo() {
        return video;
    }

    public static void setVideo(String video) {
        GlobalVariables.video = video;
    }

    public static void resetHomePageIcon() {
        GlobalVariables.homePageicons = new HashMap<>();
    }

    public static void setHomePageicons(HomePageBookmark homePageicons) {
        for (int i = 0; i < GlobalVariables.homePageicons.size(); i++) {
            if (GlobalVariables.homePageicons.get(i).getId() == homePageicons.getId()) {
                GlobalVariables.homePageicons.remove(i);
                GlobalVariables.homePageicons.put(homePageicons.getId(), homePageicons);
                return;
            }
        }
        GlobalVariables.homePageicons.put(homePageicons.getId(), homePageicons);
    }

    public static int isStateOfRestoreTabs() {
        return stateOfRestoreTabs;
    }

    public static void setStateOfRestoreTabs(int stateOfRestoreTabs) {
        GlobalVariables.stateOfRestoreTabs = stateOfRestoreTabs;
    }

    public static extra extraData = new extra();


    public static Activity getmActivity() {
        return mActivity;
    }

    public static void setmActivity(Activity mActivity) {
        GlobalVariables.mActivity = mActivity;
    }

    public static extra getExtraData() {
        return extraData;
    }

    public static void setExtraData(extra extraData) {
        GlobalVariables.extraData = extraData;
    }

    public static Resources getResources() {
        return GlobalVariables.resources;
    }

    public static void setResources(Resources resources) {
        GlobalVariables.resources = resources;
    }

    public static Long getHistoryStack() {
        return historyStack;
    }

    public static void setHistoryStack(Long historyStack) {
        Realm realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        try {
            if (realm.where(extra.class).findFirst() != null) {
                Log.d("tomar", "setHistoryStack: " + historyStack);
                realm.where(extra.class).findFirst().setHistoryStack(historyStack);
            } else {
                realm.copyToRealm(GlobalVariables.getExtraData());
            }
        } catch (Exception e) {
            Log.d("tomar", "setHistoryStack: ");
            //            GlobalVariables.setHistoryStack(historyStack);
            e.printStackTrace();
        }
        realm.commitTransaction();
        GlobalVariables.historyStack = historyStack;
    }

    public static ArrayList<String> getSaveImagespathWA() {
        return saveImagespathWA;
    }

    public static void deselectImagePathWA(String ImagePath) {
        for (int i = 0; i < saveImagespathWA.size(); i++)
            if (saveImagespathWA.get(i).equals(ImagePath)) {
                saveImagespathWA.remove(i);
            }
    }

    public static void addSaveImagespathWA(String saveImagespathWA) {
        if (!GlobalVariables.saveImagespathWA.contains(saveImagespathWA))
            GlobalVariables.saveImagespathWA.add(saveImagespathWA);
    }

    public static boolean isSelectAllWA() {
        return selectAllWA;
    }

    public static void setSelectAllWA(boolean selectAllWA) {
        GlobalVariables.selectAllWA = selectAllWA;
    }

    public static boolean isSelectWAFlag() {
        return selectWAFlag;
    }

    public static void setSelectWAFlag(boolean selectWAFlag) {
        GlobalVariables.selectWAFlag = selectWAFlag;
    }

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int flag) {
        GlobalVariables.flag = flag;
    }

    public static ArrayList<TabModel> getTabModels() {
        return tabModels;
    }

    public static TabModel getThisIdTableModel(int id) {
        for(TabModel t:GlobalVariables.tabModels){
            if(t.getId()==id)return t;
        }
        return GlobalVariables.tabModels.get(0);
    }

    public static void removeTabModel(int id) {
        for(int i=0;i<GlobalVariables.tabModels.size();i++){
            if (GlobalVariables.tabModels.get(i).getId()==id){
                GlobalVariables.tabModels.remove(i);
                break;
            }
        }
    }

    public static void setTabModels(Integer id,TabModel tabModels) {
        for(int i=0;i<GlobalVariables.tabModels.size();i++){
            if(GlobalVariables.tabModels.get(i).getId()==tabModels.getId()){
                GlobalVariables.tabModels.set(i,tabModels);
                break;
            }
        }
    }
    public static void addNewTabModel(TabModel tabModel){
        GlobalVariables.tabModels.add(tabModel);
    }

    public static int getActiveWebView() {
        return activeHomeFragment;
    }

    public static int getBtnXEnd() {
        return btnXEnd;
    }

    public static int getBtnYTop() {
        return btnYTop;
    }

    public static int getBtnXSt() {
        return btnXSt;
    }

    public static int getBtnYSt() {
        return btnYSt;
    }

    public static void setBtnXSt(int btnXSt) {
        GlobalVariables.btnXSt = btnXSt;
    }

    public static void setBtnYSt(int btnYSt) {
        GlobalVariables.btnYSt = btnYSt;
    }

    public static void setBtnXEnd(int btnX) {
        GlobalVariables.btnXEnd = btnX;
    }

    public static void setBtnYTop(int btnY) {
        GlobalVariables.btnYTop = btnY;
    }


    public static void setDisplayMetrics(DisplayMetrics dm) {
        displayMetrics = dm;
    }

    public static void setActiveHomeFragment(int activeHomeFragment) {
        GlobalVariables.activeHomeFragment = activeHomeFragment;
    }


    public static void setUrl(String url) {
        GlobalVariables.url = url;
    }

    public static String getUrl() {
        return url;
    }

    public static boolean isVideoState() {
        return videoState;
    }

    public static void setVideoState(boolean videoState) {
        GlobalVariables.videoState = videoState;
    }
}
