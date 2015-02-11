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

public class ContactsHooks {

	public static void hook(LoadPackageParam lpparam) {

		try {
			final Class<?> cResolver = findClass("android.content.ContentResolver", lpparam.classLoader);

			// findAndHookMethod(cResolver, "query",
			// android.net.Uri.class,java.lang.String[].class,
			// java.lang.String.class,java.lang.String[].class,java.lang.String.class,
			// new XC_MethodHook() {

			XposedBridge.hookAllMethods(cResolver, "query", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

					String packageName = AndroidAppHelper.currentPackageName();
					String likeStmt = "";

					XposedMod.prefs.reload();
					if (XposedMod.isActive(packageName, Common.PREF_APP + Common.PREF_CONTACTS))
						likeStmt = " NOT LIKE '*%' "; // show all normal
													  // contacts
					else
						likeStmt = " LIKE '*%' "; // show all hidden contacts

					Uri uri = (Uri) param.args[0];

					boolean flag = false;

					if (param.args[1] != null) {
						String[] pNames = (String[]) param.args[1];
						for (int i = 0; i < pNames.length; i++) {
							if (pNames[i].equals("name") || pNames[i].equals("display_name"))
								flag = true;
						}

						if (param.args[2] != null) {
							String selection = (String) param.args[2];
//							XposedBridge.log("   selection=" + selection);

							// //
							// if((uri.getHost()+uri.getPath()).equals("com.android.contacts/profile")
							// ||
							// if(uri.getHost().equals("com.android.contacts")
							// ||
							// uri.getHost().equals("call_log")){

							if (flag) {
								String sName = "";
								if (uri.getHost().equals("com.android.contacts"))
									sName = "display_name";
								if (uri.getHost().equals("call_log"))
									sName = "name";

								if (selection == null || selection.isEmpty())
									selection = sName + likeStmt;
								else
									selection += " AND " + sName + likeStmt;

								param.args[2] = selection;
								XposedBridge.log("   new selection=" + param.args[2]);
							}
						}

					}
				}
			});

			/*---------------------------------------------------------------------*/
			//
			// final Class<?> db =
			// findClass("android.database.sqlite.SQLiteQueryBuilder",
			// lpparam.classLoader);
			// XposedBridge.hookAllMethods(db, "query", new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam param) throws
			// Throwable {
			// XposedBridge.log("   Hooked method: " + param.method);
			// // XposedBridge.log("   SQL: " + (String) param.args[0]);
			// }
			// });
			//
			// /*---------------------------------------------------------------------*/
			//
			// final Class<?> cLoader =
			// findClass("android.content.CursorLoader", lpparam.classLoader);
			//
			// XposedBridge.hookAllConstructors(cLoader, new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam param) throws
			// Throwable {
			// XposedBridge.log("   Hooked method: " + param.method);
			// if (param.args.length > 1) {
			// XposedBridge.log("   uri=" + param.args[1]);
			// }
			// }
			// });
			//
			// XposedBridge.hookAllMethods(cLoader, "setUri", new
			// XC_MethodHook() {
			// protected void beforeHookedMethod(MethodHookParam param) throws
			// Throwable {
			//
			// XposedBridge.log("   Hooked method: " + param.method);
			// XposedBridge.log("   uri=" + param.args[0]);
			// }
			// });

			/*---------------------------------------------------------------------*/

			// final Class<?> scLoader =
			// findClass("android.support.v4.content.CursorLoader",
			// lpparam.classLoader);
			//
			// XposedBridge.hookAllConstructors(scLoader, new XC_MethodHook() {
			// @Override
			// protected void beforeHookedMethod(MethodHookParam param) throws
			// Throwable {
			// XposedBridge.log("   Hooked method: " + param.method);
			// if (param.args[1] != null) {
			// XposedBridge.log("   uri=" + param.args[1]);
			// }
			// }
			// });
			//
			// XposedBridge.hookAllMethods(scLoader, "setUri", new
			// XC_MethodHook() {
			// protected void beforeHookedMethod(MethodHookParam param) throws
			// Throwable {
			//
			// XposedBridge.log("   Hooked method: " + param.method);
			// XposedBridge.log("   uri=" + param.args[0]);
			// }
			// });

		} catch (Throwable e) {
			XposedBridge.log(e);
		}
	}
}
