package com.credithc.permissionproxy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.credithc.libannotation.PermissionDenied;
import com.credithc.libannotation.PermissionGranted;
import com.credithc.libannotation.PermissionRationale;

public class MainActivity extends AppCompatActivity {

	public static final int REQUEST_CODE_READ_CONTACTS = 0X10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@PermissionGranted(REQUEST_CODE_READ_CONTACTS)
	public void onRequestPermissionGranted(String[] permissions) {
		Toast.makeText(this, "权限申请通过", Toast.LENGTH_SHORT).show();
	}

	@PermissionRationale(REQUEST_CODE_READ_CONTACTS)
	public void onRequestPermissionDenied(String[] permissions) {
		Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
	}

	@PermissionDenied(REQUEST_CODE_READ_CONTACTS)
	public void onShouldRequestPermissionRationale(String[] permissions) {
	}

}
