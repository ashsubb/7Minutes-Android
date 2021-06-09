package com.sbstrm.appirater;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*	
 * @source https://github.com/sbstrm/appirater-android
 * @license MIT/X11
 * 
 * Copyright (c) 2011-2013 sbstrm Y.K.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class Appirater {

	public static final String PREF_LAUNCH_COUNT = "launch_count";
	private static final String PREF_RATE_CLICKED = "rateclicked";
	private static final String PREF_DONT_SHOW = "dontshow";
	private static final String PREF_DATE_REMINDER_PRESSED = "date_reminder_pressed";
	private static final String PREF_DATE_FIRST_LAUNCHED = "date_firstlaunch";
	private static final String PREF_APP_VERSION_CODE = "versioncode";
	public static String appName, marketLink;
	private static boolean isUpdate = false;

	public static void appLaunched(Context mContext) {
		boolean testMode = mContext.getResources().getBoolean(
				R.bool.appirator_test_mode);
		SharedPreferences prefs = mContext.getSharedPreferences(
				mContext.getPackageName() + ".appirater", 0);

		if (!testMode
				&& (prefs.getBoolean(PREF_DONT_SHOW, false) || prefs
						.getBoolean(PREF_RATE_CLICKED, false))) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong(PREF_LAUNCH_COUNT, 0);

		// Get date of first launch
		long date_firstLaunch = prefs.getLong(PREF_DATE_FIRST_LAUNCHED, 0);

		// Get reminder date pressed
		long date_reminder_pressed = prefs.getLong(PREF_DATE_REMINDER_PRESSED,
				0);

		try {
			int appVersionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
			if (prefs.getInt(PREF_APP_VERSION_CODE, 0) != 0
					&& prefs.getInt(PREF_APP_VERSION_CODE, 0) != appVersionCode) {
				isUpdate = true;
				launch_count = 0;
			}
			editor.putInt(PREF_APP_VERSION_CODE, appVersionCode);
		} catch (Exception e) {
			// do nothing
		}

		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong(PREF_DATE_FIRST_LAUNCHED, date_firstLaunch);
		}

		if (testMode) {
			editor.commit();
			showRateDialog1(mContext, editor);
			return;
		}

		// Wait at least n days before opening
		if (launch_count >= mContext.getResources().getInteger(
				R.integer.appirator_launches_until_prompt)) {
			if (date_reminder_pressed == 0) {
				showRateDialog1(mContext, editor);
				launch_count = 0;
			} else {
				long remindMillisecondsToWait = mContext.getResources()
						.getInteger(R.integer.appirator_days_before_reminding)
						* 24 * 60 * 60 * 1000L;
				if (System.currentTimeMillis() >= (remindMillisecondsToWait + date_reminder_pressed)) {
					showRateDialog1(mContext, editor);
					launch_count = 0;
				}
			}
		}

		launch_count++;
		editor.putLong(PREF_LAUNCH_COUNT, launch_count);
		editor.commit();
	}

	@SuppressLint("NewApi")
	private static void showRateDialog1(final Context mContext,
			final SharedPreferences.Editor editor) {

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.appirater1, null);

		TextView tv = (TextView) layout.findViewById(R.id.message);

		if (isUpdate) {
			tv.setText("Love the newest update?");
		} else {
			tv.setText(String.format(mContext.getString(R.string.rate_message),
					appName));
		}

		Button loveBtn = (Button) layout.findViewById(R.id.love);
		loveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

//				FlurryAgent.logEvent(appName + "_android_love_it");
				showRateDialog2(mContext, editor);
				dialog.dismiss();
			}
		});

		Button itsOkBtn = (Button) layout.findViewById(R.id.ok);
		itsOkBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

//				FlurryAgent.logEvent(appName + "_android_its_ok");
				showRateDialog3(mContext, editor);
				dialog.dismiss();
			}
		});

		Button hateBtn = (Button) layout.findViewById(R.id.hate);
		hateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

