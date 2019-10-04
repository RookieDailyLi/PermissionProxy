package com.credithc.libpermissionhelper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @data: 2019/10/4 0004
 * @author: liyong
 * @desc: class description
 */
public class PermissionHelper {

	public static final String SUFFIX = "$PermissionProxy";

	public static List<String> permissionGrantPrevious = new ArrayList<>();

	/**
	 * Activity申请权限调用
	 *
	 * @param activity
	 * @param requestCode
	 * @param permissions
	 */
	public static void requestPermission(Activity activity, int requestCode, String... permissions) {
		permissionGrantPrevious.clear();
		permissionGrantPrevious = findGrantPermissions(activity, permissions);
		doRequestPermission(activity, permissions, requestCode);
	}

	/**
	 * Fragment申请权限调用
	 *
	 * @param activity
	 * @param requestCode
	 * @param permissions
	 */
	public static void requestPermission(Fragment activity, int requestCode, String... permissions) {
		permissionGrantPrevious.clear();
		permissionGrantPrevious = findGrantPermissions(activity.getActivity(), permissions);
		doRequestPermission(activity.getActivity(), permissions, requestCode);
	}

	/**
	 * 调用android api,申请权限
	 *
	 * @param activity
	 * @param permissions
	 * @param requestCode
	 */
	private static void doRequestPermission(final Activity activity, final String[] permissions, final int requestCode) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			doExecuteGrant(activity, permissions, requestCode);
			return;
		}

		List<String> rationaleList = findRationalePermissions(activity, permissions);
		String[] rationaleArray = rationaleList.toArray(new String[rationaleList.size()]);
		List<String> denyList = findDenyPermissions(activity, permissions);
		final String[] denyArray = denyList.toArray(new String[denyList.size()]);

		//不需要向用户解释，直接请求
		if (rationaleList.size() == 0) {
			ActivityCompat.requestPermissions(activity, denyArray, requestCode);
			return;
		}

		//需要向用户弹窗解释为什么需要以下权限
		doExecuteRationale(activity, rationaleArray, requestCode, new RationaleCallBack() {
			@Override
			public void executeRationale(String[] rationalePermissions) {
				ActivityCompat.requestPermissions(activity, denyArray, requestCode);
			}
		});
	}

	/**
	 * 找到已经授权的权限
	 *
	 * @param activity
	 * @param permissions
	 * @return
	 */
	public static List<String> findGrantPermissions(final Activity activity, String[] permissions) {
		List<String> permissionGrantList = new ArrayList<>();
		for (String permission : permissions) {
			if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
				permissionGrantList.add(permission);
			}
		}
		return permissionGrantList;
	}

	/**
	 * 找到没有授权的权限
	 *
	 * @param activity
	 * @param permissions
	 * @return
	 */
	public static List<String> findDenyPermissions(final Activity activity, String[] permissions) {
		List<String> permissionDenyList = new ArrayList<>();
		for (String permission : permissions) {
			if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
				permissionDenyList.add(permission);
			}
		}
		return permissionDenyList;
	}

	/**
	 * 找到需要向用户解释的权限
	 *
	 * @param activity
	 * @param permissions
	 * @return
	 */
	public static List<String> findRationalePermissions(final Activity activity, String[] permissions) {
		List<String> permissionRationList = new ArrayList<>();
		for (String permission : permissions) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				permissionRationList.add(permission);
			}
		}
		return permissionRationList;
	}

	/**
	 * 授权的权限回调
	 *
	 * @param activity
	 * @param permissions
	 * @param requestCode
	 */
	private static void doExecuteGrant(Activity activity, String[] permissions, int requestCode) {
		PermissionProxy proxy = findProxy(activity);
		if (proxy != null) {
			proxy.grant(activity, permissions, requestCode);
		}
	}

	/**
	 * 未授权的权限回调
	 *
	 * @param activity
	 * @param permissions
	 * @param requestCode
	 */
	private static void doExecuteDeny(Activity activity, String[] permissions, int requestCode) {
		PermissionProxy proxy = findProxy(activity);
		if (proxy != null) {
			proxy.deny(activity, permissions, requestCode);
		}
	}

	/**
	 * 需要解释的权限回调
	 *
	 * @param activity
	 * @param permissions
	 * @param requestCode
	 * @param callBack
	 */
	private static void doExecuteRationale(Activity activity, String[] permissions, int requestCode, RationaleCallBack callBack) {
		PermissionProxy proxy = findProxy(activity);
		if (proxy != null) {
			proxy.rationale(activity, permissions, requestCode, callBack);
		}
	}

	public static void doExecuteResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
		List<String> granted = new ArrayList<>();
		List<String> denied = new ArrayList<>();
		int length = grantResults.length;
		for (int i = 0; i < length; i++) {
			if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
				granted.add(permissions[i]);
			} else {
				denied.add(permissions[i]);
			}
		}
		granted.addAll(permissionGrantPrevious);
		String[] grantedArray = new String[granted.size()];
		String[] deniedArray = new String[denied.size()];
		doExecuteGrant(activity, granted.toArray(grantedArray), requestCode);
		doExecuteDeny(activity, denied.toArray(deniedArray), requestCode);
	}

	/**
	 * 找到APT权限代理类
	 *
	 * @param activity
	 * @return
	 */
	private static PermissionProxy findProxy(Activity activity) {
		Class<?> cls = activity.getClass();
		PermissionProxy proxy = null;
		try {
			Class proxyCls = Class.forName(cls.getName() + SUFFIX);
			proxy = (PermissionProxy) proxyCls.newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			return proxy;
		}
	}

}
