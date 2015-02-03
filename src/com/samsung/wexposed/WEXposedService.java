package com.samsung.wexposed;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;

import com.samsung.wexposed.hooks.ActivityHooks.ResponseReceiver;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class WEXposedService extends Service {
	private static final String TAG = "WEXposedService";
	private static Hashtable<String, String> htLaunchActivity = new Hashtable<String, String>();
	public static final String PARAM_IN_MSG = "pkgName";
	public static final String PARAM_OUT_MSG = "launchActivity";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		final PackageManager pm = getPackageManager();
		
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo temp : appList)
        	htLaunchActivity.put(temp.activityInfo.packageName, temp.activityInfo.name);
        
        Log.d(TAG, "htLaunchActivity Filled: " + htLaunchActivity.toString());
		
	}

	@Override
	public void onDestroy() {
//		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(Common.TAG, "onDestroy");
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Log.d(TAG, "onStart");
		String pkgName = intent.getStringExtra(PARAM_IN_MSG);
		String launchActivity = htLaunchActivity.get(pkgName);
		
//		Log.d(Common.TAG, "package =" + pkgName + ", launch activity = " + launchActivity);
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(PARAM_IN_MSG, pkgName);
		broadcastIntent.putExtra(PARAM_OUT_MSG, launchActivity);
		sendBroadcast(broadcastIntent);
		
//		Intent mIntent = new Intent(this, Dialog.class);
//		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //necessary when starting activity from service
////		mIntent.setAction(Intent.ACTION_VIEW);
////	    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//		startActivity(mIntent);
	}
}
