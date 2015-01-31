
package com.samsung.wexposed.hooks;

import static de.robv.android.xposed.XposedHelpers.findClass;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Collections;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ActivityHooks{

   	public static void hook(LoadPackageParam lpparam) {
//   		final PackageManager pm = lpparam.getClass();
//   		getLaunchIntentForPackage(lpparam.packageName)

   		XposedBridge.log("   lpparam.appinfo = " + lpparam.appInfo.className);
   		
        try {
//            final Class<?> mainActivity = findClass("android.app.Activity", lpparam.classLoader);
//            
//            XposedBridge.hookAllMethods(mainActivity, "onCreate", new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param){
//                    
//                }
//
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws
//                        Throwable {
//
//                    XposedBridge.log("   Hooked method: " + param.method);
//                    
//                    String hostPackageName = AndroidAppHelper.currentPackageName();
//                    XposedBridge.log("   HostPackage Name: " + hostPackageName);
////                    if(! "com.samsung.xposedexp".equals(hostPackageName)){
////                    Intent mIntent = new Intent(((ContextWrapper) param.thisObject).getBaseContext(), MyService.class);
//                    Intent mIntent = new Intent(); 
//                    mIntent.setClassName("com.samsung.xposedexp", "com.samsung.xposedexp.MyService"); 
//                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    ((Activity) param.thisObject).startService(mIntent);
//                    XposedBridge.log("=======");
////                    }
//                    
////                    Intent settingsActivity = new Intent(((ContextWrapper) param.thisObject).getBaseContext(), 
////                            findClass("com.samsung.xposedexp.Dialog", lpparam.classLoader));
////                    ((Activity) param.thisObject).startActivity(settingsActivity);
//                }
//            });

        } catch (Throwable e) {
			XposedBridge.log(e);
		}
    }
   	
//
//	public static void loadMainActivities() {
//		final PackageManager pm = getPackageManager();
//		pm.getLaunchIntentForPackage(packageName)
//
//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
//        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
//
//        for (ResolveInfo temp : appList) {
//
//            Log.v("my logs", "package and activity name = "
//                    + temp.activityInfo.packageName + "    "
//                    + temp.activityInfo.name);
//
//
//        }
//    }
}
