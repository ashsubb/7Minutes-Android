package com.zenlabs.sevenminuteworkout.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.activity.SplashActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by madarashunor on 09/10/15.
 */
public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        LogService.Log("ReminderBroadcastReceiver", "onReceive Alarm");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);

        boolean isRunning = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            Log.d("push", "app is running");
            isRunning = true;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(context.getResources().getString(R.string.app_name));
        mBuilder.setContentText(context.getResources().getString(R.string.notification_text));
        mBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(context, SplashActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        if (!isRunning) {
            mBuilder.setSubText(context.getResources().getString(R.string.click_to_run));
            mBuilder.setContentIntent(resultPendingIntent);
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(UtilsValues.NOTIFICATION_ID, mBuilder.build());

        setResultCode(Activity.RESULT_OK);

        wl.release();

        String time = UtilsMethods.getStringFromSharedPreferences(context, UtilsValues.SHARED_PREFERENCES_REMINDER_TIME);
        Calendar calendar = Calendar.getInstance();
        if (!time.equals("")) {
            calendar.setTimeInMillis(Long.valueOf(time));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            setAlarm(context, calendar);
        }

    }

    public static void setAlarm(Context context, Calendar calendar) {
        cancelAlarm(context);

        Intent i = new Intent(context, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent); // Millisec * Second * Minute * Hour
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // Millisec * Second * Minute * Hour

//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        UtilsMethods.saveStringInSharedPreferences(context, UtilsValues.SHARED_PREFERENCES_REMINDER_TIME, calendar.getTimeInMillis() + "");
        LogService.Log("ReminderBroadcastReceiver", "setAlarm: " + calendar.getTimeInMillis() + " time: " + calendar.toString());

    }

    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        UtilsMethods.saveStringInSharedPreferences(context, UtilsValues.SHARED_PREFERENCES_REMINDER_TIME, "");
        LogService.Log("ReminderBroadcastReceiver", "cancelAlarm");
    }

}
