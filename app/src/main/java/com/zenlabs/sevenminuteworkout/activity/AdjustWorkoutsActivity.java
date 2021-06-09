package com.zenlabs.sevenminuteworkout.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.utils.AdjustTimeEnum;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

public class AdjustWorkoutsActivity extends Activity {

    private RelativeLayout firstItemContainerRelativeLayout, seconItemContainerRelativeLayout, thirdContainerItemRelativeLayout;
    private RelativeLayout firstItemRelativeLayout, secondItRelativeLayout, thirdItemRelativeLayout;

    private ImageView exerciseFirstImageView, exerciseSecondImageView, exerciseThirdImageView;
    private TextView exerciseFirstTextView, exerciseSecondTextView, exerciseThirdTextView;
    private TextView exerciseTextView;

    private ImageView restFirstImageView, restSecondImageView, restThirdImageView;
    private TextView restFirstTextView, restSecondTextView, restThirdTextView;
    private TextView restTextView;

    private ImageView circleFirstImageView, circleSecondImageView, circleThirdImageView;
    private TextView circleFirstTextView, circleSecondTextView, circleThirdTextView;
    private TextView circleTextView;

    private int selectedRestTime, selectedExerciseTime, seletedCircuitTime;

    private Button saveMySettingsButton;

    private AnimatorSet flipAnimatorSet;
    private int whichLayoutAnimated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_workouts);

        initViews();

        setTimes();

        initTimeTexts();

        initTimesSelected();

        setOnClickListeners();

        startFlipAnimation(firstItemContainerRelativeLayout);

    }

    private void initViews() {

        LayoutInflater tmpInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageView crossImageView = (ImageView) findViewById(R.id.adjustWorkoutActionBarBackImageView);
        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        firstItemContainerRelativeLayout = (RelativeLayout) findViewById(R.id.adjustWorkoutActivityFirstItemRelativeLayout);
        seconItemContainerRelativeLayout = (RelativeLayout) findViewById(R.id.adjustWorkoutActivitySecondItemRelativeLayout);
        thirdContainerItemRelativeLayout = (RelativeLayout) findViewById(R.id.adjustWorkoutActivityThirdItemRelativeLayout);

        firstItemRelativeLayout = (RelativeLayout) tmpInflater.inflate(R.layout.adjust_workout_item_layout, firstItemContainerRelativeLayout, false);
        exerciseTextView = (TextView) firstItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemTitleTextView);

        exerciseFirstTextView = (TextView) firstItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemFirstTextView);
        exerciseSecondTextView = (TextView) firstItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemSeondTextView);
        exerciseThirdTextView = (TextView) firstItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemThirdTextView);

        exerciseFirstImageView = (ImageView) firstItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemFirstHexaImageView);
        exerciseSecondImageView = (ImageView) firstItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemSeondHexaImageView);
        exerciseThirdImageView = (ImageView) firstItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemThirdHexaImageView);

        firstItemContainerRelativeLayout.addView(firstItemRelativeLayout);

        secondItRelativeLayout = (RelativeLayout) tmpInflater.inflate(R.layout.adjust_workout_item_layout, seconItemContainerRelativeLayout, false);
        restTextView = (TextView) secondItRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemTitleTextView);

        restFirstTextView = (TextView) secondItRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemFirstTextView);
        restSecondTextView = (TextView) secondItRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemSeondTextView);
        restThirdTextView = (TextView) secondItRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemThirdTextView);

        restFirstImageView = (ImageView) secondItRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemFirstHexaImageView);
        restSecondImageView = (ImageView) secondItRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemSeondHexaImageView);
        restThirdImageView = (ImageView) secondItRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemThirdHexaImageView);

        seconItemContainerRelativeLayout.addView(secondItRelativeLayout);

        thirdItemRelativeLayout = (RelativeLayout) tmpInflater.inflate(R.layout.adjust_workout_item_layout, thirdContainerItemRelativeLayout, false);
        circleTextView = (TextView) thirdItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemTitleTextView);

        circleFirstTextView = (TextView) thirdItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemFirstTextView);
        circleSecondTextView = (TextView) thirdItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemSeondTextView);
        circleThirdTextView = (TextView) thirdItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemThirdTextView);

        circleFirstImageView = (ImageView) thirdItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemFirstHexaImageView);
        circleSecondImageView = (ImageView) thirdItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemSeondHexaImageView);
        circleThirdImageView = (ImageView) thirdItemRelativeLayout.findViewById(R.id.adjustWorkoutActivityItemThirdHexaImageView);

        thirdContainerItemRelativeLayout.addView(thirdItemRelativeLayout);

