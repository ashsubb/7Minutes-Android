package com.zenlabs.sevenminuteworkout.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbstrm.appirater.Appirater;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.database.DatabaseHelper;
import com.zenlabs.sevenminuteworkout.utils.BaseActivity;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.ShareResponseManager;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsRateMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

import java.io.IOException;
import java.util.Calendar;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.Poptart;

public class WorkoutCompletedActivity extends BaseActivity {

    private int workoutId;
    private int circules;

    private Button doneButton;
    private TextView workoutDateTextView;
    private TextView workoutOptionTextView, cyclesCompletedTextView;
    private ImageView facebookShareImageView, twitterShareImageView, instagramShareImageView;
    private RelativeLayout myAchivementsRelativeLayout;
    private Button myAchiemetnsButton;

    private DatabaseHelper databaseHelper;

    private boolean isKiipAlreadyShown = false;
    private boolean showKiip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_completed);

        Bundle extras = getIntent().getExtras();
        workoutId = extras.getInt(WorkoutActivity.EXTRA_DATE_WORKOUT_ID, 0);

        circules = UtilsMethods.getAdjustTimeFromSharedPreferences(WorkoutCompletedActivity.this, UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME);

        playSound();

        initDatabaseSetComplWorkout();

//        insertTmpDatasInDb();

        initialiseViews();

        boolean showRateDialog = false;

        Appirater.appName = " "+getResources().getString(R.string.app_name);

        if (!Appirater.alreadyRated(this)) {
            showRateDialog = true;
        }

        if (showRateDialog) {
            UtilsRateMethods.createRateDialog(this, rateAppRateDialogManager);
        } else {
            showKiip = true;
        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            databaseHelper.openDataBase();
        }

    }

    @Override
    protected void onPause() {

        super.onPause();

        if (databaseHelper != null) {
            databaseHelper.close();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        showKiip();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        LogService.Log("WorkoutCompletedActivty", "onActivityResult resultCode: " + resultCode + " requestCode: " + requestCode);

        if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
            if (requestCode == UtilsValues.ACTIVITY_RESULT_CODE_SHARE_FACEBOOK) {
                UtilsMethods.showLikeFollowAfterPostFbTweet(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_FACEBOOK_PACKAGE);
            } else if (requestCode == UtilsValues.ACTIVITY_RESULT_CODE_SHARE_TWITTER) {
                UtilsMethods.showLikeFollowAfterPostFbTweet(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_TWITTER_PACKAGE);
            } else if (requestCode == UtilsValues.ACTIVITY_RESULT_CODE_SHARE_INSTAGRAM) {
                UtilsMethods.showLikeFollowAfterPostFbTweet(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_INSTAGRAM_PACKAGE);
            }
        }

//        if (whatKindOfApp.equals(UtilsValues.SHARE_APP_FACEBOOK_PACKAGE) || whatKindOfApp.equals(UtilsValues.SHARE_APP_TWITTER_PACKAGE)) {
//            showLikeFollowAfterPostFbTweet(context, whatKindOfApp);
//        }
    }

    private void initDatabaseSetComplWorkout() {
        try {
            databaseHelper = new DatabaseHelper(WorkoutCompletedActivity.this);
            databaseHelper.addCompletedWorkout(workoutId, circules, Calendar.getInstance().getTimeInMillis() + "");
            LogService.Log("WorkoutCompletedActiivty", "db ex: " + databaseHelper.getAllCompletedWorkouts().toString());
        } catch (Exception ex) {
            LogService.Log("WorkoutCompletedActiivty", "db ex: " + ex.toString());
        }
    }

