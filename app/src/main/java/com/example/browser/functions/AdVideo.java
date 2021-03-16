package com.example.browser.functions;

import android.widget.Toast;

import com.example.browser.activities.MainActivity;
import com.example.browser.utils.GlobalVariables;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdVideo implements RewardedVideoAdListener {

    RewardedVideoAd rewardedVideoAd;
    public AdVideo( RewardedVideoAd rewardedVideoAd) {
    this.rewardedVideoAd = rewardedVideoAd;
    }

    public void loadAds() {
        rewardedVideoAd.loadAd( "ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build());

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        rewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        ((MainActivity)GlobalVariables.getmActivity()).stateChangeLoadingAd(false);
    }

    @Override
    public void onRewardedVideoStarted() {
        ((MainActivity)GlobalVariables.getmActivity()).stateChangeLoadingAd(false);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        ((MainActivity)GlobalVariables.getmActivity()).stateChangeLoadingAd(false);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        rewardedVideoAd.destroy();
        ((MainActivity)GlobalVariables.getmActivity()).stateChangeLoadingAd(false);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        ((MainActivity)GlobalVariables.getmActivity()).stateChangeLoadingAd(false);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(GlobalVariables.getmActivity(), "Error Loading Ad...", Toast.LENGTH_SHORT).show();
        ((MainActivity)GlobalVariables.getmActivity()).stateChangeLoadingAd(false);
    }

    @Override
    public void onRewardedVideoCompleted() {
    }
    //1,6,3,2 on error 5
}