//        firstItemContainerRelativeLayout.setVisibility(View.VISIBLE);
//        seconItemContainerRelativeLayout.setVisibility(View.VISIBLE);
//        thirdContainerItemRelativeLayout.setVisibility(View.VISIBLE);

        saveMySettingsButton = (Button) findViewById(R.id.adjustWorkoutActivitySaveButton);

    }

    private void setOnClickListeners() {

        exerciseFirstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedExerciseTime = Integer.valueOf(exerciseFirstTextView.getText().toString());
                initTimesSelected();

            }
        });

        exerciseSecondImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedExerciseTime = Integer.valueOf(exerciseSecondTextView.getText().toString());
                initTimesSelected();

            }
        });

        exerciseThirdImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedExerciseTime = Integer.valueOf(exerciseThirdTextView.getText().toString());
                initTimesSelected();

            }
        });

        circleFirstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seletedCircuitTime = Integer.valueOf(circleFirstTextView.getText().toString());
                initTimesSelected();

            }
        });

        circleSecondImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seletedCircuitTime = Integer.valueOf(circleSecondTextView.getText().toString());
                initTimesSelected();

            }
        });

        circleThirdImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seletedCircuitTime = Integer.valueOf(circleThirdTextView.getText().toString());
                initTimesSelected();

            }
        });

        restFirstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedRestTime = Integer.valueOf(restFirstTextView.getText().toString());
                initTimesSelected();

            }
        });

        restSecondTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedRestTime = Integer.valueOf(restSecondTextView.getText().toString());
                initTimesSelected();

            }
        });

        restThirdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedRestTime = Integer.valueOf(restThirdTextView.getText().toString());
                initTimesSelected();

            }
        });

        saveMySettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsMethods.saveIntInSharedPreferences(AdjustWorkoutsActivity.this, UtilsValues.SHARED_PREFERENCES_EXERCISE_TIME, selectedExerciseTime);
                UtilsMethods.saveIntInSharedPreferences(AdjustWorkoutsActivity.this, UtilsValues.SHARED_PREFERENCES_REST_TIME, selectedRestTime);
                UtilsMethods.saveIntInSharedPreferences(AdjustWorkoutsActivity.this, UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME, seletedCircuitTime);
                onBackPressed();
            }
        });

    }

    private void setTimes() {

        selectedExerciseTime = UtilsMethods.getAdjustTimeFromSharedPreferences(AdjustWorkoutsActivity.this, UtilsValues.SHARED_PREFERENCES_EXERCISE_TIME);
        selectedRestTime = UtilsMethods.getAdjustTimeFromSharedPreferences(AdjustWorkoutsActivity.this, UtilsValues.SHARED_PREFERENCES_REST_TIME);
        seletedCircuitTime = UtilsMethods.getAdjustTimeFromSharedPreferences(AdjustWorkoutsActivity.this, UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME);

    }

    private void initTimeTexts() {

        for (int i = 0; i < AdjustTimeEnum.EXERCISE_TIMES.length; ++i) {
            switch (i) {
                case 0:
                    exerciseFirstTextView.setText(AdjustTimeEnum.EXERCISE_TIMES[i] + "");
                    break;
                case 1:
                    exerciseSecondTextView.setText(AdjustTimeEnum.EXERCISE_TIMES[i] + "");
                    break;
                case 2:
                    exerciseThirdTextView.setText(AdjustTimeEnum.EXERCISE_TIMES[i] + "");
                    break;
            }
        }

        for (int i = 0; i < AdjustTimeEnum.REST_TIMES.length; ++i) {
            switch (i) {
                case 0:
                    restFirstTextView.setText(AdjustTimeEnum.REST_TIMES[i] + "");
                    break;
                case 1:
                    restSecondTextView.setText(AdjustTimeEnum.REST_TIMES[i] + "");
                    break;
                case 2:
                    restThirdTextView.setText(AdjustTimeEnum.REST_TIMES[i] + "");
                    break;
            }
        }

        for (int i = 0; i < AdjustTimeEnum.CIRCUIT_TIMES.length; ++i) {
            switch (i) {
                case 0:
                    circleFirstTextView.setText(AdjustTimeEnum.CIRCUIT_TIMES[i] + "");
                    break;
                case 1:
                    circleSecondTextView.setText(AdjustTimeEnum.CIRCUIT_TIMES[i] + "");
                    break;
                case 2:
                    circleThirdTextView.setText(AdjustTimeEnum.CIRCUIT_TIMES[i] + "");
                    break;
            }
        }

        exerciseTextView.setText(getResources().getString(R.string.exercise_time_in_seconds));
        restTextView.setText(getResources().getString(R.string.rest_time_in_seconds));
        circleTextView.setText(getResources().getString(R.string.number_of_circuits));

    }

    private void initTimesSelected() {

        exerciseFirstImageView.setImageResource(R.drawable.hexa_outline_blue);
        exerciseSecondImageView.setImageResource(R.drawable.hexa_outline_blue);
        exerciseThirdImageView.setImageResource(R.drawable.hexa_outline_blue);

        restFirstImageView.setImageResource(R.drawable.hexa_outline_blue);
        restSecondImageView.setImageResource(R.drawable.hexa_outline_blue);
        restThirdImageView.setImageResource(R.drawable.hexa_outline_blue);

        circleFirstImageView.setImageResource(R.drawable.hexa_outline_blue);
        circleSecondImageView.setImageResource(R.drawable.hexa_outline_blue);
        circleThirdImageView.setImageResource(R.drawable.hexa_outline_blue);

        int indexExercise = 0;
        int indexRest = 0;
        int indexCicuit = 0;

        for (int i = 0; i < AdjustTimeEnum.EXERCISE_TIMES.length; ++i) {
            if (selectedExerciseTime == AdjustTimeEnum.EXERCISE_TIMES[i]) {
                indexExercise = i;
                break;
            }
        }

        for (int i = 0; i < AdjustTimeEnum.REST_TIMES.length; ++i) {
            if (selectedRestTime == AdjustTimeEnum.REST_TIMES[i]) {
                indexRest = i;
                break;
            }
        }

        for (int i = 0; i < AdjustTimeEnum.CIRCUIT_TIMES.length; ++i) {
            if (seletedCircuitTime == AdjustTimeEnum.CIRCUIT_TIMES[i]) {
                indexCicuit = i;
                break;
            }
        }

        setSelectedHexaImagesBackground(indexExercise, indexRest, indexCicuit);

    }

    private void setSelectedHexaImagesBackground(int indexExercise, int indexRest, int indexCicuit) {

        switch (indexExercise) {
            case 0:
                exerciseFirstImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
            case 1:
                exerciseSecondImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
            case 2:
                exerciseThirdImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
        }

        switch (indexRest) {
            case 0:
                restFirstImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
            case 1:
                restSecondImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
            case 2:
                restThirdImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
        }

        switch (indexCicuit) {
            case 0:
                circleFirstImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
            case 1:
                circleSecondImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
            case 2:
                circleThirdImageView.setImageResource(R.drawable.selected_blue_hex);
                break;
        }

    }

    private void startFlipAnimation(final RelativeLayout layout) {
        long delay = 0;

        if (whichLayoutAnimated == 0) {
            delay = 1000;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flipAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(AdjustWorkoutsActivity.this, R.animator.flip_x_animation);
                flipAnimatorSet.setTarget(layout);
                flipAnimatorSet.addListener(flipAnimationListener);
                flipAnimatorSet.start();
            }
        }, delay);
    }

    private Animator.AnimatorListener flipAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
            switch (whichLayoutAnimated) {
                case 0:
                    firstItemContainerRelativeLayout.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    seconItemContainerRelativeLayout.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    thirdContainerItemRelativeLayout.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    //show behind items
                    break;
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animator animation) {

            whichLayoutAnimated += 1;

            switch (whichLayoutAnimated) {
                case 0:
                    startFlipAnimation(firstItemContainerRelativeLayout);
                    break;
                case 1:
                    startFlipAnimation(seconItemContainerRelativeLayout);
                    break;
                case 2:
                    startFlipAnimation(thirdContainerItemRelativeLayout);
                    break;
                case 3:
                    //
                    showAnimatedSaveButton();
                    break;
            }

            LogService.Log("AdjustWorkoutActivity", "onAnimationEnd whichLayoutAnimated: " + whichLayoutAnimated);


        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // TODO Auto-generated method stub

        }
    };

    private void showAnimatedSaveButton() {

        Animation animation = AnimationUtils.loadAnimation(AdjustWorkoutsActivity.this,
                R.anim.translate_bottom_top_animation);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                saveMySettingsButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        saveMySettingsButton.startAnimation(animation);

    }

}
