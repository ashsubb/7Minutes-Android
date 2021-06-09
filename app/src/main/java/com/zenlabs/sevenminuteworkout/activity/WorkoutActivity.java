package com.zenlabs.sevenminuteworkout.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.database.Exercise;
import com.zenlabs.sevenminuteworkout.utils.DialogResponseManager;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorkoutActivity extends Activity {

    private ArrayList<Exercise> items;

    public static final String EXTRA_DATE_WORKOUT_ID = "EXTRA_DATE_WORKOUT_ID";
    public static final String EXTRA_DATE_EXERCISE_LIST = "EXTRA_DATE_EXERCISE_LIST";

    private int restTime;
    private int exerciseTime;
    private int circuitTime;

    private int workoutPosition = -1;
    private int positionInItems = -1;

    private TextView actionBarTitleTextView;
    private ImageView closeImageView;

    private RelativeLayout containerRelativeLayout;

    private TextView exerciseNumberOfTextView;
    private LinearLayout exerciseInformationLinearLayout;
    private TextView countDownAndInfoTextView;
    private TextView restStaticTextView;

    private ImageView pauseOverlayImageView;
    private ImageView backgroundFlip1ImageView;
    private ImageView backgroundFlip2ImageView;
    private ImageView backgroundFlip3ImageView;

    private RelativeLayout navigationBarContainerRelativeLayout;
    private ImageView startPauseImageView;
    private RelativeLayout leftNavigatinRelativeLayout, rightNavigatinRelativeLayout;
    private LinearLayout bottomInformationLinearLayout;
    private TextView nextExerciseStaticTextView, nextExerciseNameTextView;

    private boolean isRest = true;
    private boolean isPauase = false;

    private CountDownTimer countDownTimer;
    private long exerciseMillisTime = 0;
//    private int exerciseTimeInt = 0;

    private MediaPlayer tickSoundMediaPlayer;
    private MediaPlayer nowRestSoundMediaPlayer;
    private MediaPlayer secondsLeftOneSoundMediaPlayer;
    private MediaPlayer secondsLeftTwoSoundMediaPlayer;
    private MediaPlayer secondsLeftThreeSoundMediaPlayer;
    private MediaPlayer secondsLeftTenSoundMediaPlayer;
    private MediaPlayer secondsLeftFifteennSoundMediaPlayer;
    private MediaPlayer nextExerciseSoundMediaPlayer;
    private MediaPlayer readyGoSoundMediaPlayer;
    private MediaPlayer exerciseSoundMediaPlayer;
    private MediaPlayer finishBeepSoundMediaPlayer;

    private AnimatorSet flipAnimatorSet;
    private int whichFlipIsAnimated = 0;

    private int randomFlipedImagePosition = 0;
    private int[] flipedImage1Ids = {R.drawable.random1_1, R.drawable.random2_1, R.drawable.random03_1, R.drawable.random04_1, R.drawable.random05_1, R.drawable.random06_1, R.drawable.random07_1, R.drawable.random08_1, R.drawable.random09_1, R.drawable.random10_1};
    private int[] flipedImage2Ids = {R.drawable.random1_2, R.drawable.random02_2, R.drawable.random03_2, R.drawable.random04_2, R.drawable.random05_2, R.drawable.random06_2, R.drawable.random07_2, R.drawable.random08_2, R.drawable.random09_2, R.drawable.random10_2};
    private int[] flipedImage3Ids = {R.drawable.random1_3, R.drawable.random02_3, R.drawable.random03_3, R.drawable.random04_3, R.drawable.random05_3, R.drawable.random06_3, R.drawable.random07_3, R.drawable.random08_3, R.drawable.random09_3, R.drawable.random10_3};
//    private int[] flipedBigImageIds = {R.drawable.ex_1, R.drawable.ex_2};

    private boolean isFinish = false;
    private int workoutId = 0;

    private boolean activityIsPaused = false;

    //    Admod
    private AdView admobAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        LogService.Log("WorkoutActivity", " onCreate ");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        String objectString = extras.getString(EXTRA_DATE_EXERCISE_LIST, "");

        if (!objectString.equals("")) {
//            items = new Gson().fromJson(objectString, ArrayList.class);
            items = new Gson().fromJson(objectString, new TypeToken<ArrayList<Exercise>>() {
            }.getType());
            LogService.Log("WorkoutActivity", "items: " + items);
        }

        workoutId = extras.getInt(EXTRA_DATE_WORKOUT_ID, 0);

        initSoundsPlayers();

        setTimes();

        initialiseViews();

        loadContent(restTime);

    }

    private void initSoundsPlayers() {

        boolean isSet = UtilsMethods.getBooleanFromSharedPreferences(WorkoutActivity.this, UtilsValues.SHARED_PREFERENCES_TICK_SOUNDS);

        if (isSet) {

            tickSoundMediaPlayer = new MediaPlayer();
            AssetFileDescriptor tickAFD = getResources().openRawResourceFd(R.raw.timer_beep);
            try {
                tickSoundMediaPlayer.setDataSource(tickAFD.getFileDescriptor(), tickAFD.getStartOffset(), tickAFD.getDeclaredLength());
                tickSoundMediaPlayer.prepare();
            } catch (IllegalArgumentException e) {
                LogService.Log("initSoundsPlayers", "error: " + e.toString());
                tickSoundMediaPlayer = null;
            } catch (IllegalStateException e) {
                LogService.Log("initSoundsPlayers", "error: " + e.toString());
                tickSoundMediaPlayer = null;
            } catch (IOException e) {
                LogService.Log("initSoundsPlayers", "error: " + e.toString());
                tickSoundMediaPlayer = null;
            }

        }

        nextExerciseSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor nextExerciseAFD = getResources().openRawResourceFd(R.raw.nextexerciseis);
        try {

            nextExerciseSoundMediaPlayer.setDataSource(nextExerciseAFD.getFileDescriptor(), nextExerciseAFD.getStartOffset(), nextExerciseAFD.getDeclaredLength());
            nextExerciseSoundMediaPlayer.prepare();
            nextExerciseSoundMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playSoundExerciseName();
                }
            });

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            nextExerciseSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            nextExerciseSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            nextExerciseSoundMediaPlayer = null;
        }

        nowRestSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor nowRestAFD = getResources().openRawResourceFd(R.raw.now_rest);
        try {

            nowRestSoundMediaPlayer.setDataSource(nowRestAFD.getFileDescriptor(), nowRestAFD.getStartOffset(), nowRestAFD.getDeclaredLength());
            nowRestSoundMediaPlayer.prepare();
            nowRestSoundMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    nextExerciseSoundMediaPlayer.start();
                }
            });

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            nowRestSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            nowRestSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            nowRestSoundMediaPlayer = null;
        }

        secondsLeftOneSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor secondsLeftOneAFD = getResources().openRawResourceFd(R.raw.one);
        try {

            secondsLeftOneSoundMediaPlayer.setDataSource(secondsLeftOneAFD.getFileDescriptor(), secondsLeftOneAFD.getStartOffset(), secondsLeftOneAFD.getDeclaredLength());
            secondsLeftOneSoundMediaPlayer.prepare();

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftOneSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftOneSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftOneSoundMediaPlayer = null;
        }

        secondsLeftTwoSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor secondsLeftTwoAFD = getResources().openRawResourceFd(R.raw.two);
        try {

            secondsLeftTwoSoundMediaPlayer.setDataSource(secondsLeftTwoAFD.getFileDescriptor(), secondsLeftTwoAFD.getStartOffset(), secondsLeftTwoAFD.getDeclaredLength());
            secondsLeftTwoSoundMediaPlayer.prepare();

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftTwoSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftTwoSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftTwoSoundMediaPlayer = null;
        }

        secondsLeftThreeSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor secondsLeftThreeAFD = getResources().openRawResourceFd(R.raw.three);
        try {

            secondsLeftThreeSoundMediaPlayer.setDataSource(secondsLeftThreeAFD.getFileDescriptor(), secondsLeftThreeAFD.getStartOffset(), secondsLeftThreeAFD.getDeclaredLength());
            secondsLeftThreeSoundMediaPlayer.prepare();

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftThreeSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftThreeSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftThreeSoundMediaPlayer = null;
        }

        secondsLeftTenSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor secondsLeftTenAFD = getResources().openRawResourceFd(R.raw.secondsleft10);
        try {

            secondsLeftTenSoundMediaPlayer.setDataSource(secondsLeftTenAFD.getFileDescriptor(), secondsLeftTenAFD.getStartOffset(), secondsLeftTenAFD.getDeclaredLength());
            secondsLeftTenSoundMediaPlayer.prepare();

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftTenSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftTenSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftTenSoundMediaPlayer = null;
        }

        secondsLeftFifteennSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor secondsLeftFifteenAFD = getResources().openRawResourceFd(R.raw.secondsleft15);
        try {

            secondsLeftFifteennSoundMediaPlayer.setDataSource(secondsLeftFifteenAFD.getFileDescriptor(), secondsLeftFifteenAFD.getStartOffset(), secondsLeftFifteenAFD.getDeclaredLength());
            secondsLeftFifteennSoundMediaPlayer.prepare();

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftFifteennSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftFifteennSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            secondsLeftFifteennSoundMediaPlayer = null;
        }

        readyGoSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor readyGoAFD = getResources().openRawResourceFd(R.raw.ready_go);
        try {

            readyGoSoundMediaPlayer.setDataSource(readyGoAFD.getFileDescriptor(), readyGoAFD.getStartOffset(), readyGoAFD.getDeclaredLength());
            readyGoSoundMediaPlayer.prepare();
            readyGoSoundMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int time = (int) (exerciseMillisTime / 1000);
                    startCountDown(time);
                }
            });

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            readyGoSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            readyGoSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            readyGoSoundMediaPlayer = null;
        }

        finishBeepSoundMediaPlayer = new MediaPlayer();
        AssetFileDescriptor finishBeepAFD = getResources().openRawResourceFd(R.raw.beep);
        try {

            finishBeepSoundMediaPlayer.setDataSource(finishBeepAFD.getFileDescriptor(), finishBeepAFD.getStartOffset(), finishBeepAFD.getDeclaredLength());
            finishBeepSoundMediaPlayer.prepare();

        } catch (IllegalArgumentException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            finishBeepSoundMediaPlayer = null;
        } catch (IllegalStateException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            finishBeepSoundMediaPlayer = null;
        } catch (IOException e) {
            LogService.Log("initSoundsPlayers", "error: " + e.toString());
            finishBeepSoundMediaPlayer = null;
        }

    }

    private void playSoundNextExercise() {

        if (isRest) {

            if (workoutPosition != -1) {
                if (nowRestSoundMediaPlayer != null) {
                    nowRestSoundMediaPlayer.start();
                }
            } else if (nextExerciseSoundMediaPlayer != null) {
                nextExerciseSoundMediaPlayer.start();
            }


        }

    }

    private void playSoundExerciseName() {

        int position = 0;

        if (workoutPosition != -1) {
            position = positionInItems;
            position += 1;
        }

        exerciseSoundMediaPlayer = new MediaPlayer();
        String path = "android.resource://" + getPackageName() + "/raw" + "/" + items.get(position).getSound();
        try {
            exerciseSoundMediaPlayer.setDataSource(WorkoutActivity.this, Uri.parse(path));
            exerciseSoundMediaPlayer.prepare();
            exerciseSoundMediaPlayer.start();
        } catch (Exception e) {
            LogService.Log("playSoundExerciseName", "error: " + e.toString());
        }

        startCountDown(restTime);

    }

    private void playTickSound() {
        if (tickSoundMediaPlayer != null) {
            tickSoundMediaPlayer.start();
        }
    }

    private void setTimes() {

        restTime = UtilsMethods.getAdjustTimeFromSharedPreferences(WorkoutActivity.this, UtilsValues.SHARED_PREFERENCES_REST_TIME);
        exerciseTime = UtilsMethods.getAdjustTimeFromSharedPreferences(WorkoutActivity.this, UtilsValues.SHARED_PREFERENCES_EXERCISE_TIME);
        circuitTime = UtilsMethods.getAdjustTimeFromSharedPreferences(WorkoutActivity.this, UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME);

    }

    private void initialiseViews() {

        actionBarTitleTextView = (TextView) findViewById(R.id.workoutActvityScreenActionBarTitleTextView);
        closeImageView = (ImageView) findViewById(R.id.workoutScreenActionBarBackImageView);
        closeImageView.setOnClickListener(closeImageViewOnClickListener);

        containerRelativeLayout = (RelativeLayout) findViewById(R.id.workoutActivityContainerRelativeLayout);

        exerciseNumberOfTextView = (TextView) findViewById(R.id.workoutActivityExersiceNrOfTextView);
        exerciseInformationLinearLayout = (LinearLayout) findViewById(R.id.workoutActivityIntructionsLinearLayout);
        exerciseInformationLinearLayout.setOnClickListener(exerciseInformationLinearLayoutOnClickListener);

        countDownAndInfoTextView = (TextView) findViewById(R.id.workoutActivityCounterAndInfoTextView);
        restStaticTextView = (TextView) findViewById(R.id.workoutActivityRestCountDownTextView);

        backgroundFlip1ImageView = (ImageView) findViewById(R.id.workoutActivityBackroundFlip1ImageView);
        backgroundFlip2ImageView = (ImageView) findViewById(R.id.workoutActivityBackroundFlip2ImageView);
        backgroundFlip3ImageView = (ImageView) findViewById(R.id.workoutActivityBackroundFlip3ImageView);
        pauseOverlayImageView = (ImageView) findViewById(R.id.workoutActivityBackroundImageView);

        navigationBarContainerRelativeLayout = (RelativeLayout) findViewById(R.id.workoutActivityNavigationBarContainerRelativeLayout);

        startPauseImageView = (ImageView) findViewById(R.id.workoutActivityStartPauseImageView);
        startPauseImageView.setOnClickListener(startPauseImageViewOnClickListener);

        leftNavigatinRelativeLayout = (RelativeLayout) findViewById(R.id.workoutActivityNavigationBarLeftRelativeLayout);
        leftNavigatinRelativeLayout.setOnClickListener(leftNavigatinRelativeLayoutOnClickListener);

        rightNavigatinRelativeLayout = (RelativeLayout) findViewById(R.id.workoutActivityNavigationBarRightRelativeLayout);
        rightNavigatinRelativeLayout.setOnClickListener(rightNavigatinRelativeLayoutOnClickListener);

        bottomInformationLinearLayout = (LinearLayout) findViewById(R.id.workoutActivityBottomLinearLayout);

        nextExerciseStaticTextView = (TextView) findViewById(R.id.workoutActivityNextExerciseTextView);
        nextExerciseNameTextView = (TextView) findViewById(R.id.workoutActivityNexWorkoutNameTextView);

        admobAdView = (AdView) findViewById(R.id.workoutActivityBannerAdView);
        showAdmobBanner();

    }

    @Override
    protected void onPause() {

        activityIsPaused = true;
        if (!isPauase && !isFinish) {
            pauseExercise(true);
        }
        if (admobAdView != null) {
            admobAdView.pause();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        activityIsPaused = false;
        super.onResume();
        if (admobAdView != null) {
            admobAdView.resume();
        }
        LogService.Log("WorkoutActivity", " onResume ");
    }

    @Override
    protected void onDestroy() {
        if (admobAdView != null) {
            admobAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        onBackPressedAction();
    }

    private void loadContent(int timeForCountDown) {

        if (workoutPosition >= (circuitTime * items.size())) {

        } else {

            if (workoutPosition != -1) {

                LogService.Log("WorkOutActivity", "workoutPosition" + "increase" + " positionInItems: " + positionInItems);

                if (workoutPosition == 0 || (((double) workoutPosition) % ((double) items.size()) == 0)) {
                    positionInItems = 0;
                }

                if (workoutPosition != 0) {
                    LogService.Log("WorkOutActivity", "workoutPosition" + " value: " + (((double) workoutPosition) % ((double) items.size()) == 0));
                }

//                int sumPositions = (circuitTime - 1) * items.size();
//                positionInItems = workoutPosition;
//                if (workoutPosition >= items.size()) {
//                    positionInItems = (workoutPosition - (sumPositions));
//                }

                LogService.Log("WorkOutActivity", "setCurrentExerciseName: positionInItems " + positionInItems + " workoutPosition: " + workoutPosition + " items.size(): " + items.size());

            }
//        if(workoutPosition==-1){
//            //start
//        }
//        else {
//        }

            setCurrentExerciseName(positionInItems);

            setNrOfExercise();

            setNextExerciseName(positionInItems);

            if (!isRest) {
                exerciseMillisTime = (exerciseTime) * 1000;
                readyGoSoundMediaPlayer.start();
            } else {
                playSoundNextExercise();
            }

        }

    }

    private void setCurrentExerciseName(int position) {

        String name = getResources().getString(R.string.take_a_breath);

        if (position != -1 && !isRest) {
            name = items.get(position).getName();
        }

        actionBarTitleTextView.setText(name);

    }

    private void setNrOfExercise() {

        if (!isRest && !isPauase) {
            String nrOfExercise = getResources().getString(R.string.exercise) + " " + (workoutPosition + 1) + " " + getResources().getString(R.string.of) + " " + circuitTime * items.size();
            exerciseNumberOfTextView.setVisibility(View.VISIBLE);
            exerciseNumberOfTextView.setText(nrOfExercise);
        } else {
            exerciseNumberOfTextView.setVisibility(View.GONE);
        }


    }

    private void setNextExerciseName(int position) {

        if (!isRest && isPauase) {
            nextExerciseStaticTextView.setVisibility(View.GONE);
            nextExerciseNameTextView.setVisibility(View.GONE);
        } else {

            position += 1;

            if (isRest) {
                nextExerciseStaticTextView.setTextColor(getResources().getColor(R.color.frog_green_menu_color));
            } else {
                nextExerciseStaticTextView.setTextColor(getResources().getColor(R.color.blue_button_color));
            }

            String name = getResources().getString(R.string.last_exercise);

            if (workoutPosition < ((circuitTime * items.size()) - 1)) {

                if (position >= items.size()) {
                    position = 0;
                }
                name = " " + items.get(position).getName();

                nextExerciseStaticTextView.setVisibility(View.VISIBLE);
            } else {
                nextExerciseStaticTextView.setVisibility(View.GONE);
            }
            nextExerciseNameTextView.setVisibility(View.VISIBLE);
            nextExerciseNameTextView.setText(name);

        }

    }

    private void startCountDown(int time) {

        LogService.Log("WorkoutActivity", "startCountDown time: " + time);

//        playSoundNextExercise();

        if (!activityIsPaused) {

            if (isRest) {
                restStaticTextView.setVisibility(View.VISIBLE);
            }

            startPauseImageView.setVisibility(View.VISIBLE);
            countDownAndInfoTextView.setVisibility(View.VISIBLE);
            bottomInformationLinearLayout.setVisibility(View.VISIBLE);

//        exerciseTimeInt = time;

            countDownTimer = new CountDownTimer((time + 1) * 1000, 1000) {

                private long prevExerciseTime = 0;

                @Override
                public void onTick(long millisUntilFinished) {

//                --exerciseTimeInt;

                    prevExerciseTime = exerciseMillisTime;

                    exerciseMillisTime = millisUntilFinished;

//                exerciseMillisTime += 6;

                    int tmpTime = (int) (exerciseMillisTime / 1000);

                    LogService.Log("WorkoutActivity", "startCountDown exerciseMillisTime: " + exerciseMillisTime + " theTime: " + tmpTime /*+ " exerciseTimeInt: " + exerciseTimeInt*/ /*+ " round: " + UtilsMethods.getRoundedLongNumber(exerciseMillisTime) + " prevExerciseTime: " + prevExerciseTime*/);

                    playTickSound();
                    countDownAndInfoTextView.setText(getTimeFormat(exerciseMillisTime));
//                countDownAndInfoTextView.setText(getTimeFormat(exerciseTimeInt));
                    if (tmpTime == 1) {
                        secondsLeftOneSoundMediaPlayer.start();
                    } else if (tmpTime == 2) {
                        secondsLeftTwoSoundMediaPlayer.start();
                    } else if (tmpTime == 3) {
                        secondsLeftThreeSoundMediaPlayer.start();
                    } else if (tmpTime == 10 && !isRest) {
                        secondsLeftTenSoundMediaPlayer.start();
                    } else if (tmpTime == 15 && !isRest) {
                        secondsLeftFifteennSoundMediaPlayer.start();
                    }

//                if (startPauseImageView.getVisibility() == View.INVISIBLE || startPauseImageView.getVisibility() == View.GONE) {
//                    startPauseImageView.setVisibility(View.VISIBLE);
//                }

                }

                @Override
                public void onFinish() {
                    startPauseImageView.setVisibility(View.INVISIBLE);
                    finishBeepSoundMediaPlayer.start();
                    countDownAndInfoTextView.setText(getTimeFormat(0));
                    finishCountDownAction();
                }

            };
            countDownTimer.start();

        }

    }

    private String getTimeFormat(long millisUntilFinished) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
    }

    private String getTimeFormat(int timeInInt) {
        return String.format("%02d:%02d", timeInInt / 100, timeInInt % 100);
    }

    private View.OnClickListener startPauseImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tmpTime = (int) (exerciseMillisTime / 1000);

            if (tmpTime > 1) {

                if (!isPauase) {
                    pauseExercise(true);
                } else {
                    whichFlipIsAnimated = 0;
                    startFlipAnimation(backgroundFlip1ImageView);
                }
            } else {
                LogService.Log("WorkoutActivity", " startPauseImageViewOnClickListener no action tmpTime: " + tmpTime + " isPauase: " + isPauase);
                if (isPauase) {
                    exerciseMillisTime = restTime * 1000;
                    whichFlipIsAnimated = 0;
                    startFlipAnimation(backgroundFlip1ImageView);
                }
            }

        }
    };

    private void pauseExercise(boolean isShowAnimation) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        isPauase = true;
        startPauseImageView.setImageResource(R.drawable.resume_btn);
        startPauseImageView.setVisibility(View.VISIBLE);

        if (isShowAnimation) {
            navigationBarContainerRelativeLayout.setVisibility(View.INVISIBLE);
        }
        exerciseNumberOfTextView.setVisibility(View.INVISIBLE);
        exerciseInformationLinearLayout.setVisibility(View.VISIBLE);

        String infoText = "";

        if (isRest) {
            restStaticTextView.setVisibility(View.INVISIBLE);
        } else {
            infoText = getResources().getString(R.string.workout_paused);
            bottomInformationLinearLayout.setVisibility(View.INVISIBLE);
        }

        countDownAndInfoTextView.setText(infoText);

        LogService.Log("WorkoutActivity", " pauseExercise showAnimation: " + isShowAnimation);

        if (isShowAnimation) {
            startPauseAnimation();
        }

    }

    private void reStartExercise() {

        isPauase = false;
        startPauseImageView.setImageResource(R.drawable.pause_button);

        exerciseInformationLinearLayout.setVisibility(View.INVISIBLE);
        leftNavigatinRelativeLayout.setVisibility(View.INVISIBLE);
        rightNavigatinRelativeLayout.setVisibility(View.INVISIBLE);

        startPauseImageView.setVisibility(View.INVISIBLE);
        navigationBarContainerRelativeLayout.setVisibility(View.VISIBLE);

        if (isRest) {
            restStaticTextView.setVisibility(View.VISIBLE);
        } else {
            exerciseNumberOfTextView.setVisibility(View.VISIBLE);
            countDownAndInfoTextView.setText("");
            bottomInformationLinearLayout.setVisibility(View.VISIBLE);
        }

        if (!isRest) {
            int time = (int) (exerciseMillisTime / 1000);
            readyGoSoundMediaPlayer.start();
        } else {
            startPauseImageView.setVisibility(View.VISIBLE);
            int time = (int) (exerciseMillisTime / 1000);
            startCountDown(time);
        }

    }

    private void finishCountDownAction() {

        LogService.Log("WorkoutActivity", "finishCountDownAction workoutPosition: " + workoutPosition + " size: " + ((circuitTime * items.size() - 1)));

        if (workoutPosition == (circuitTime * items.size() - 1)) {
            //last exercise
            finishWorkoutAction();
        } else {

            startPauseImageView.setVisibility(View.INVISIBLE);
            countDownAndInfoTextView.setVisibility(View.INVISIBLE);
            bottomInformationLinearLayout.setVisibility(View.INVISIBLE);

            if (isRest) {
                isRest = false;
                ++workoutPosition;
                ++positionInItems;
                restStaticTextView.setVisibility(View.GONE);
                loadContent(exerciseTime);
            } else {
                isRest = true;
//                restStaticTextView.setVisibility(View.VISIBLE);
                loadContent(restTime);
            }

            whichFlipIsAnimated = 0;
            startFlipAnimation(backgroundFlip1ImageView);
        }


    }

    private void startPauseAnimation() {

        Animation animation = AnimationUtils.loadAnimation(WorkoutActivity.this,
                R.anim.pause_up_down_translation_animation);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                pauseOverlayImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                backgroundFlip1ImageView.setImageResource(R.drawable.paused_bg_1);
                backgroundFlip2ImageView.setImageResource(R.drawable.paused_bg_2);
                backgroundFlip3ImageView.setImageResource(R.drawable.paused_bg_3);
                pauseOverlayImageView.setVisibility(View.GONE);

                leftNavigatinRelativeLayout.setVisibility(View.VISIBLE);
                startPauseImageView.setVisibility(View.VISIBLE);
                rightNavigatinRelativeLayout.setVisibility(View.VISIBLE);

                navigationBarContainerRelativeLayout.setVisibility(View.VISIBLE);

                pauseExercise(false);

                LogService.Log("WorkoutActivity", "startPauseAnimation onAnimationEnd");

                animation.reset();
                animation = null;
                System.gc();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pauseOverlayImageView.startAnimation(animation);

    }

    private void startFlipAnimation(final ImageView imageView) {

        long delay = 0;

//        if (whichFlipIsAnimated == 0) {
//            delay = 1000;
//        }

        if (whichFlipIsAnimated == 0) {
            Random random = new Random();
            randomFlipedImagePosition = random.nextInt((flipedImage1Ids.length - 1) - 0) + 0;
        }
//        LogService.Log();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                flipAnimatorSet = null;
                System.gc();

                flipAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(WorkoutActivity.this, R.animator.flip_animation);
                flipAnimatorSet.setTarget(imageView);
                flipAnimatorSet.addListener(flipAnimationListener);
                flipAnimatorSet.start();

            }
        }, delay);

    }

    private Animator.AnimatorListener flipAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

            if (isPauase) {
                countDownAndInfoTextView.setText("");
                exerciseInformationLinearLayout.setVisibility(View.INVISIBLE);
                leftNavigatinRelativeLayout.setVisibility(View.INVISIBLE);
                rightNavigatinRelativeLayout.setVisibility(View.INVISIBLE);
                navigationBarContainerRelativeLayout.setVisibility(View.INVISIBLE);
            }

            switch (whichFlipIsAnimated) {

                case 0:
                    if (isRest) {
                        backgroundFlip1ImageView.setImageResource(R.drawable.rest_bg_1);
                    } else {

                        if (items.get(positionInItems).getFlip1ImageName() != null) {
                            backgroundFlip1ImageView.setImageResource(getResources()
                                    .getIdentifier(items.get(positionInItems).getFlip1ImageName(), "drawable", getPackageName()));
                        } else {
                            backgroundFlip1ImageView.setImageResource(flipedImage1Ids[randomFlipedImagePosition]);
                        }

                    }
                    break;
                case 1:
                    if (isRest) {
                        backgroundFlip2ImageView.setImageResource(R.drawable.rest_bg_2);
                    } else {

                        if (items.get(positionInItems).getFlip2ImageName() != null) {
                            backgroundFlip2ImageView.setImageResource(getResources()
                                    .getIdentifier(items.get(positionInItems).getFlip2ImageName(), "drawable", getPackageName()));
                        } else {
                            backgroundFlip2ImageView.setImageResource(flipedImage2Ids[randomFlipedImagePosition]);
                        }

                    }
                    break;
                case 2:
                    if (isRest) {
                        backgroundFlip3ImageView.setImageResource(R.drawable.rest_bg_3);
                    } else {
                        if (items.get(positionInItems).getFlip3ImageName() != null) {
                            backgroundFlip3ImageView.setImageResource(getResources()
                                    .getIdentifier(items.get(positionInItems).getFlip3ImageName(), "drawable", getPackageName()));
                        } else {
                            backgroundFlip3ImageView.setImageResource(flipedImage3Ids[randomFlipedImagePosition]);
                        }

                    }
                    break;
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animator animation) {

            flipAnimatorSet = null;
            System.gc();


            whichFlipIsAnimated += 1;

            switch (whichFlipIsAnimated) {
                case 0:
                    startFlipAnimation(backgroundFlip1ImageView);
                    break;
                case 1:
                    startFlipAnimation(backgroundFlip2ImageView);
                    break;
                case 2:
                    startFlipAnimation(backgroundFlip3ImageView);
                    break;
                case 3:
                    if (isPauase) {
                        reStartExercise();
                    }
                    break;
            }

            LogService.Log("WorkoutActivity", "onAnimationEnd whichFlipIsAnimated: " + whichFlipIsAnimated);


        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // TODO Auto-generated method stub

        }
    };

    private View.OnClickListener closeImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            onBackPressedAction();

        }
    };

    private void onBackPressedAction() {

        boolean canBePaused = true;

        if ((flipAnimatorSet != null && flipAnimatorSet.isRunning()) || (nextExerciseSoundMediaPlayer != null && nextExerciseSoundMediaPlayer.isPlaying()) || (readyGoSoundMediaPlayer != null && readyGoSoundMediaPlayer.isPlaying()) || (exerciseSoundMediaPlayer != null && exerciseSoundMediaPlayer.isPlaying())) {
            canBePaused = false;
        }

        if (canBePaused) {

            if (!isPauase) {
                pauseExercise(true);
            }

            UtilsMethods.showInformationDialogTwoButtons(WorkoutActivity.this, getResources().getString(R.string.question_quit_workout), getResources().getString(R.string.yes_sure), getResources().getString(R.string.no_keep_going), closeDialogResponseManager);

        }

    }

    private DialogResponseManager closeDialogResponseManager = new DialogResponseManager() {
        @Override
        public void responseArrived(boolean isOkay) {
            if (isOkay) {
                finish();
            }
        }
    };

    private View.OnClickListener exerciseInformationLinearLayoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            LogService.Log("HomeWorkoutListAdapter", "< on item click >");
            Intent intent = new Intent(WorkoutActivity.this, ExerciseInfoActivity.class);

            int pos = positionInItems;

            if (isRest) {
                pos += 1;
            }
            if (pos >= 0 && pos < items.size()) {
                intent.putExtra(ExerciseInfoActivity.EXERCISE_ACTIVITY_EXTRA_INFO, new Gson().toJson(items.get(pos)));
            }

            startActivity(intent);

        }
    };

    private View.OnClickListener leftNavigatinRelativeLayoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (workoutPosition > 0) {
                --workoutPosition;
                --positionInItems;
                startWorkoutAfterPreviousOrNext();
            }
        }
    };

    private View.OnClickListener rightNavigatinRelativeLayoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (workoutPosition < (circuitTime * items.size() - 1)) {
                ++workoutPosition;
                ++positionInItems;
                startWorkoutAfterPreviousOrNext();
            } else {
                finishWorkoutAction();
            }
        }
    };

    private void startWorkoutAfterPreviousOrNext() {

        isPauase = false;
        isRest = false;

        startPauseImageView.setVisibility(View.INVISIBLE);

        startPauseImageView.setImageResource(R.drawable.pause_button);

        exerciseInformationLinearLayout.setVisibility(View.INVISIBLE);
        leftNavigatinRelativeLayout.setVisibility(View.INVISIBLE);
        rightNavigatinRelativeLayout.setVisibility(View.INVISIBLE);

        exerciseNumberOfTextView.setVisibility(View.VISIBLE);
        countDownAndInfoTextView.setText("");
        bottomInformationLinearLayout.setVisibility(View.VISIBLE);

        restStaticTextView.setVisibility(View.GONE);
        loadContent(exerciseTime);

        whichFlipIsAnimated = 0;
        startFlipAnimation(backgroundFlip1ImageView);

    }

    private void finishWorkoutAction() {
        isFinish = true;
        Intent intent = new Intent(WorkoutActivity.this, WorkoutCompletedActivity.class);
        intent.putExtra(EXTRA_DATE_WORKOUT_ID, workoutId);
        startActivity(intent);
        finish();
    }

    private void showAdmobBanner() {

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."

        if (UtilsMethods.getBooleanFromSharedPreferences(WorkoutActivity.this, MainActivity.SETTINGS_IAP_ITEM, false) || UtilsMethods.getBooleanFromSharedPreferences(WorkoutActivity.this, MainActivity.ALL_IN_ONE_IAP_ITEM, false)) {
            admobAdView.setVisibility(View.GONE);
            ((RelativeLayout.LayoutParams) containerRelativeLayout.getLayoutParams()).addRule(RelativeLayout.ABOVE,0);
        } else {
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice("E26F9502A8C20A51B2229BCF512B62DA")
//                    .build();
            AdRequest adRequest = new AdRequest.Builder().build();
            // Start loading the ad in the background.
            admobAdView.loadAd(adRequest);
        }


    }

}
