package com.example.myapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
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
        
        // 1. Screen ke upar render hone wala view setup
        floatingView = new View(this);
        floatingView.setBackgroundColor(0xFF00FF00); // Pure Green Color

        // 2. Layout Type define karna naye aur purane android versions ke liye
        int layoutType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            // Safe fallback for older versions
            @SuppressWarnings("deprecation")
            int oldType = WindowManager.LayoutParams.TYPE_PHONE;
            layoutType = oldType;
        }

        // 3. Layout Parameters set karne ka sabse safe aur 100% working tarika
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = 150;  // Width in pixels
        params.height = 150; // Height in pixels
        params.type = layoutType;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;

        // Gravity.LEFT ko Gravity.START se replace kiya (Modern Standard)
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 200;
        params.y = 200;

        // 4. Window Manager initialize karke view add karna
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(floatingView, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Safe removal check taaki NullPointerException na aaye
        if (windowManager != null && floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }
}
