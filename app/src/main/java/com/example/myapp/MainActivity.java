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

        // Check karna ki kya screen alert layout pehle se allow hai
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Settings manager open karna permission activate karne ke liye
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            startFloatingService();
        }
    }

    private void startFloatingService() {
        startService(new Intent(MainActivity.this, FloatingWidgetService.class));
        finish(); // Main page interface ko background background thread se terminate karna
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
