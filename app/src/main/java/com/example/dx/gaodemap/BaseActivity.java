package com.example.dx.gaodemap;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.dx.gaodemap.permisions.Permissions;

public class BaseActivity extends Activity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
