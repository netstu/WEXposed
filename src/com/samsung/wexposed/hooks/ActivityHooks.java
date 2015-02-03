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
import com.samsung.wexposed.WEXposedService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.util.Hashtable;

public class ActivityHooks {

	public static Hashtable<String, String> htLaunchActivity = new Hashtable<String, String>();

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
					// String pkgName = AndroidAppHelper.currentPackageName();
					// File file = new File(Environment.getDataDirectory(),
					// "data/" + pkgName + "/launcher_activity.tmp");
					// if (!file.exists()) {
					// try {
					// file.mkdirs();
					// file.createNewFile();
					// XposedBridge.log("### la file created: " +
					// Environment.getDataDirectory().toString() + "/data/" +
					// pkgName + "/launcher_activity.tmp");
					// } catch (IOException e) {
					// XposedBridge.log(e);
					// }
					// }
					//
					// Context mContext = (Activity) param.thisObject;
					// File outputDir = mContext.getCacheDir();
					// XposedBridge.log("=== onCreate cacheDir: " +
					// outputDir.toString());

					String hostPackageName = AndroidAppHelper.currentPackageName();

					// Context mContext = ((ContextWrapper)
					// param.thisObject).getBaseContext();
					Context mContext = (Activity) param.thisObject;
//					Intent msgIntent = new Intent(mContext, WEXposedService.class);
//					msgIntent.putExtra(WEXposedService.PARAM_IN_MSG, hostPackageName);
//					mContext.startService(msgIntent);
//					XposedBridge.log("   service started: " + msgIntent.toString());
//
//					// set up a broadcast receiver
//					IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
//					filter.addCategory(Intent.CATEGORY_DEFAULT);
//					mContext.registerReceiver(new ResponseReceiver(), filter);
					
					final PackageManager pm = mContext.getPackageManager();
					Intent launchIntent = pm.getLaunchIntentForPackage(hostPackageName);
					XposedBridge.log("=== launchIntent: " + launchIntent.resolveActivity(pm).getClassName());
					XposedBridge.log("=== activity: " + activity);
					// check if the received result matches
					// param.thisObject.getClass()
					
					if(activity.equals(launchIntent.resolveActivity(pm).getClassName())){
						XposedBridge.log("=== Launcher Activity Started ===");
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

			// XposedBridge.hookAllMethods(activity, "onDestroy", new
			// XC_MethodHook() {
			//
			// @Override
			// protected void beforeHookedMethod(MethodHookParam param) {
			//
			// Exception ex = new Exception();
			// String s = Log.getStackTraceString(ex);
			// XposedBridge.log("---------------------------");
			// XposedBridge.log("calling stack: " + s);
			//
			// XposedBridge.log("### Activity Class: " +
			// param.thisObject.getClass());
			// String pkgName = AndroidAppHelper.currentPackageName();
			// File file = new File(Environment.getDataDirectory(), "data/" +
			// pkgName + "/launcher_activity.tmp");
			// file.delete();
			// XposedBridge.log("### la file deleted: " +
			// Environment.getDataDirectory().toString() + "/data/" + pkgName +
			// "/launcher_activity.tmp");
			//
			//
			// Context mContext = (Activity) param.thisObject;
			// File outputDir = mContext.getCacheDir();
			//
			// XposedBridge.log("=== onStop cacheDir: " + outputDir.toString());
			//
			// }
			// });

		} catch (Throwable e) {
			XposedBridge.log(e);
		}

	}

	public static class ResponseReceiver extends BroadcastReceiver {
		public final static String ACTION_RESP = Common.MY_PACKAGE_NAME + ".intent.action.MESSAGE_PROCESSED";

		@Override
		public void onReceive(Context context, Intent intent) {
			String pkgName = intent.getStringExtra(WEXposedService.PARAM_IN_MSG);
			String launchActivity = intent.getStringExtra(WEXposedService.PARAM_OUT_MSG);
			// Log.d(Common.TAG, "package =" + pkgName + ", launch activity = "
			// + launchActivity);

			htLaunchActivity.put(pkgName, launchActivity);
			Log.d(Common.TAG, "[XposedMod] htLaunchActivity Filled: " + htLaunchActivity.toString());

		}
	}

}
