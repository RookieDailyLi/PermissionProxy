package com.credithc.permissionproxy;

import com.credithc.libpermissionhelper.RationaleCallBack;

/**
 * @data: 2019/9/28 0028
 * @author: liyong
 * @desc: class description
 */
public interface PermiProxy<T> {
	void grant(T activity, int requestCode, String[] permissions);

	void deny(T activity, int requestCode, String[] permissions);

	void rationale(T activity, int requestCode, String[] permissions, RationaleCallBack callBack);
}
