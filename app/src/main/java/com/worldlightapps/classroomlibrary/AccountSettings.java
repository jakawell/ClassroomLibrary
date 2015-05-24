package com.worldlightapps.classroomlibrary;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountSettings {

	public static final String SETTINGS_NAME = "com.worldlightapps.classroomlibrary.AccountSettings";
	public static final String ACCOUNT_ID_SETTING = "current_account_id";
	
	public static void setCurrentAccountId(Context context, String value) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(ACCOUNT_ID_SETTING, value).commit();
	}
	
	public static String getCurrentAccountId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(ACCOUNT_ID_SETTING, "");
	}
}
