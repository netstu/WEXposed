package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.app.AndroidAppHelper;
import android.location.Location;

import com.samsung.wexposed.Common;
import com.samsung.wexposed.XposedMod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class LocationHooks {

	public static void hook(LoadPackageParam lpparam) {

		try {
			findAndHookMethod("android.location.LocationManager", lpparam.classLoader, "getLastKnownLocation", String.class, new XC_MethodHook() {

				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {

					String packageName = AndroidAppHelper.currentPackageName();

					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_LOCATION)) {
						if (param.getResult() != null) {

							XposedBridge.log("   Result location: " + param.getResult().toString());
							Location loc = (Location) param.getResult();
							// set location to San Antonio
							loc.setLatitude(29.4241219);
							loc.setLongitude(-98.4936282);

							XposedBridge.log("==> [android] Result location is changed to: " + param.getResult().toString());
						}
					}
				}
			});

			findAndHookMethod("android.location.LocationManager.ListenerTransport", lpparam.classLoader, "onLocationChanged", Location.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_LOCATION)) {

						Location loc = (Location) param.args[0];
						// set location to San Antonio
						loc.setLatitude(29.4241219);
						loc.setLongitude(-98.4936282);

						XposedBridge.log("==> [android] Result location is changed to: " + param.args[0]);

					}
				}
			});

			/****************************** BEGIN GMS (NOT USED) ********************/

			// findAndHookMethod("com.google.android.gms.location.LocationClient"
			// , lpparam.classLoader, "getLastLocation", new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam
			// param) throws Throwable {
			// XposedBridge.log("   calling package: " + lpparam.appInfo);
			// XposedBridge.log("   Hooked method: " + param.method); //
			// XposedBridge.log("   Method Args: " + param.args[0].toString());
			//
			// }
			//
			// @Override
			// protected void afterHookedMethod(MethodHookParam param)
			// throws Throwable {
			// if (param.getResult() != null) {
			// XposedBridge.log("   Result location: " +
			// param.getResult().toString());
			// Location loc = (Location)
			// param.getResult(); // set location to
			// // San Antonio
			// loc.setLatitude(29.4241219);
			// loc.setLongitude(-98.4936282);
			// XposedBridge.log("==> [gms] Result location is changed to: " +
			// param.getResult().toString());
			// }
			// }
			// });
			//
			// findAndHookMethod("com.google.android.gms.internal.ly$a",
			// lpparam.classLoader, "handleMessage", android.os.Message.class,
			// new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam
			// param) throws Throwable {
			// XposedBridge.log("   calling package: " + lpparam.appInfo);
			// XposedBridge.log("   Hooked method: " + param.method);
			// XposedBridge.log("   Method Args: " + param.args[0].toString());
			// Location loc = (Location)
			// ((android.os.Message) param.args[0]).obj;
			// // set location to San Antonio
			// loc.setLatitude(29.4241219);
			// loc.setLongitude(-98.4936282);
			// XposedBridge.log("===> [gms] Changed location to: " +
			// param.args[0].toString());
			// }
			//
			// @Override
			// protected void afterHookedMethod(MethodHookParam param)
			// throws Throwable {
			// }
			// });

			/********************** END OF GMS ***********************/

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
