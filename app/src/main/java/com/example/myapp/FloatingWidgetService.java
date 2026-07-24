package com.example.myapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class FloatingWidgetService extends Service {
    private WindowManager windowManager;
    private View floatingView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Screen ke upar render hone wala view context element setup karna
        floatingView = new View(this);
        // Iska UI background color green (Hara) rakh rahe hain
        floatingView.setBackgroundColor(0xFF00FF00); 

        int layoutType;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutType = WindowManager.LayoutParams.TYPE_PHONE;
        }

        // Layout variables size setup pixels aur flags configuration
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                150, // width configuration size
                150, // height configuration size
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Taki baaki touches piche transfer ho sakein
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 200;
        params.y = 200;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }
}
