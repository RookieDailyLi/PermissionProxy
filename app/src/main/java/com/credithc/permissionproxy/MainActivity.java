package com.credithc.permissionproxy;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.credithc.libannotation.PermissionDenied;
import com.credithc.libannotation.PermissionGranted;
import com.credithc.libannotation.PermissionRationale;
import com.credithc.libpermissionhelper.PermissionHelper;
import com.credithc.libpermissionhelper.RationaleCallBack;

public class MainActivity extends AppCompatActivity {

	public static final int REQUEST_CODE_READ_CONTACTS = 0X10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PermissionHelper.requestPermission(this, REQUEST_CODE_READ_CONTACTS
				, Manifest.permission.READ_CONTACTS
				, Manifest.permission.READ_SMS
				, Manifest.permission.ACCESS_COARSE_LOCATION);
	}

	@PermissionGranted(REQUEST_CODE_READ_CONTACTS)
	public void onRequestPermissionGranted(String[] permissions) {
		Toast.makeText(this, "权限申请通过" + appendPermission(permissions), Toast.LENGTH_SHORT).show();
	}

	@PermissionDenied(REQUEST_CODE_READ_CONTACTS)
	public void onRequestPermissionDenied(String[] permissions) {
		new AlertDialog.Builder(this)
				.setTitle("权限申请失败")
				.setMessage("请在系统->设置中授予以下权限，以继续使用应用\n" + appendPermission(permissions))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				})
				.create()
				.show();
	}

	@PermissionRationale(REQUEST_CODE_READ_CONTACTS)
	public void onShouldRequestPermissionRationale(final String[] rationalePermissions, final RationaleCallBack callBack) {
		new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("应用需要以下权限才能正常使用\n" + appendPermission(rationalePermissions))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						callBack.executeRationale(rationalePermissions);
					}
				})
				.create()
				.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionHelper.doExecuteResult(this, requestCode, permissions, grantResults);
	}

	private StringBuilder appendPermission(String... permissions) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String permission : permissions) {
			stringBuilder.append(permission + "\n");
		}
		return stringBuilder;
	}
}
