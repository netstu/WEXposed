package com.samsung.wexposed.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.samsung.bin2liu.addetector.Detector;
import com.samsung.wexposed.Common;
import com.samsung.wexposed.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SuppressLint("WorldReadableFiles")
public class ApplicationSettings extends Activity {

	private Switch swtActive;

	private String pkgName;
	private SharedPreferences prefs;
	private Set<String> settingKeys;
	private Map<String, Object> initialSettings;
	private Set<String> disabledPermissions;
	private boolean allowRevoking;
	private Intent parentIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		swtActive = new Switch(this);
		getActionBar().setCustomView(swtActive);
		getActionBar().setDisplayShowCustomEnabled(true);

		setContentView(R.layout.app_settings);

		Intent i = getIntent();
		parentIntent = i;

		prefs = getSharedPreferences(Common.PREFS, Context.MODE_WORLD_READABLE);

		ApplicationInfo app;
		try {
			app = getPackageManager().getApplicationInfo(i.getStringExtra("package"), 0);
			pkgName = app.packageName;
		} catch (NameNotFoundException e) {
			// Close the dialog gracefully, package might have been uninstalled
			finish();
			return;
		}

		// Display app info
		((TextView) findViewById(R.id.app_label)).setText(app.loadLabel(getPackageManager()));
		((TextView) findViewById(R.id.package_name)).setText(app.packageName);
		((ImageView) findViewById(R.id.app_icon)).setImageDrawable(app.loadIcon(getPackageManager()));

		// Update switch of active/inactive tweaks
		if (prefs.getBoolean(pkgName + Common.PREF_ACTIVE, false)) {
			swtActive.setChecked(true);
			findViewById(R.id.viewTweaks).setVisibility(View.VISIBLE);
		} else {
			swtActive.setChecked(false);
			findViewById(R.id.viewTweaks).setVisibility(View.GONE);
		}
		// Toggle the visibility of the lower panel when changed
		swtActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				findViewById(R.id.viewTweaks).setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		// Helper to list all apk folders under /res
		((Button) findViewById(R.id.btnListAds)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationSettings.this);

