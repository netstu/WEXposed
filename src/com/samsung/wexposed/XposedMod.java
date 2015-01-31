package com.samsung.wexposed;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.content.ContextWrapper;
import android.content.Intent;

import com.samsung.wexposed.hooks.ActivityHooks;
import com.samsung.wexposed.hooks.ContactsHooks;
import com.samsung.wexposed.hooks.InternetHooks;
import com.samsung.wexposed.hooks.LocationHooks;
import com.samsung.wexposed.hooks.PackagePermissions;
import com.samsung.wexposed.hooks.SensorHooks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class XposedMod implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	public static final String this_package = XposedMod.class.getPackage().getName();
	public static XSharedPreferences prefs;
	public static HashMap<String, String> hm_pkgActivity;

	@Override
	public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
		loadPrefs();
		PackagePermissions.initHooks();
	}

	

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		prefs.reload();

		if (this_package.equals(lpparam.packageName)) {
			findAndHookMethod(this_package + ".XposedModActivity", lpparam.classLoader, 
					"isModActive", XC_MethodReplacement.returnConstant(true));
		}
		
//		if (!XposedMod.isActive(lpparam.packageName)) return;
		
		XposedBridge.log("Loaded Package: " + lpparam.packageName);
//		
//		Intent resultIntent = new Intent();
//		resultIntent.addCategory(Common.INTENT_CATEGORY);
//		resultIntent.putExtra(Common.INTENT_PACKAGE_NAME, lpparam.packageName);
//		resultIntent.putExtra(Common.INTENT_EVENT, Common.INTENT_START_EVENT);
//		sendBroadcast(resultIntent);
		
//		XposedBridge.log("==> Location setting is " + 
//				isActive(lpparam.packageName, Common.PREF_APP + Common.PREF_LOCATION));

		ActivityHooks.hook(lpparam);
		LocationHooks.hook(lpparam);
		InternetHooks.hook(lpparam);
		ContactsHooks.hook(lpparam);
		SensorHooks.hook(lpparam);

	}
	
//	public static HashSet<String> loadDetectedAds()
//	{
//		Detector adsDetector = new Detector(context, readAdList("adlist.txt"));
//        try {
//        	HashSet<String> adsEntries = adsDetector.getAppInfo(pkgName).ads;               
//			if (adsEntries.size() == 0)
//				adsEntries.add(getString(R.string.res_noentries));
//			for (String a : adsEntries) {
//				contents.append('\n');
//				contents.append(a);
//			}
//        } catch (NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//	}

	public static void loadPrefs() {
		prefs = new XSharedPreferences(Common.UI_PACKAGE_NAME, Common.PREFS);
		prefs.makeWorldReadable();
	}
	

	public static boolean isActive(String packageName) {
		return prefs.getBoolean(packageName + Common.PREF_ACTIVE, false);
	}

	public static boolean isActive(String packageName, String sub) {
		if(prefs.getBoolean(packageName + Common.PREF_ACTIVE, false))
			return prefs.getBoolean(packageName + sub, false);
		return prefs.getBoolean(Common.PREF_DEFAULT + sub, true);
	}
}
