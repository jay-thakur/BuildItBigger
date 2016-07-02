package com.appniche.builditbigger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import static android.R.attr.y;
import static android.os.Build.VERSION_CODES.M;
import static com.appniche.jokedisplay.JokeDisplayActivity.LOG_TAG;


public class MainActivityFragment extends Fragment {

    InterstitialAd mInterstitialAd;
    ProgressBar progressBar;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        Button button = (Button) root.findViewById(R.id.tell_joke_button);

        //interstitialAd part starts here
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                tellJoke(getView());
                //MainActivity.tellJoke(getActivity(), progressBar);
            }
        });

        requestNewInterstitial();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "in the onclick method");
                if (mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {
                    Log.d(LOG_TAG, "in the else block");
                    tellJoke(getView());
                    //MainActivity.tellJoke(view.getContext(), progressBar);
                }
            }
        });
        return root;
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void tellJoke(View view){
        Log.d(LOG_TAG, " inside tell joke method");
        progressBar.setVisibility(View.VISIBLE);// this line is same as onPreExecute()
        new EndpointsAsyncTask(progressBar).execute(getActivity());
    }


}
