package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findClass;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.app.AndroidAppHelper;
import android.database.Cursor;
import android.net.Uri;

import com.samsung.wexposed.Common;
import com.samsung.wexposed.XposedMod;

public class CalendarHooks {

	public static void hook(LoadPackageParam lpparam) {

		try {
			final Class<?> cResolver = findClass("android.content.ContentResolver", lpparam.classLoader);

			XposedBridge.hookAllMethods(cResolver, "query", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

					String packageName = AndroidAppHelper.currentPackageName();

					XposedBridge.log("Calendar Hooks in " + packageName);

					XposedMod.prefs.reload();
					Uri uri = (Uri) param.args[0];
					if (packageName.equals("com.android.calendar") && uri.getHost().equals("com.android.calendar"))
						if (!XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_CALENDAR))
							param.setResult(null);
				}
			});

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
