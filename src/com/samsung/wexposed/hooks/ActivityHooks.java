package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findClass;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.samsung.wexposed.Common;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.util.Hashtable;

public class ActivityHooks {

//	public static Hashtable<String, String> htLaunchActivity = new Hashtable<String, String>();

	public static void hook(LoadPackageParam lpparam) {
		// final PackageManager pm = lpparam.getClass();
		// getLaunchIntentForPackage(lpparam.packageName)

		// XposedBridge.log("   lpparam.appinfo = " +
		// lpparam.appInfo.className);

		try {
			final Class<?> activity = findClass("android.app.Activity", lpparam.classLoader);

			XposedBridge.hookAllMethods(activity, "onCreate", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) {

					String activity = param.thisObject.getClass().getName();
					String hostPackageName = AndroidAppHelper.currentPackageName();
					Context mContext = (Activity) param.thisObject;

					final PackageManager pm = mContext.getPackageManager();
					Intent launchIntent = pm.getLaunchIntentForPackage(hostPackageName);

					if (launchIntent != null && activity.equals(launchIntent.resolveActivity(pm).getClassName())) {
						XposedBridge.log("=== Launcher Activity Started ===");

						Intent resultIntent = new Intent();
						resultIntent.addCategory(Common.INTENT_CATEGORY);
						resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //necessary when starting activity from service

						resultIntent.setAction(Common.INTENT_START_EVENT);
						resultIntent.putExtra(Common.INTENT_PACKAGE_NAME, hostPackageName);
						resultIntent.putExtra(Common.INTENT_EVENT, Common.INTENT_START_EVENT);
						mContext.sendBroadcast(resultIntent);

						XposedBridge.log("Sent intent: " + resultIntent.toString());
					}
				}

				// @Override
				// protected void afterHookedMethod(MethodHookParam param)
				// throws
				// Throwable {
				//
				// XposedBridge.log("   Hooked method: " + param.method);
				//
				// String hostPackageName =
				// AndroidAppHelper.currentPackageName();
				// XposedBridge.log("   HostPackage Name: " + hostPackageName);
				// // if(! "com.samsung.xposedexp".equals(hostPackageName)){
				// // Intent mIntent = new Intent(((ContextWrapper)
				// param.thisObject).getBaseContext(), MyService.class);
				// Intent mIntent = new Intent();
				// mIntent.setClassName("com.samsung.xposedexp",
				// "com.samsung.xposedexp.MyService");
				// mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// ((Activity) param.thisObject).startService(mIntent);
				// XposedBridge.log("=======");
				// // }
				//
				// // Intent settingsActivity = new Intent(((ContextWrapper)
				// param.thisObject).getBaseContext(),
				// // findClass("com.samsung.xposedexp.Dialog",
				// lpparam.classLoader));
				// // ((Activity)
				// param.thisObject).startActivity(settingsActivity);
				// }
			});

//			XposedBridge.hookAllMethods(activity, "onPause", new XC_MethodHook() {
//
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) {
//					
//					String hostPackageName = AndroidAppHelper.currentPackageName();
//					Context mContext = (Activity) param.thisObject;
//					
//					Intent resultIntent = new Intent();
//					resultIntent.addCategory(Common.INTENT_CATEGORY);
//					resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //necessary when starting activity from service
//
//					resultIntent.setAction(Common.INTENT_ONCE_EVENT);
//					resultIntent.putExtra(Common.INTENT_PACKAGE_NAME, hostPackageName);
//					resultIntent.putExtra(Common.INTENT_EVENT, Common.INTENT_ONCE_EVENT);
//					mContext.sendBroadcast(resultIntent);
//
//					XposedBridge.log("Sent intent: " + resultIntent.toString());
//					
//				}
//			});

		} catch (Throwable e) {
			XposedBridge.log(e);
		}

	}

}
