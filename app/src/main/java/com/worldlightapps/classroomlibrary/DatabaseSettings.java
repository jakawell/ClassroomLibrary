package com.worldlightapps.classroomlibrary;

import android.content.Context;
import android.content.SharedPreferences;

public class DatabaseSettings {

	public static final String SETTINGS_NAME = "com.worldlightapps.classroomlibrary.DatabaseSettings";
	
	public static final String AZURE_SERVICE_SITE = "https://classroomlibrary.azure-mobile.net/";
	public static final String AZURE_SERVICE_APP_ID = "YMfQsWreNttOYeRfcAjfMYAErAcODj11";
	
	public static final String ISBN_ATTRIBUTE_ID_SETTING = "Isbn_Attr_Id";
	public static final String DATEADDED_ATTRIBUTE_ID_SETTING = "DateAdded_Attr_Id";
	public static final String CHECKEDOUT_ATTRIBUTE_ID_SETTING = "CheckedOut_Attr_Id";
	public static final String CHECKEDBY_ATTRIBUTE_ID_SETTING = "CheckedBy_Attr_Id";
	public static final String TITLE_ATTRIBUTE_ID_SETTING = "Title_Attr_Id";
	public static final String AUTHOR_ATTRIBUTE_ID_SETTING = "Author_Attr_Id";
	
	public static void setIsbnAttributeId(Context context, String value) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(ISBN_ATTRIBUTE_ID_SETTING, value).commit();
	}

	public static void setDateAddedAttributeId(Context context, String value) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(DATEADDED_ATTRIBUTE_ID_SETTING, value).commit();
	}
	
	public static void setCheckedOutAttributeId(Context context, String value) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(CHECKEDOUT_ATTRIBUTE_ID_SETTING, value).commit();
	}
	
	public static void setCheckedByAttributeId(Context context, String value) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(CHECKEDBY_ATTRIBUTE_ID_SETTING, value).commit();
	}
	
	public static void setTitleAttributeId(Context context, String value) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(TITLE_ATTRIBUTE_ID_SETTING, value).commit();
	}
	
	public static void setAuthorAttributeId(Context context, String value) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(AUTHOR_ATTRIBUTE_ID_SETTING, value).commit();
	}
	
	public static String getIsbnAttributeId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(ISBN_ATTRIBUTE_ID_SETTING, "");
	}
	
	public static String getDateAddedAttributeId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(DATEADDED_ATTRIBUTE_ID_SETTING, "");
	}

	public static String getCheckedOutAttributeId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(CHECKEDOUT_ATTRIBUTE_ID_SETTING, "");
	}
	
	public static String getCheckedByAttributeId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(CHECKEDBY_ATTRIBUTE_ID_SETTING, "");
	}
	
	public static String getTitleAttributeId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(TITLE_ATTRIBUTE_ID_SETTING, "");
	}
	
	public static String getAuthorAttributeId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(AUTHOR_ATTRIBUTE_ID_SETTING, "");
	}
}
