package com.samsung.wexposed;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import com.samsung.wexposed.hooks.ActivityHooks;
import com.samsung.wexposed.hooks.AdsHooks;
import com.samsung.wexposed.hooks.ContactsHooks;
import com.samsung.wexposed.hooks.IdentityHooks;
import com.samsung.wexposed.hooks.LocationHooks;
import com.samsung.wexposed.hooks.PackagePermissions;
import com.samsung.wexposed.hooks.SensorHooks;
import com.samsung.wexposed.hooks.WifiHooks;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedMod implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	public static final String this_package = XposedMod.class.getPackage().getName();
	public static XSharedPreferences prefs;

	@Override
	public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
		loadPrefs();
		PackagePermissions.initHooks();
	}

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		prefs.reload();

		if (this_package.equals(lpparam.packageName)) {
			findAndHookMethod(this_package + ".XposedModActivity", lpparam.classLoader, "isModActive", XC_MethodReplacement.returnConstant(true));
		}

		// if (!XposedMod.isActive(lpparam.packageName)) return;

		XposedBridge.log("Loaded Package: " + lpparam.packageName);
		if (Common.UI_PACKAGE_NAME.equals(lpparam.packageName) || Common.MY_PACKAGE_NAME.equals(lpparam.packageName))
			return;

		ActivityHooks.hook(lpparam);

		// XposedBridge.log("package: " + lpparam.packageName +
		// "==> launch activity: " + htLaunchActivity.get(lpparam.packageName));

		 LocationHooks.hook(lpparam);
//		 InternetHooks.hook(lpparam);
		 ContactsHooks.hook(lpparam);
		 SensorHooks.hook(lpparam);
//		 CalendarHooks.hook(lpparam);
		 WifiHooks.hook(lpparam);
		 IdentityHooks.hook(lpparam);
		 AdsHooks.hook(lpparam);

	}

	// public static HashSet<String> loadDetectedAds()
	// {
	// Detector adsDetector = new Detector(context, readAdList("adlist.txt"));
	// try {
	// HashSet<String> adsEntries = adsDetector.getAppInfo(pkgName).ads;
	// if (adsEntries.size() == 0)
	// adsEntries.add(getString(R.string.res_noentries));
	// for (String a : adsEntries) {
	// contents.append('\n');
	// contents.append(a);
	// }
	// } catch (NameNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static void loadPrefs() {
		prefs = new XSharedPreferences(Common.UI_PACKAGE_NAME, Common.PREFS);
		prefs.makeWorldReadable();
	}

	public static boolean isActive(String packageName) {
		return prefs.getBoolean(packageName + Common.PREF_ACTIVE, false);
	}

	public static boolean isActive(String packageName, String sub) {
		if (prefs.getBoolean(packageName + Common.PREF_ACTIVE, false))
			return prefs.getBoolean(packageName + sub, prefs.getBoolean(Common.PREF_DEFAULT + sub, true));
		return prefs.getBoolean(Common.PREF_DEFAULT + sub, true);
	}

}