				ScrollView scrollPane = new ScrollView(ApplicationSettings.this);
				TextView txtPane = new TextView(ApplicationSettings.this);
				StringBuilder contents = new StringBuilder();
				Detector adsDetector = new Detector(ApplicationSettings.this, readAdList("adlist.txt"));
                try {
                	HashSet<String> adsEntries = adsDetector.getAppInfo(pkgName).ads;               
					if (adsEntries.size() == 0)
						adsEntries.add(getString(R.string.res_noentries));
					for (String a : adsEntries) {
						contents.append('\n');
						contents.append(a);
					}
                } catch (NameNotFoundException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }
				txtPane.setText(contents);
				scrollPane.addView(txtPane);
				builder.setView(scrollPane);
				builder.setTitle(R.string.res_title);
				builder.show();
			}
		});
		
		// Update Switches
		((Switch) findViewById(R.id.sw_ads_internet)).setChecked(prefs.getBoolean(pkgName + Common.PREF_ADS + Common.PREF_INTERNET, true));
		((Switch) findViewById(R.id.sw_ads_location)).setChecked(prefs.getBoolean(pkgName + Common.PREF_ADS + Common.PREF_LOCATION, true));
		((Switch) findViewById(R.id.sw_ads_contacts)).setChecked(prefs.getBoolean(pkgName + Common.PREF_ADS + Common.PREF_CONTACTS, true));

		
		((Switch) findViewById(R.id.sw_app_internet)).setChecked(prefs.getBoolean(pkgName + Common.PREF_APP + Common.PREF_INTERNET, true));
		((Switch) findViewById(R.id.sw_app_location)).setChecked(prefs.getBoolean(pkgName + Common.PREF_APP + Common.PREF_LOCATION, true));
		((Switch) findViewById(R.id.sw_app_contacts)).setChecked(prefs.getBoolean(pkgName + Common.PREF_APP + Common.PREF_CONTACTS, true));
		
		((Switch) findViewById(R.id.sw_sensor)).setChecked(prefs.getBoolean(pkgName + Common.PREF_SENSOR, true));


		// Setting for permissions revoking
		allowRevoking = prefs.getBoolean(pkgName + Common.PREF_REVOKEPERMS, false);
		disabledPermissions = prefs.getStringSet(pkgName + Common.PREF_REVOKELIST, new HashSet<String>());

		Button btnPermissions = (Button) findViewById(R.id.btnPermissions);
		btnPermissions.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// set up permissions editor
				try {
					final PermissionSettings permsDlg = new PermissionSettings(ApplicationSettings.this, pkgName, allowRevoking, disabledPermissions);
					permsDlg.setOnOkListener(new PermissionSettings.OnDismissListener() {
						@Override
						public void onDismiss(PermissionSettings obj) {
							allowRevoking = permsDlg.getRevokeActive();
							disabledPermissions.clear();
							disabledPermissions.addAll(permsDlg.getDisabledPermissions());
						}
					});
					permsDlg.display();
				} catch (NameNotFoundException e) {
				}
			}
		});

		settingKeys = getSettingKeys();
		initialSettings = getSettings();
	}

	private Set<String> getSettingKeys() {
		HashSet<String> settingKeys = new HashSet<String>();
		settingKeys.add(pkgName + Common.PREF_ACTIVE);

		settingKeys.add(pkgName + Common.PREF_ADS + Common.PREF_INTERNET);
		settingKeys.add(pkgName + Common.PREF_ADS + Common.PREF_LOCATION);
		settingKeys.add(pkgName + Common.PREF_ADS + Common.PREF_CONTACTS);
		
		settingKeys.add(pkgName + Common.PREF_APP + Common.PREF_INTERNET);
		settingKeys.add(pkgName + Common.PREF_APP + Common.PREF_LOCATION);
		settingKeys.add(pkgName + Common.PREF_APP + Common.PREF_CONTACTS);
		
		settingKeys.add(pkgName + Common.PREF_SENSOR);
		
		settingKeys.add(pkgName + Common.PREF_REVOKEPERMS);
		settingKeys.add(pkgName + Common.PREF_REVOKELIST);
		return settingKeys;
	}

	private Map<String, Object> getSettings() {

		Map<String, Object> settings = new HashMap<String, Object>();
		if (swtActive.isChecked()) {
			settings.put(pkgName + Common.PREF_ACTIVE, true);
			
			if (allowRevoking)
				settings.put(pkgName + Common.PREF_REVOKEPERMS, true);
			
			if (((Switch) findViewById(R.id.sw_ads_internet)).isChecked())
				settings.put(pkgName + Common.PREF_ADS + Common.PREF_INTERNET, true);
			else settings.put(pkgName + Common.PREF_ADS + Common.PREF_INTERNET, false);
			
			if (((Switch) findViewById(R.id.sw_ads_location)).isChecked())
				settings.put(pkgName + Common.PREF_ADS + Common.PREF_LOCATION, true);
			else settings.put(pkgName + Common.PREF_ADS + Common.PREF_LOCATION, false);
			
			if (((Switch) findViewById(R.id.sw_ads_contacts)).isChecked())
				settings.put(pkgName + Common.PREF_ADS + Common.PREF_CONTACTS, true);
			else settings.put(pkgName + Common.PREF_ADS + Common.PREF_CONTACTS, false);
			
			if (((Switch) findViewById(R.id.sw_app_internet)).isChecked())
				settings.put(pkgName + Common.PREF_APP + Common.PREF_INTERNET, true);
			else settings.put(pkgName + Common.PREF_APP + Common.PREF_INTERNET, false);
			
			if (((Switch) findViewById(R.id.sw_app_location)).isChecked())
				settings.put(pkgName + Common.PREF_APP + Common.PREF_LOCATION, true);
			else settings.put(pkgName + Common.PREF_APP + Common.PREF_LOCATION, false);
			
			if (((Switch) findViewById(R.id.sw_app_contacts)).isChecked())
				settings.put(pkgName + Common.PREF_APP + Common.PREF_CONTACTS, true);
			else settings.put(pkgName + Common.PREF_APP + Common.PREF_CONTACTS, false);

			if (((Switch) findViewById(R.id.sw_sensor)).isChecked())
				settings.put(pkgName + Common.PREF_SENSOR, true);
			else settings.put(pkgName + Common.PREF_SENSOR, false);

			if (disabledPermissions.size() > 0)
				settings.put(pkgName + Common.PREF_REVOKELIST, new HashSet<String>(disabledPermissions));
		}
		return settings;
	}

	@Override
	public void onBackPressed() {
		// If form wasn't changed, exit without prompting
		if (getSettings().equals(initialSettings)) {
			finish();
			return;
		}

		// Require confirmation to exit the screen and lose configuration
		// changes
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.settings_unsaved_title);
		builder.setIconAttribute(android.R.attr.alertDialogIcon);
		builder.setMessage(R.string.settings_unsaved_detail);
		builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ApplicationSettings.this.finish();
			}
		});
		builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(RESULT_OK, parentIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_app, menu);
		updateMenuEntries(getApplicationContext(), menu, pkgName);
		return true;
	}

	public static void updateMenuEntries(Context context, Menu menu, String pkgName) {
		if (context.getPackageManager().getLaunchIntentForPackage(pkgName) == null) {
			menu.findItem(R.id.menu_app_launch).setEnabled(false);
			Drawable icon = menu.findItem(R.id.menu_app_launch).getIcon().mutate();
			icon.setColorFilter(Color.GRAY, Mode.SRC_IN);
			menu.findItem(R.id.menu_app_launch).setIcon(icon);
		}

		boolean hasMarketLink = false;
		try {
			PackageManager pm = context.getPackageManager();
			String installer = pm.getInstallerPackageName(pkgName);
			if (installer != null)
				hasMarketLink = installer.equals("com.android.vending") || installer.contains("google");
		} catch (Exception e) {
		}

		menu.findItem(R.id.menu_app_store).setEnabled(hasMarketLink);
		try {
			Resources res = context.createPackageContext("com.android.vending", 0).getResources();
			int id = res.getIdentifier("ic_launcher_play_store", "mipmap", "com.android.vending");
			Drawable icon = res.getDrawable(id);
			if (!hasMarketLink) {
				icon = icon.mutate();
				icon.setColorFilter(Color.GRAY, Mode.SRC_IN);
			}
			menu.findItem(R.id.menu_app_store).setIcon(icon);
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_save) {
			Editor prefsEditor = prefs.edit();
			Map<String, Object> newSettings = getSettings();
			for (String key : settingKeys) {
				Object value = newSettings.get(key);
				if (value == null) {
					prefsEditor.remove(key);
				} else {
					if (value instanceof Boolean) {
//						Log.v(Common.TAG, key + " : " + ((Boolean) value).booleanValue());
						prefsEditor.putBoolean(key, ((Boolean) value).booleanValue());
					} else if (value instanceof Integer) {
						prefsEditor.putInt(key, ((Integer) value).intValue());
					} else if (value instanceof String) {
						prefsEditor.putString(key, (String) value);
					} else if (value instanceof Set) {
						prefsEditor.remove(key);
						// Commit and reopen the editor, as it seems to be
						// bugged when updating a StringSet
						prefsEditor.commit();
						prefsEditor = prefs.edit();
						prefsEditor.putStringSet(key, (Set<String>) value);
					} else {
						// Should never happen
						throw new IllegalStateException("Invalid setting type: " + key + "=" + value);
					}
				}
			}
			prefsEditor.commit();

			// Update saved settings to detect modifications later
			initialSettings = newSettings;

			// Check if in addition to saving the settings, the app should also
			// be killed
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.settings_apply_title);
			builder.setMessage(R.string.settings_apply_detail);
			builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Send the broadcast requesting to kill the app
					Intent applyIntent = new Intent(Common.MY_PACKAGE_NAME + ".UPDATE_PERMISSIONS");
					applyIntent.putExtra("action", Common.ACTION_PERMISSIONS);
					applyIntent.putExtra("Package", pkgName);
					applyIntent.putExtra("Kill", true);
					sendBroadcast(applyIntent, Common.MY_PACKAGE_NAME + ".BROADCAST_PERMISSION");

					dialog.dismiss();
				}
			});
			builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Send the broadcast but not requesting kill
					Intent applyIntent = new Intent(Common.MY_PACKAGE_NAME + ".UPDATE_PERMISSIONS");
					applyIntent.putExtra("action", Common.ACTION_PERMISSIONS);
					applyIntent.putExtra("Package", pkgName);
					applyIntent.putExtra("Kill", false);
					sendBroadcast(applyIntent, Common.MY_PACKAGE_NAME + ".BROADCAST_PERMISSION");

					dialog.dismiss();
				}
			});
			builder.create().show();

		} else if (item.getItemId() == R.id.menu_app_launch) {
			Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(pkgName);
			startActivity(LaunchIntent);
		} else if (item.getItemId() == R.id.menu_app_settings) {
			startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName)));
		} else if (item.getItemId() == R.id.menu_app_store) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkgName)));
		}
		return super.onOptionsItemSelected(item);
	}
	
	public List<String> readAdList(String filename) {
		ArrayList<String> ret = new ArrayList<String>();
		
		BufferedReader reader = null;
		try {
		    reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));

		    // do reading, usually loop until end of file reading  
		    String mLine = reader.readLine();
		    while (mLine != null) {
		       //process line
		        ret.add(mLine.toLowerCase(Locale.US));
		    	mLine = reader.readLine();
		    }
		} catch (IOException e) {
		    //log the exception
		} finally {
		    if (reader != null) {
		         try {
		             reader.close();
		         } catch (IOException e) {
		             //log the exception
		         }
		    }
		}
		
		return ret;
	}

}
