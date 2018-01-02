package com.gylraj.makeit;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

    private static final int START_LEVEL = 1;
    private int mLevel;
    private Button mNextLevelButton;
    private InterstitialAd mInterstitialAd;
    private TextView mLevelTextView;
    private SeekBar c_range;
    private void updateAlpha(int range){
        Intent RTReturn = new Intent(MakeItService.GET_RANGE);
        RTReturn.putExtra("range", range);
        sendBroadcast(RTReturn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        c_range = (SeekBar) findViewById(R.id.c_range);
        c_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateAlpha(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updateAlpha(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateAlpha(seekBar.getProgress());
            }
        });

        MobileAds.initialize(this, "ca-app-pub-5236093434202887~1540523050");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Toast.makeText(getApplicationContext(), "onAdLoaded  ", Toast.LENGTH_LONG).show();

                if(true){
                    if(Build.VERSION.SDK_INT >= 23) {
                        if (!Settings.canDrawOverlays(MainActivity.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 1234);
                        }else{
                            Intent intent = new Intent(MainActivity.this, MakeItService.class);
                            startService(intent);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this, MakeItService.class);
                        startService(intent);
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toast.makeText(getApplicationContext(), "onAdFailedToLoad errorCode "+ errorCode, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Toast.makeText(getApplicationContext(), "onAdOpened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Toast.makeText(getApplicationContext(), "onAdLeftApplication", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Toast.makeText(getApplicationContext(), "onAdClosed", Toast.LENGTH_LONG).show();
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
