package com.credithc.libpermissionhelper;

/**
 * @data: 2019/9/28 0028
 * @author: liyong
 * @desc: class description
 */
public interface PermissionProxy<T> {
	void grant(T activity, int requestCode, String[] permissions);

	void deny(T activity, int requestCode, String[] permissions);

	void rationale(T activity, int requestCode, String[] permissions);
}
