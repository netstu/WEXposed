package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findClass;
import android.app.AndroidAppHelper;
import android.net.Uri;

import com.samsung.wexposed.Common;
import com.samsung.wexposed.XposedMod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class WifiHooks {
	public static void hook(LoadPackageParam lpparam) {
		try {
			final Class<?> wifi = findClass("android.net.wifi.WifiInfo", lpparam.classLoader);

			XposedBridge.hookAllMethods(wifi, "getSSID", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI))
						param.setResult("FakeWifi"); // change the wifi ssid to
						                             // a predefined string
				}
			});

			XposedBridge.hookAllMethods(wifi, "getBSSID", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI)) {
						param.setResult("FakeWifi");
					}
				}
			});

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
