package com.samsung.wexposed;


import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;

import com.samsung.wexposed.hooks.PackagePermissions;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.util.Locale;

public class XposedMod implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	public static final String this_package = XposedMod.class.getPackage().getName();
	public static XSharedPreferences prefs;

	@Override
	public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
		loadPrefs();

		PackagePermissions.initHooks();
//		Activities.hookActivitySettings();
	}


	@SuppressLint("NewApi")
	private void setConfigurationLocale(Configuration config, Locale loc) {
		config.locale = loc;
		if (Build.VERSION.SDK_INT >= 17) {
			// Don't use setLocale() in order not to trigger userSetLocale
			config.setLayoutDirection(loc);
		}
	}


	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		prefs.reload();

		if (this_package.equals(lpparam.packageName)) {
			findAndHookMethod("com.samsung.wexposed.XposedModActivity",
					lpparam.classLoader, "isModActive", XC_MethodReplacement.returnConstant(true));
		}
	}

	public static void loadPrefs() {
		prefs = new XSharedPreferences(Common.MY_PACKAGE_NAME, Common.PREFS);
		prefs.makeWorldReadable();
	}

	public static boolean isActive(String packageName) {
		return prefs.getBoolean(packageName + Common.PREF_ACTIVE, false);
	}

	public static boolean isActive(String packageName, String sub) {
		return prefs.getBoolean(packageName + Common.PREF_ACTIVE, false) && prefs.getBoolean(packageName + sub, false);
	}
}
