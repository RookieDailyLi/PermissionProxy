package com.credithc.permissionproxy;

/**
 * @data: 2019/9/28 0028
 * @author: liyong
 * @desc: class description
 */
public class MainActivity$PermissionProxy implements PermissionProxy<MainActivity> {
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

	}

	@Override
	public void rationale(MainActivity activity, int requestCode, String[] permissions) {

	}
}
