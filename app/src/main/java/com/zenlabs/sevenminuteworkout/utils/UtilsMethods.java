package com.zenlabs.sevenminuteworkout.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.activity.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by madarashunor on 07/10/15.
 */
public class UtilsMethods {


    public static void showLegalDisclaimer(final Context context) {

        final Dialog dialog;

        dialog = new Dialog(context, R.style.Dialog_PopUp7MinuteWorkout);
        try {

            dialog.setContentView(R.layout.legal_dislaimer_popup_layout);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            dialog.getWindow().setLayout(UtilsMethods.getDialogWidth(context),
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button okButton = (Button) dialog.findViewById(R.id.legalDislaimerPopUpOkButton);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {

        }
    }

    public static void showTipScreenDialog(final Context context) {

        final Dialog dialog;

        dialog = new Dialog(context, R.style.Dialog_PopUp7MinuteWorkout);
        try {

            dialog.setContentView(R.layout.tip_screen_popup_layout);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            ImageView imageView = (ImageView) dialog.findViewById(R.id.tipScreenDialogImageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception e) {

        }
    }

    public static void showInformationDialogTwoButtons(final Context context, String titleText, String firstButtonText, String secondButtonText, final DialogResponseManager closeDialogResponseManager) {

        final Dialog dialog;

        dialog = new Dialog(context, R.style.Dialog_PopUp7MinuteWorkout);
        try {

            dialog.setContentView(R.layout.information_popup_two_buttons_layout);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            dialog.getWindow().setLayout(UtilsMethods.getDialogWidth(context),
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView titleTextView = (TextView) dialog.findViewById(R.id.informatioPopUpTitleTextVIew);
            titleTextView.setText(titleText);

            Button firstButton = (Button) dialog.findViewById(R.id.informatioPopUpFirstButton);
            firstButton.setText(firstButtonText);
            firstButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    closeDialogResponseManager.responseArrived(true);
                }
            });

            Button secondButton = (Button) dialog.findViewById(R.id.informatioPopUpSecondButton);
            secondButton.setText(secondButtonText);
            secondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    closeDialogResponseManager.responseArrived(false);
                }
            });

