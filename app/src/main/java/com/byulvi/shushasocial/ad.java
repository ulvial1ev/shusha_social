package com.byulvi.shushasocial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class ad extends AppCompatActivity {
    RewardedAd mRewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(
                this,
                "ca-app-pub-2304984891162285/8375224360",adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        mRewardedAd = rewardedAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d("TAG",loadAdError.getMessage());
                    }
                });


    }

    private void aaa() {
        if (mRewardedAd.isLoaded()){
        mRewardedAd.show(this, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull com.google.android.gms.ads.rewarded.RewardItem rewardItem) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ad.this);
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt("adcontrol",0);
                edit.commit();
                finish();
            }
        });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
