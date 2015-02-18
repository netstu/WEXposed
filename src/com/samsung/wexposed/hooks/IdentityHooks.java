package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findClass;
import android.app.AndroidAppHelper;

import com.samsung.wexposed.Common;
import com.samsung.wexposed.XposedMod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class IdentityHooks {
	public static void hook(LoadPackageParam lpparam) {
		try {
			final Class<?> telMgr = findClass("android.telephony.TelephonyManager", lpparam.classLoader);

			XposedBridge.hookAllMethods(telMgr, "getDeviceId", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_IDENTITY))
						param.setResult("FakeIdentity");
				}
			});

			XposedBridge.hookAllMethods(telMgr, "getSimSerialNumber", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_IDENTITY))
						param.setResult("FakeIdentity");
				}
			});

			XposedBridge.hookAllMethods(telMgr, "getSubscriberId", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_IDENTITY))
						param.setResult("FakeIdentity");
				}
			});

			final Class<?> wifiInfo = findClass("android.net.wifi.WifiInfo", lpparam.classLoader);

			XposedBridge.hookAllMethods(wifiInfo, "getMacAddress", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_IDENTITY))
						param.setResult("FakeIdentity");
				}
			});

			final Class<?> secSettings = findClass("android.provider.Settings.Secure", lpparam.classLoader);

			findAndHookMethod(secSettings, "getString", android.content.ContentResolver.class, java.lang.String.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					if ("android_id".equals(param.args[1])) {
						// return a fake string only if the "String name"
						// parameter is equal to "android_id"
						String packageName = AndroidAppHelper.currentPackageName();
						XposedMod.prefs.reload();
						if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_IDENTITY))
							param.setResult("FakeIdentity");
					}
				}
			});

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
