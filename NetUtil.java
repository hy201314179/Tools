package com.jkx4rh.client.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/**
	 * 判断网络连接方式，WIFI，3G;
	 * 
	 * @return true WIFI ; false 3G
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo networkInfo = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null) {
				return networkInfo.isConnected();
			}
		}
		return false;
	}
	
	/**
	 * 描述：检查是否连接网络
	 * **/
	public static boolean isConnectingToInternet(Context context) {
		
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo[] networkInfos = manager.getAllNetworkInfo();
			if (networkInfos != null) {
				for (NetworkInfo networkInfo : networkInfos) {
					if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
