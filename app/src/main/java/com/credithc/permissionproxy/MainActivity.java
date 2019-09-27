package com.credithc.permissionproxy;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.credithc.libannotation.PermissionDenied;
import com.credithc.libannotation.PermissionGranted;
import com.credithc.libannotation.PermissionRationale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @PermissionGranted({Manifest.permission.READ_CONTACTS})
    public void onRequestPermissionGranted(Activity activity, int requestCode, String[] permissions) {
        Toast.makeText(activity, "权限申请通过", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied({Manifest.permission.READ_CONTACTS})
    public void onShouldRequestPermissionRationale(Activity activity, int requestCode, String[] permissions) {
    }

    @PermissionRationale({Manifest.permission.READ_CONTACTS})
    public void onRequestPermissionDenied(Activity activity, int requestCode, String[] permissions) {
        Toast.makeText(activity, "权限申请失败", Toast.LENGTH_SHORT).show();
    }
}