//    private void insertTmpDatasInDb(){
//        try {
//            databaseHelper = new DatabaseHelper(WorkoutCompletedActivity.this);
//
//            Calendar calendar = Calendar.getInstance();
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.HOUR, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 3);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.HOUR, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.HOUR, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
//            calendar.add(Calendar.DATE, 1);
//            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
////
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
////
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
////
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
////
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
////
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
////
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
////
////            calendar.add(Calendar.DATE, 1);
////            databaseHelper.addCompletedWorkout(workoutId, circules, calendar.getTimeInMillis() + "");
//
////            databaseHelper.addCompletedWorkout(workoutId, circules, Calendar.getInstance().getTimeInMillis() + "");
////            databaseHelper.addCompletedWorkout(workoutId, circules, Calendar.getInstance().getTimeInMillis() + "");
//            LogService.Log("WorkoutCompletedActiivty", "db ex: " + databaseHelper.getAllCompletedWorkouts().toString());
//        } catch (Exception ex) {
//            LogService.Log("WorkoutCompletedActiivty", "db ex: " + ex.toString());
//        }
//    }

    private void initialiseViews() {

        workoutDateTextView = (TextView) findViewById(R.id.workoutCompletedActivityDateTextView);

        Calendar calendar = Calendar.getInstance();
        String dateString = String.format("%1$tB %1$td, %1$tY ", calendar);
        dateString = dateString.substring(0, 1).toUpperCase() + dateString.substring(1).toLowerCase();
        String workoutDateString = getResources().getString(R.string.for_) + " " + dateString;
        workoutDateTextView.setText(workoutDateString);

        workoutOptionTextView = (TextView) findViewById(R.id.workoutCompletedActivityWorkoutOptionTextView);
        workoutOptionTextView.setText(workoutId + "");

        cyclesCompletedTextView = (TextView) findViewById(R.id.workoutCompletedActivityCyclesCompTextView);
        cyclesCompletedTextView.setText(circules + "");

        facebookShareImageView = (ImageView) findViewById(R.id.workoutCompletedActivityFaceBookShareImageView);
        facebookShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPrefieldPopUp();

            }
        });

        twitterShareImageView = (ImageView) findViewById(R.id.workoutCompletedActivityTwitterShareImageView);
        twitterShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsMethods.specififcShareByApps(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_TWITTER_PACKAGE, shareDialogResponseManager);
            }
        });

        instagramShareImageView = (ImageView) findViewById(R.id.workoutCompletedActivityInstaShareImageView);
        instagramShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsMethods.specififcShareByApps(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_INSTAGRAM_PACKAGE, shareDialogResponseManager);
            }
        });

        myAchivementsRelativeLayout = (RelativeLayout) findViewById(R.id.workoutCompletedActivityButtonsRelativeLayout);
        myAchivementsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAchievements();
            }
        });

        myAchiemetnsButton = (Button) findViewById(R.id.workoutCompletedActivityDoneButton);
        myAchiemetnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAchievements();
            }
        });

        doneButton = (Button) findViewById(R.id.workoutCompletedActivityMyAchievemtnsButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private ShareResponseManager shareDialogResponseManager = new ShareResponseManager() {
        @Override
        public void shareResponse(Intent intent, int resultCode) {
            startActivityForResult(intent, resultCode);
        }
    };

    private void startAchievements() {
        startActivity(new Intent(WorkoutCompletedActivity.this, AchievementsActivity.class));
    }

    private void showPrefieldPopUp() {

        if (UtilsMethods.getBooleanFromSharedPreferences(WorkoutCompletedActivity.this, UtilsValues.SHARED_PREFERENCES_SHOW_PREFIELD_TEXT)) {
            final Dialog dialog;

            dialog = new Dialog(WorkoutCompletedActivity.this, R.style.Dialog_PopUp7MinuteWorkout);
            try {

                dialog.setContentView(R.layout.prefield_facebook_popup_layout);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                TextView thanksTextView = (TextView) dialog.findViewById(R.id.prefieldFacebookPopupThanksTextView);
                TextView neverShowTextView = (TextView) dialog.findViewById(R.id.prefieldFacebookPopupNeverShowTextView);

                thanksTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UtilsMethods.specififcShareByApps(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_FACEBOOK_PACKAGE, shareDialogResponseManager);
                        dialog.dismiss();
                    }
                });

                neverShowTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UtilsMethods.saveBooleanInSharedPreferences(WorkoutCompletedActivity.this, UtilsValues.SHARED_PREFERENCES_SHOW_PREFIELD_TEXT, false);
                        UtilsMethods.specififcShareByApps(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_FACEBOOK_PACKAGE, shareDialogResponseManager);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            } catch (Exception e) {
                LogService.Log("WokroutCompletedAcitvity", "showPrefieldPopUp ex: " + e.toString());
            }
        } else {
            UtilsMethods.specififcShareByApps(WorkoutCompletedActivity.this, UtilsValues.SHARE_APP_FACEBOOK_PACKAGE, shareDialogResponseManager);
        }

    }

    private void playSound() {
        MediaPlayer soundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor tickAFD = getResources().openRawResourceFd(R.raw.awesome_complete);
        try {
            soundMediaPlayer.setDataSource(tickAFD.getFileDescriptor(), tickAFD.getStartOffset(), tickAFD.getDeclaredLength());
            soundMediaPlayer.prepare();
            soundMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LogService.Log("WorkoutCompletedActivity", "setOnCompletionListener finish");
//                    showKiip();
                }
            });
            soundMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            soundMediaPlayer = null;
//            showKiip();
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            soundMediaPlayer = null;
//            showKiip();
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            soundMediaPlayer = null;
//            showKiip();
        }

    }

    private void showKiip() {

        if (!isKiipAlreadyShown) {

            if (showKiip) {

                if (UtilsMethods.getBooleanFromSharedPreferences(WorkoutCompletedActivity.this, UtilsValues.SHARED_PREFERENCES_KIIP_REWARDS)) {
                    Kiip.getInstance().saveMoment(UtilsValues.KIIP_MOMENT_WORKOUT_COMPLETED, new Kiip.Callback() {

                        @Override
                        public void onFinished(Kiip kiip, Poptart reward) {
                            if (reward == null) {
                                LogService.Log("kiip_fragment_tag", "Successful moment but no reward to give.");
//                                Toast.makeText(WorkoutCompletedActivity.this, "Kiip: Successful moment but no reward to give.", Toast.LENGTH_LONG).show();
                            } else {
                                onPoptart(reward);
                            }
                            isKiipAlreadyShown = true;
                        }

                        @Override
                        public void onFailed(Kiip kiip, Exception exception) {
                            LogService.Log("kiip_fragment_tag", "onFailed ex: " + exception.toString());
                        }
                    });
                }

            }

        }

    }

    UtilsRateMethods.RateDialogManager rateAppRateDialogManager = new UtilsRateMethods.RateDialogManager() {
        @Override
        public void dialogDismissed() {
            showKiip = true;
            showKiip();
        }
    };


}