            dialog.show();
        } catch (Exception e) {

        }
    }

    public static void saveBooleanInSharedPreferences(Context context, String key, boolean value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value)
                .commit();

        LogService.Log("saveBooleanInSharedPreferences", "key: " + key + " value: " + value);

    }

    public static boolean getBooleanFromSharedPreferences(Context context, String key) {
        return getBooleanFromSharedPreferences(context, key, true);
    }

    public static boolean getBooleanFromSharedPreferences(Context context, String key, boolean defaultValue) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        if (key.equals(UtilsValues.SHARED_PREFERENCES_TICK_SOUNDS)) {
            defaultValue = false;
        }

        boolean value = sharedPreferences.getBoolean(key, defaultValue);

        return value;
    }

    public static void saveStringInSharedPreferences(Context context, String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value)
                .commit();

        LogService.Log("saveStringInSharedPreferences", "key: " + key + " value: " + value);

    }

    public static String getStringFromSharedPreferences(Context context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        String value = sharedPreferences.getString(key, "");

        return value;
    }

    public static void saveIntInSharedPreferences(Context context, String key, int value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value)
                .commit();

        LogService.Log("saveIntInSharedPreferences", "key: " + key + " value: " + value);

    }

    public static int getAdjustTimeFromSharedPreferences(Context context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        int value = sharedPreferences.getInt(key, -1);

        if (value == -1) {
            switch (key) {
                case UtilsValues.SHARED_PREFERENCES_EXERCISE_TIME:
                    value = AdjustTimeEnum.EXERCISE_TIME_30;
                    break;
                case UtilsValues.SHARED_PREFERENCES_REST_TIME:
                    value = AdjustTimeEnum.REST_TIME_10;
                    break;
                case UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME:
                    value = AdjustTimeEnum.CIRCUIT_NR_1;
                    break;
            }
        }

        return value;
    }

    public static int getDialogWidth(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        int width;
        if (isTablet(context)) {
            width = (int) (metrics.widthPixels * 0.7);
        } else {
            width = (int) (metrics.widthPixels * 0.8);
        }
        return width;
    }

    public static int getDeviceWidth(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String readFile(String path) {

        String stringText = "";

        LogService.Log("readFile", "path: " + path);

        try {
            URLConnection conn = new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String StringBuffer;

            while ((StringBuffer = bufferReader.readLine()) != null) {
                stringText += StringBuffer;
            }
            bufferReader.close();

        } catch (Throwable t) {
            LogService.Log("readFile", "Throwable: " + t.toString());
            t.printStackTrace();
        }

        LogService.Log("readFile", "stringText: " + stringText);

        return stringText;
    }

    public static String getMoreApps(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                context.getString(R.string.PREFS), 1);
        String more_apps = settings.getString("more_apps", "");
        return more_apps;
    }

    public static void setMoreApps(Context context, String more_apps) {
        SharedPreferences settings = context.getSharedPreferences(
                context.getString(R.string.PREFS), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("more_apps", more_apps);
        editor.commit();
    }

    public static boolean hasInternetConnection(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetwork = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {

                return true;
            }

            NetworkInfo mobileNetwork = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnected()) {

                return true;
            }

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {

                return true;
            }
        }
        return false;
    }

    public static boolean isUnlocked(Context context, String itemName) {

        boolean isUnlocked = false;

        if (UtilsMethods.getBooleanFromSharedPreferences(context, itemName, false)) {
            isUnlocked = true;
        }

        if (UtilsMethods.getBooleanFromSharedPreferences(context, MainActivity.ALL_IN_ONE_IAP_ITEM, false)) {
            isUnlocked = true;
        }

        return isUnlocked;
    }

    public static void specififcShareByApps(Context context, String whatKindOfApp, ShareResponseManager shareResponseManager) {

//        String urlToShare = "Check out the newest 7 Minute Workout app from @zenlabsfitness! https://play.google.com/store/apps/details?id=com.c10kforpink&hl=en ";


        String urlToShare = context.getResources().getString(R.string.sharing_text_workout_end);
        int resultCode = UtilsValues.ACTIVITY_RESULT_CODE_SHARE_TWITTER;

        String uriFacebook = "" + Uri.parse(UtilsValues.GOOGLE_STORE_URL + context.getPackageName());

        if (whatKindOfApp.equals(UtilsValues.SHARE_APP_FACEBOOK_PACKAGE)) {
            resultCode = UtilsValues.ACTIVITY_RESULT_CODE_SHARE_FACEBOOK;
//            urlToShare +=" "+ "https://play.google.com/store/apps/details?id=com.c10kforpink&hl=en";
//            urlToShare += Uri.parse(UtilsValues.GOOGLE_STORE_URL + context.getPackageName());
        }

        boolean isAppFound = false;

        Intent intent = new Intent(Intent.ACTION_SEND);

        if (whatKindOfApp.equals(UtilsValues.SHARE_APP_FACEBOOK_PACKAGE) || whatKindOfApp.equals(UtilsValues.SHARE_APP_TWITTER_PACKAGE)) {

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", urlToShare);
            clipboard.setPrimaryClip(clip);

            intent.setType("text/plain");
            if(whatKindOfApp.equals(UtilsValues.SHARE_APP_FACEBOOK_PACKAGE)) {
                intent.putExtra(Intent.EXTRA_TEXT, uriFacebook);
            }
            else if(whatKindOfApp.equals(UtilsValues.SHARE_APP_TWITTER_PACKAGE)){
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
            }

        } else {

            //instagram

            resultCode = UtilsValues.ACTIVITY_RESULT_CODE_SHARE_INSTAGRAM;

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.insta_screen);

            File filePath = Environment.getExternalStorageDirectory();

            String fileName = "img1.jpg";

            File fileToStream = new File(filePath, fileName);
            try {

                FileOutputStream outStream;

                outStream = new FileOutputStream(fileToStream);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                outStream.flush();

                outStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String sdPath = filePath.getAbsolutePath().toString() + "/" + fileName;

            LogService.Log("specififcShareByApps", "Your IMAGE ABSOLUTE PATH:-" + sdPath);

            File fileToShare = new File(sdPath);

            if (!fileToShare.exists()) {
                LogService.Log("specififcShareByApps", "no image file at location :" + sdPath);
            }

            // Set the MIME type
            intent.setType("image/*");

            // Create the URI from the media
            Uri uri = Uri.fromFile(fileToShare);

            // Add the URI to the Intent.
            intent.putExtra(Intent.EXTRA_STREAM, uri);


        }

        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            LogService.Log("specififcShareByApps", "info.activityInfo.packageName: " + info.activityInfo.packageName);
            if (info.activityInfo.packageName.toLowerCase().startsWith(whatKindOfApp)) {
                intent.setPackage(info.activityInfo.packageName);
                isAppFound = true;
                break;
            }
        }

        if (isAppFound) {

            shareResponseManager.shareResponse(intent, resultCode);

        } else {
            showInformationDialog(context, context.getResources().getString(R.string.application_unavailable), context.getResources().getString(R.string.error_app_not_found));
        }


    }

    public static void showInformationDialog(final Context context, String title, String content) {

        final Dialog dialog;

        dialog = new Dialog(context, R.style.Dialog_PopUp7MinuteWorkout);
        try {

            dialog.setContentView(R.layout.information_dialog_layout);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            dialog.getWindow().setLayout(UtilsMethods.getDialogWidth(context),
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView titleTextView = (TextView) dialog.findViewById(R.id.informationDialogTitleTextVIew);
            titleTextView.setText(title);

            TextView contentTextView = (TextView) dialog.findViewById(R.id.informationDialogContentTextVIew);
            contentTextView.setText(content);

            Button doneButton = (Button) dialog.findViewById(R.id.informatioDialogButton);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
        } catch (Exception e) {
        }
    }

    public static final void showLikeFollowAfterPostFbTweet(final Context context, String whatKindOfApp) {

        String information = context.getResources().getString(R.string.follow_us_twitter_text);
        String rightButtonText = context.getResources().getString(R.string.follow_us);
        String shareUrl = context.getResources().getString(R.string.share_url_twitter_browser);

        if (whatKindOfApp.equals(UtilsValues.SHARE_APP_FACEBOOK_PACKAGE)) {
            information = context.getResources().getString(R.string.like_us_facebook_text);
            rightButtonText = context.getResources().getString(R.string.like_us);
            shareUrl = context.getResources().getString(R.string.share_url_facebook_browser);
        } else if (whatKindOfApp.equals(UtilsValues.SHARE_APP_INSTAGRAM_PACKAGE)) {
            information = context.getResources().getString(R.string.follow_us_instagram_text);
            shareUrl = context.getResources().getString(R.string.share_url_instagram_browser);
        }

        final Dialog dialog;

        dialog = new Dialog(context, R.style.Dialog_PopUp7MinuteWorkout);
        try {

            dialog.setContentView(R.layout.information_popup_two_horizontal_buttons_layout);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            dialog.getWindow().setLayout(UtilsMethods.getDialogWidth(context),
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView titleTextView = (TextView) dialog.findViewById(R.id.informatioTwoHoristonalButtonsPopUpTitleTextVIew);
            titleTextView.setText(information);

            Button leftButton = (Button) dialog.findViewById(R.id.informatioTwoHoristonalButtonsPopUpFirstButton);
            leftButton.setText(context.getResources().getString(R.string.no_thanks));
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button rightButton = (Button) dialog.findViewById(R.id.informatioTwoHoristonalButtonsPopUpSecondButton);
            rightButton.setText(rightButtonText);
            final String finalShareUrl = shareUrl;
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalShareUrl));
                    ((Activity) context).startActivity(browserIntent);
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
        }
    }

    public static double getRoundedLongNumber(long number) {
        double setValue = number;
        int index = 1;
        while (setValue > 99) {
            setValue = setValue / 10;
            index *= 10;
        }
        int theValue = (int) setValue;
        LogService.Log("getRoundedLongNumber", "theValue: " + theValue);
        return theValue * index;
    }

}
