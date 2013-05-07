package com.android.nitelights.login;

import android.content.Context;
import android.content.SharedPreferences;

abstract public class PrefsHelper {

	private static String appName;
	private static Context context;

	public static void setup(Context context, String appName) {
		PrefsHelper.context = context;
		PrefsHelper.appName = appName;
	}
	
	public static SharedPreferences getPreferences() {
		return context.getSharedPreferences(appName, Context.MODE_PRIVATE);
	}

}
