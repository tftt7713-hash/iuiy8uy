package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Check karna ki kya overlay permission pehle se hai
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } catch (Exception e) {
                // Kuch devices me package name ke bina intent kholna padta hai
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        } else {
            startFloatingService();
        }
    }

    private void startFloatingService() {
        Intent intent = new Intent(MainActivity.this, FloatingWidgetService.class);
        
        // 🔥 FIX: Android 8.0 (Oreo) aur upar ke liye startForegroundService zaroori hai
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        finish(); 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                startFloatingService();
            } else {
                Toast.makeText(this, "Permission missing! Cannot start overlay.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