//				FlurryAgent.logEvent(appName + "_android_hate_it");
				showRateDialog3(mContext, editor);
				dialog.dismiss();
			}
		});

		dialog.setContentView(layout);
		dialog.show();
	}

	public static void showRateDialog2(final Context mContext,
			final SharedPreferences.Editor editor) {

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.appirater2, null);

		Button sureBtn = (Button) layout.findViewById(R.id.sure);
		sureBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// RATE
				rate(mContext);
				dialog.dismiss();
			}
		});

		Button remindBtn = (Button) layout.findViewById(R.id.maybe);
		remindBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

//				FlurryAgent.logEvent(appName + "_android_maybe_later");
				if (editor != null) {
					editor.putLong(PREF_DATE_REMINDER_PRESSED,
							System.currentTimeMillis());
					editor.commit();
				}
				dialog.dismiss();
			}
		});

		Button noBtn = (Button) layout.findViewById(R.id.no_thanks);
		noBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				refuse(mContext);
				dialog.dismiss();
			}
		});

		dialog.setContentView(layout);
		dialog.show();
	}

	private static void showRateDialog3(final Context mContext,
			final SharedPreferences.Editor editor) {

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.appirater3, null);

		TextView tv = (TextView) layout.findViewById(R.id.message);
		tv.setText(String.format(mContext.getString(R.string.opinion_message),
				appName));

		Button notNowBtn = (Button) layout.findViewById(R.id.not_now);
		notNowBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

//				FlurryAgent.logEvent(appName + "_android_not_now");
				if (editor != null) {
					editor.putBoolean(PREF_DONT_SHOW, true);
					editor.commit();
				}
				dialog.dismiss();
			}

		});

		Button sendBtn = (Button) layout.findViewById(R.id.send);
		sendBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// SEND FEEDBACK
				sendFeedback(mContext);
				dialog.dismiss();
			}
		});

		dialog.setContentView(layout);
		dialog.show();
	}

	public static void rate(Context mContext) {

		SharedPreferences prefs = mContext.getSharedPreferences(
				mContext.getPackageName() + ".appirater", 0);
		final SharedPreferences.Editor editor = prefs.edit();

//		FlurryAgent.logEvent(appName + "_android_sure");
		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String
				.format(marketLink, mContext.getPackageName()))));
		if (editor != null) {
			editor.putBoolean(PREF_RATE_CLICKED, true);
			editor.commit();
		}
	}

	public static void refuse(Context mContext) {

		SharedPreferences prefs = mContext.getSharedPreferences(
				mContext.getPackageName() + ".appirater", 0);
		SharedPreferences.Editor editor = prefs.edit();

//		FlurryAgent.logEvent(appName + "_android_no_thanks");
		if (editor != null) {
			editor.putBoolean(PREF_DONT_SHOW, true);
			editor.commit();
		}
	}

	public static boolean alreadyRated(Context mContext) {

		SharedPreferences prefs = mContext.getSharedPreferences(
				mContext.getPackageName() + ".appirater", 0);
		return prefs.getBoolean(PREF_RATE_CLICKED, false);
	}

	public static boolean alreadyRefused(Context mContext) {

		SharedPreferences prefs = mContext.getSharedPreferences(
				mContext.getPackageName() + ".appirater", 0);
		return prefs.getBoolean(PREF_DONT_SHOW, false);
	}

	public static void sendFeedback(Context mContext) {

		SharedPreferences prefs = mContext.getSharedPreferences(
				mContext.getPackageName() + ".appirater", 0);
		final SharedPreferences.Editor editor = prefs.edit();

//		FlurryAgent.logEvent(appName + "_android_send_feedback");
		PackageInfo pinfo;
		try {
			pinfo = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
			pinfo = null;
		}

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
		intent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { "contactus@zenlabsllc.com" });
		intent.putExtra(Intent.EXTRA_TEXT, "App Name:" + appName + "\n"
				+ "App Version: " + pinfo.versionName + "\n"
				+ "Device Model : " + Build.MODEL + Build.MANUFACTURER + "\n"
				+ "OS Version : " + "Android " + Build.VERSION.RELEASE);

		mContext.startActivity(Intent.createChooser(intent,
				"Select email application."));

//		FlurryAgent.logEvent(appName + "_android_not_now");
		if (editor != null) {
			editor.putBoolean(PREF_DONT_SHOW, true);
			editor.commit();
		}
	}
}