package com.gylraj.makeit;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.gylraj.makeit.Utils.Utils.setColorAlpha;

/**
 * Created by mac on 02/01/2018.
 */

public class MakeItService extends Service implements View.OnClickListener{


    private static final int mLayoutParamFlags = WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
    // Views
    private View mDemoView;
    private Button mEndButton;

    float c_alpha = 0.5f;
    float c_red = 255.0f;
    float c_green = 123.0f;
    float c_blue = 123.0f;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void showNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Demo App")
                .setContentText("Demo Message");

        startForeground(R.string.app_name, notificationBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        showNotification();

        return START_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mDemoView = layoutInflater.inflate(R.layout.windowmanager_demo, null);
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//        WindowManager.LayoutParams.MATCH_PARENT,
//        WindowManager.LayoutParams.MATCH_PARENT,
//        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//        mLayoutParamFlags,
//        PixelFormat.TRANSLUCENT);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        mEndButton = (Button) mDemoView.findViewById(R.id.end_button);
        //mEndButton.setVisibility(View.GONE);
//        mEndButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "text ", Toast.LENGTH_SHORT).show();
//            }
//        });

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(mDemoView, params);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mDemoView.setBackgroundColor(Color.argb(c_alpha, c_red, c_green, c_blue));
//        }else{
//            mDemoView.setBackgroundColor(Color.parseColor(setColorAlpha(80,"#000000")));
//        }
        mDemoView.setBackgroundColor(Color.parseColor(setColorAlpha(80,"#000000")));

        registerBroadcastReceiver();
    }
    public static String addAlpha(String originalColor, double alpha) {
        long alphaFixed = Math.round(alpha * 255);
        String alphaHex = Long.toHexString(alphaFixed);
        if (alphaHex.length() == 1) {
            alphaHex = "0" + alphaHex;
        }
        originalColor = originalColor.replace("#", "#" + alphaHex);


        return originalColor;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove notification
        unregisterBroadcastReceiver();
        stopForeground(true);
        // Remove WindowManager
        if(mDemoView != null){
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(mDemoView);
        }
    }

    @Override
    public void onClick(View view) {
        return;
    }

    private BroadcastReceiver br;
    public static final String GET_RANGE = "getRange";
    private void registerBroadcastReceiver() {
        if (br == null) {
            br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    final Bundle extras = intent.getExtras();
                    if(action == GET_RANGE){
                        int range = extras.getInt("range", 30);
                        if(mDemoView!=null) {
                            mDemoView.setBackgroundColor(Color.parseColor(setColorAlpha(range, "#000000")));
                        }

                    }
                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(GET_RANGE);
            registerReceiver(br, intentFilter);
        }
    }
    private void unregisterBroadcastReceiver(){
        if (br != null) {
            unregisterReceiver(br);
            br = null;
        }
    }
}
