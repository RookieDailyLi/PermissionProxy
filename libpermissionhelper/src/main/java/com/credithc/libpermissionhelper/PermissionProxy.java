package com.credithc.libpermissionhelper;

/**
 * @data: 2019/9/28 0028
 * @author: liyong
 * @desc: class description
 */
public interface PermissionProxy<T> {
	void grant(T activity, String[] permissions, int requestCode);

	void deny(T activity, String[] permissions, int requestCode);

	void rationale(T activity, String[] permissions, int requestCode,RationaleCallBack callBack);
}
