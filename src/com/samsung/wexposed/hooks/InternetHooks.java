package com.samsung.wexposed.hooks;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;
import static de.robv.android.xposed.XposedHelpers.findClass;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.location.Location;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.google.android.gms.ads.search.SearchAdRequest;
import com.samsung.wexposed.Common;
import com.samsung.wexposed.XposedMod;

import java.lang.reflect.Method;

public class InternetHooks {
	public static void hook(LoadPackageParam lpparam) {
		try {
			final Class<?> url = findClass("java.net.URL", lpparam.classLoader);
			XposedBridge.hookAllConstructors(url, new XC_MethodHook() {

				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_INTERNET)) {

						if (1 != param.args.length)
							return;
						XposedBridge.log("=== org_url: " + param.args[0]);
						param.args[0] = "";
						XposedBridge.log(" +++ url is changed to: " + param.args[0]);
					}
				}

			});
		} catch (Throwable e) {
			XposedBridge.log(e);
		}

		try {

			findAndHookMethod("android.webkit.WebView", lpparam.classLoader, "loadUrl", String.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_INTERNET)) {

						try {
							param.setResult(null);
						} catch (Throwable t) {
							param.setThrowable(t);
						}

					}
				}
			});
		} catch (Throwable e) {
			XposedBridge.log(e);
		}

		try {
			final Class<?> httpClient = findClass("org.apache.http.impl.client.AbstractHttpClient", lpparam.classLoader);

			// used by com.mopub.mobileads
			XposedBridge.hookAllMethods(httpClient, "execute", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_INTERNET)) {

						try {
							param.setResult(null);
						} catch (Throwable t) {
							param.setThrowable(t);
						}
					}

				}

			});

			/*--------------------------------- Experimental ------------------------------------*/

			// Method m = findMethodBestMatch(httpClient, "execute");
			// XposedBridge.hookMethod(m, new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam param) throws
			// Throwable {
			// // this will be called before the clock was updated by the
			// // original method
			// XposedBridge.log("   calling package: " +
			// lpparam.appInfo.packageName);
			// XposedBridge.log("   HttpClient Class: " + httpClient.getName());
			// XposedBridge.log("   Param Class: " + param.getClass());
			// XposedBridge.log("   Hooked method: " + param.method);
			// XposedBridge.log("   Args: " + param.args[0].toString());
			//
			// Exception ex = new Exception();
			// XposedBridge.log("   calling trace: " +
			// Log.getStackTraceString(ex));
			//
			// }
			//
			// @Override
			// protected void afterHookedMethod(MethodHookParam param) throws
			// Throwable {
			// }
			// });
		} catch (Throwable e) {
			XposedBridge.log(e);
		}

		try {
			final Class<?> aHttpClient = findClass("android.net.http.AndroidHttpClient", lpparam.classLoader);

			XposedBridge.hookAllMethods(aHttpClient, "execute", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_INTERNET)) {

						try {
							param.setResult(null);
						} catch (Throwable t) {
							param.setThrowable(t);
						}
					}

				}
			});

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
