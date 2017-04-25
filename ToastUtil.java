package com.jkx4rh.client.tool;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast mToast;

	/**
	 * Toast快速切换
	 */
	public static void showToast(Context context, String msg, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, duration);
		} else {
			mToast.setText(msg);
		}
		mToast.show();
	}

	/**
	 * 立即取消Toast
	 */
	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}
}
