package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findClass;

import android.app.AndroidAppHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.google.android.gms.ads.search.SearchAdRequest.Builder;
import com.samsung.wexposed.Common;
import com.samsung.wexposed.XposedMod;

public class AdsHooks {
	public static void hook(LoadPackageParam lpparam) {
		try {
			
			/*------------------------------ com.google.android.gms.ads --------------------------*/

//			Class<?> adview = findClass("com.google.android.gms.ads.AdView", lpparam.classLoader);
//			XposedBridge.hookAllMethods(adview, "loadAd", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("Detected com.google.android.gms.ads.AdView.loadAd in " + lpparam.appInfo.packageName);
//					XposedBridge.log("   Hooked method: " + param.method);
//
//					Exception ex = new Exception();
//					// XposedBridge.log("   calling trace: " +
//					// Log.getStackTraceString(ex));
//				}
//			});
//
//			Class<?> interstitialAd = findClass("com.google.android.gms.ads.InterstitialAd", lpparam.classLoader);
//			XposedBridge.hookAllMethods(interstitialAd, "loadAd", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("Detected com.google.android.gms.ads.InterstitialAd.loadAd in " + lpparam.appInfo.packageName);
//					XposedBridge.log("   Hooked method: " + param.method);
//
//					Exception ex = new Exception();
//					// XposedBridge.log("   calling trace: " +
//					// Log.getStackTraceString(ex));
//				}
//			});
//
//			// Class<?> searchAdView =
//			// findClass("com.google.android.gms.ads.search.SearchAdView",
//			// lpparam.classLoader);
//			// XposedBridge.hookAllMethods(searchAdView, "loadAd",
//			// new XC_MethodHook() {
//			// @Override
//			// protected void beforeHookedMethod(MethodHookParam
//			// param) throws Throwable {
//			// XposedBridge
//			// .log("Detected com.google.android.gms.ads.search.SearchAdView.loadAd in "
//			// +
//			// lpparam.appInfo.packageName);
//			// XposedBridge.log("   Hooked method: " + param.method);
//			// // SearchAdRequest sar = (SearchAdRequest) param.args[0];
//			//
//			//
//			// // Method[] mss = param.args[0].getClass().getDeclaredMethods();
//			// // for (Method ms:mss) {XposedBridge.log("\t\t\t" + ms + "\n");}
//			//
//			//
//			// Method m =
//			// param.args[0].getClass().getDeclaredMethod("getQuery");
//			// String s = (String)m.invoke(param.args[0]);
//			// XposedBridge.log("   search query: " + s);
//			//
//			// // SearchAdRequest new_sar = new SearchAdRequest.Builder()
//			// // .setQuery("Banks/ATM Las Vegas United States").build();
//			// // XposedBridge.log("   new query: " + new_sar.getQuery());
//			// // param.args[0] = new_sar;
//			//
//			// Exception ex = new Exception();
//			// XposedBridge.log("   calling trace: " +
//			// Log.getStackTraceString(ex));
//			// }
//			// });
//
//			Class<?> arBuilder = findClass("com.google.android.gms.ads.AdRequest.Builder", lpparam.classLoader);
//			XposedBridge.hookAllMethods(arBuilder, "build", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//			XposedBridge.hookAllMethods(arBuilder, "setContentUrl", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//			XposedBridge.hookAllMethods(arBuilder, "setBirthday", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//			XposedBridge.hookAllMethods(arBuilder, "setGender", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//			XposedBridge.hookAllMethods(arBuilder, "setLocation", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//			XposedBridge.hookAllMethods(arBuilder, "addKeyword", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//			XposedBridge.hookAllMethods(arBuilder, "addNetworkExtrasBundle", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//			XposedBridge.hookAllMethods(arBuilder, "addNetworkExtras", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});
//
//			/*--------------------------------- Protect our children --------------------------------*/
//
//			XposedBridge.hookAllMethods(arBuilder, "tagForChildDirectedTreatment", new XC_MethodHook() {
//				@Override
//				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//					XposedBridge.log("   Hooked method: " + param.method);
//				}
//			});

			/*--------------------------------- SearchAd --------------------------------*/

			Class<?> sarBuilder = findClass("com.google.android.gms.ads.search.SearchAdRequest.Builder", lpparam.classLoader);
			XposedBridge.hookAllMethods(sarBuilder, "setQuery", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String packageName = AndroidAppHelper.currentPackageName();
					XposedMod.prefs.reload();
					if (!XposedMod.isActive(packageName, Common.PREF_ADS + Common.PREF_LOCATION)) {
//					XposedBridge.log("   Hooked SearchAds: " + param.method);
//					XposedBridge.log("   Query string: " + param.args[0]);
						param.args[0] = "Hotels San Francisco United States";
					}
				}
			});

			/*------------------------------ com.google.ads --------------------------*/

			// Class<?> gAdView = findClass("com.google.ads.AdView",
			// lpparam.classLoader);
			// XposedBridge.hookAllMethods(gAdView, "loadAd",
			// new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam
			// param) throws Throwable {
			// XposedBridge
			// .log("Detected com.google.ads.AdView.loadAd in "
			// +
			// lpparam.appInfo.packageName);
			// XposedBridge.log("   Hooked method: " + param.method);
			//
			// Exception ex = new Exception();
			// XposedBridge.log("   calling trace: " +
			// Log.getStackTraceString(ex));
			// }
			// });
			//
			// Class<?> gInterstitialAd =
			// findClass("com.google.ads.InterstitialAd",
			// lpparam.classLoader);
			// XposedBridge.hookAllMethods(gInterstitialAd, "loadAd",
			// new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam
			// param) throws Throwable {
			// XposedBridge
			// .log("Detected com.google.ads.InterstitialAd.loadAd in "
			// +
			// lpparam.appInfo.packageName);
			// XposedBridge.log("   Hooked method: " + param.method);
			//
			// Exception ex = new Exception();
			// XposedBridge.log("   calling trace: " +
			// Log.getStackTraceString(ex));
			// }
			// });

			// Class<?> adview = findClass("com.mopub.mobileads.MoPubView",
			// lpparam.classLoader);
			// XposedBridge.hookAllMethods(adview,
			// "loadAd", new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam
			// param) throws Throwable {
			// XposedBridge.log("Detected MoPub loadAd in " +
			// lpparam.appInfo.packageName);
			//
			// // Exception ex = new Exception();
			// // XposedBridge.log("   calling trace: " +
			// // Log.getStackTraceString(ex));
			// }
			//
			// @Override
			// protected void afterHookedMethod(MethodHookParam param)
			// throws Throwable {
			// }
			// });

			// findAndHookMethod("com.mopub.mobileads.MoPubView",
			// lpparam.classLoader, "loadAd", new XC_MethodReplacement() {
			// @Override
			// protected Object replaceHookedMethod(MethodHookParam
			// param) throws Throwable {
			// XposedBridge.log("Blocked MoPub loadAd in " +
			// lpparam.appInfo.packageName);
			// return null;
			// }
			// });

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
