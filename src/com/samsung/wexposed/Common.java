package com.samsung.wexposed;

import android.annotation.TargetApi;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Common {

	public static final String TAG = "WEXposed";
	public static final String MY_PACKAGE_NAME = Common.class.getPackage().getName();  //"com.samsung.wexposed"

	public static final String ACTION_PERMISSIONS = "update_permissions";

	public static final String PREFS = "PrivacySettings";  //setting file name

	public static final String PREF_DEFAULT = "default";  //default settings prefix
	public static final String PREF_WORK = "/work";  //work context prefix
	public static final String PREF_HOME = "/home";  //home context prefix
	public static final String PREF_GYM = "/gym";  //gym context prefix
	public static final String PREF_OUTDOOR = "/outdoor";  //outdoor context prefix

	public static final String PREF_ACTIVE = "/active";  //active settings per app
	public static final String PREF_CONTEXT = "/context";  //context settings prefix per app
	public static final String PREF_SMART = "/smart";  //smart mode prefix per app
	public static final String PREF_ONCE = "/once";  //once settings prefix per app
	public static final String PREF_APP = "/app";  //app settings prefix
	public static final String PREF_ADS = "/ads";  //ads settings prefix

	public static final String PREF_INTERNET = "/internet";  //internet setting
	public static final String PREF_LOCATION = "/location";  //location setting
	public static final String PREF_CONTACTS = "/contacts";  //contacts setting
	public static final String PREF_SENSOR = "/sensor";  //sensor setting
	
	
	public static final String PREF_REVOKEPERMS = "/revoke-perms";
	public static final String PREF_REVOKELIST = "/revoke-list";


}
