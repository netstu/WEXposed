package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

import android.app.AndroidAppHelper;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import com.samsung.wexposed.Common;
import com.samsung.wexposed.XposedMod;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class WifiHooks {
	public static void hook(LoadPackageParam lpparam) {
		try {
			final Class<?> wifiInfo = findClass("android.net.wifi.WifiInfo", lpparam.classLoader);

			XposedBridge.hookAllMethods(wifiInfo, "getSSID", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					XposedBridge.log("Wifi Hooks in " + packageName);
					XposedBridge.log("Wifi Setting = " + XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI));

					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI))
						param.setResult("FakeSSID"); // change the wifi ssid to
						                             // a predefined string
				}
			});

			XposedBridge.hookAllMethods(wifiInfo, "getBSSID", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					XposedBridge.log("Wifi Hooks in " + packageName);
					XposedBridge.log("Wifi Setting = " + XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI));

					XposedBridge.log("Wifi Hooks in " + packageName);
					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI)) {
						param.setResult("FakeBSSID");
					}
				}
			});

			final Class<?> wifiMgr = findClass("android.net.wifi.WifiManager", lpparam.classLoader);
			
//			XposedBridge.hookAllMethods(wifiMgr, "getConnectionInfo", new XC_MethodHook() {
//				@Override
//				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//					String packageName = AndroidAppHelper.currentPackageName();
//					XposedMod.prefs.reload();
//					XposedBridge.log("Wifi Manager getConnectionInfo Hook in " + packageName);
//					XposedBridge.log("SSID = " + ((WifiInfo) param.getResult()).getSSID());
//					WifiInfo wi = ((WifiInfo) param.getResult());
//					
////					callMethod(wi, setSSID, new );
//					
//
////					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI)) {
////						List<ScanResult> res = (List<ScanResult>) param.getResult();
////						for (ScanResult sr : res) {
////							sr.SSID = "FakeSSID";
////							sr.BSSID = "FakeBSSID";
////						}
////						param.setResult(res);
////					}
//				}
//			});
			
			XposedBridge.hookAllMethods(wifiMgr, "getScanResults", new XC_MethodHook() {
				@SuppressWarnings("unchecked")
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					XposedBridge.log("Wifi ScanResult Hook in " + packageName);
					XposedBridge.log("Applied Wifi Setting = " + XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI));

					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_WIFI)) {
						List<ScanResult> res = (List<ScanResult>) param.getResult();
						for (ScanResult sr : res) {
							sr.SSID = "FakeSSID";
							sr.BSSID = "FakeBSSID";
						}
						param.setResult(res);
					}
				}
			});

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
