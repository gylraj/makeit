package com.gylraj.makeit;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static com.gylraj.makeit.Utils.Utils.setColorAlpha;

public class MakeItToo extends Service implements View.OnClickListener{


    private static final int mLayoutParamFlags = WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
    // Views
    private View mDemoView;
    private Button mEndButton;
    private ImageView androidHead;

    private WindowManager wm;
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

        androidHead = new ImageView(this);
        androidHead.setImageResource(R.mipmap.ic_launcher);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP | Gravity.LEFT;

        wm.addView(androidHead, params);

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
        removeView();
    }

    private void removeView() {
        if (androidHead != null) {
            wm.removeView(androidHead);
        }
    }

    @Override
    public void onClick(View view) {
        return;
    }
}
