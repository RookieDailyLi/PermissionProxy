package com.credithc.permissionproxy;

import com.credithc.libpermissionhelper.RationaleCallBack;

/**
 * @data: 2019/9/28 0028
 * @author: liyong
 * @desc: class description
 */
public class MainActivity$$PermissionProxy implements PermiProxy<MainActivity> {
	@Override
	public void grant(MainActivity activity, int requestCode, String[] permissions) {
		switch (requestCode) {
			case 100:
				activity.onRequestPermissionGranted(permissions);
				break;
		}
	}

	@Override
	public void deny(MainActivity activity, int requestCode, String[] permissions) {
		switch (requestCode) {
			case 100:
				activity.onRequestPermissionDenied(permissions);
				break;
		}
	}

	@Override
	public void rationale(MainActivity activity, int requestCode, String[] permissions, RationaleCallBack callBack) {
		switch (requestCode) {
			case 100:
				activity.onShouldRequestPermissionRationale(permissions,callBack);
				break;
		}
	}
}
