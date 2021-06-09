package com.zenlabs.sevenminuteworkout.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.activity.MainActivity;
import com.zenlabs.sevenminuteworkout.activity.WorkoutActivity;
import com.zenlabs.sevenminuteworkout.adapter.HomeWorkoutListAdapter;
import com.zenlabs.sevenminuteworkout.adapter.ImageViewPagerAdapter;
import com.zenlabs.sevenminuteworkout.database.Achievement;
import com.zenlabs.sevenminuteworkout.database.CompletedWorkout;
import com.zenlabs.sevenminuteworkout.database.Exercise;
import com.zenlabs.sevenminuteworkout.database.ExerciseForWorkout;
import com.zenlabs.sevenminuteworkout.database.Workout;
import com.zenlabs.sevenminuteworkout.utils.AchievementsCheckBadgesUtils;
import com.zenlabs.sevenminuteworkout.utils.AdjustTimeEnum;
import com.zenlabs.sevenminuteworkout.utils.ImageViewPageIndicatorManager;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.ResponseManagerString;
import com.zenlabs.sevenminuteworkout.utils.SliderView;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

import java.util.ArrayList;

/**
 * Created by madarashunor on 12/10/15.
 */
public class HomeFragment extends Fragment {

    private int selectedWorkout;

    private ArrayList<Workout> workouts;
    private ArrayList<Bitmap> workoutBitmaps;
    private ViewPager imageViewPager;
    private ImageViewPagerAdapter imageViewPagerAdapter;

    private ResponseManagerString homeFragmentResponseManagerString;

    private ArrayList<ExerciseForWorkout> exerciseForWorkouts;
    private ArrayList<Exercise> exercises;

    private ArrayList<RadioButton> radioButtonsViewPageIndicators;
    private RadioGroup pageIndicatorRadioGroup;

    private int[] imgaPagerDrawableIds = {R.drawable.ex1_bg_image, R.drawable.ex2_bg_image, R.drawable.ex3_bg_image, R.drawable.ex4_bg_image};

    private AnimatorSet flipAnimatorSet;
    private int whichImageAnimated = 0;
    private LinearLayout flipLinearLayout;
    private ImageView flip1ImageView, flip2ImageView, flip3ImageView, flip4ImageView, flip5ImageView;

    private int whichButtonLayoutAnimated = 0;
    private TextView exerciseTimeTextView, restTimeTextView, circuitTimeTextView;
    private LinearLayout exerciseContainerLinearLayout, restContainerLinearLayout, circuitContainerLinearLayout;
    private LinearLayout exerciseLeftSideLinearLayout, restLeftSideLinearLayout, circuitLeftSideLinearLayout;
    private LinearLayout exerciseButtonsLinearLayout, restButtonsLinearLayout, circuitButtonsLinearLayout;
    private ImageView exerciseArrowUpImageView, exerciseArrowDownImageView, restArrowUpImageView, restArrowDownImageView, circuitArrowUpImageView, circuitArrowDownImageView;

    private LinearLayout listViewContainerLinearLayout;
    private LinearLayout workoutExerciseListView;
    private HomeWorkoutListAdapter listAdapter;
    private int listItemViewSize = 0;

    private RelativeLayout bottomRelativeLayout;
    private RelativeLayout bottomTextRelativeLayout;

    private ImageView startExerciseImageView;
    private ImageView bottomTextBackgroundImageView;
    //    private ImageView bottomTextImageView;
    private SliderView sliderView;

    private TextView unlockTextView;

    private ResponseManagerString goProResponseManagerString;

    private boolean isWorkOutStarted = false;
    private float startX = 0;

    private ArrayList<Achievement> achievements;
    private ArrayList<CompletedWorkout> completedWorkouts;
    private int specificAchivementPosition = -1;

    public HomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,
                null);

        RelativeLayout baseRelativeLayout = (RelativeLayout) view.findViewById(R.id.homeScreenMainBaseRelativeLayout);
        baseRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageViewPager = (ViewPager) view.findViewById(R.id.homeScreenViewPager);

        flipLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenFlipLinearLayout);

        flip1ImageView = (ImageView) view.findViewById(R.id.homeScreenFlip1ImageView);
        flip2ImageView = (ImageView) view.findViewById(R.id.homeScreenFlip2ImageView);
        flip3ImageView = (ImageView) view.findViewById(R.id.homeScreenFlip3ImageView);
        flip4ImageView = (ImageView) view.findViewById(R.id.homeScreenFlip4ImageView);
        flip5ImageView = (ImageView) view.findViewById(R.id.homeScreenFlip5ImageView);

        pageIndicatorRadioGroup = (RadioGroup) view.findViewById(R.id.homeScreenPageIndicatorRadioGroup);

        exerciseContainerLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenExerciseContainerLayout);
        restContainerLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenRestContainerLayout);
        circuitContainerLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenCircuitContainerLayout);

        exerciseButtonsLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenExerciseLinearLayout);
        exerciseLeftSideLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenExerciseLeftSideLinearLayout);

        restButtonsLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenRestLinearLayout);
        restLeftSideLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenRestLeftSideLinearLayout);

        circuitButtonsLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenCircuitLinearLayout);
        circuitLeftSideLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenCircuitLeftSideLinearLayout);

        exerciseLeftSideLinearLayout.setOnClickListener(exerciseLeftSideLinearLayoutOnClickListener);
        restLeftSideLinearLayout.setOnClickListener(restLeftSideLinearLayoutOnClickListener);
        circuitLeftSideLinearLayout.setOnClickListener(circuitLeftSideLinearLayoutOnClickListener);

        exerciseArrowUpImageView = (ImageView) view.findViewById(R.id.homeScreenExerciseButtonUpArrow);
        exerciseArrowUpImageView.setOnClickListener(exerciseArrowUpImageViewOnClickListener);

        exerciseArrowDownImageView = (ImageView) view.findViewById(R.id.homeScreenExerciseButtonDownArrow);
        exerciseArrowDownImageView.setOnClickListener(exerciseArrowDownImageViewOnClickListener);

        restArrowUpImageView = (ImageView) view.findViewById(R.id.homeScreenRestButtonUpArrow);
        restArrowUpImageView.setOnClickListener(restArrowUpImageViewOnClickListener);

        restArrowDownImageView = (ImageView) view.findViewById(R.id.homeScreenRestButtonDownArrow);
        restArrowDownImageView.setOnClickListener(restArrowDownImageViewOnClickListener);

        circuitArrowUpImageView = (ImageView) view.findViewById(R.id.homeScreenCircuitButtonUpArrow);
        circuitArrowUpImageView.setOnClickListener(circuitArrowUpImageViewOnClickListener);

        circuitArrowDownImageView = (ImageView) view.findViewById(R.id.homeScreenCircuitButtonDownArrow);
        circuitArrowDownImageView.setOnClickListener(circuitArrowDownImageViewOnClickListener);

        exerciseTimeTextView = (TextView) view.findViewById(R.id.homeScreenExerciseSecTextView);
        restTimeTextView = (TextView) view.findViewById(R.id.homeScreenRestSecTextView);
        circuitTimeTextView = (TextView) view.findViewById(R.id.homeScreenCircuitSecTextView);

        initTimes();

        listViewContainerLinearLayout = (LinearLayout) view.findViewById(R.id.homeScreenListViewContainerLinearLayout);
        workoutExerciseListView = (LinearLayout) view.findViewById(R.id.homeScreenListView);

        bottomRelativeLayout = (RelativeLayout) view.findViewById(R.id.homeScreenBottomContainerLayout);
        bottomTextRelativeLayout = (RelativeLayout) view.findViewById(R.id.homeScreenBottomLayout);
//        bottomTextRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                bottomTextRelativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                float x = bottomTextRelativeLayout.getX();
//                LogService.Log("bottomTextRelativeLayout.getViewTreeObserver", "x: "+x);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bottomTextRelativeLayout.getLayoutParams();
//                layoutParams.addRule(RelativeLayout.RIGHT_OF, 0);
//                bottomTextRelativeLayout.setLayoutParams(layoutParams);
//                bottomTextRelativeLayout.setX(x);
//            }
//        });


        sliderView = (SliderView) view.findViewById(R.id.slide);
        sliderView.setText(getResources().getString(R.string.slide_to_start_workout));

        startExerciseImageView = (ImageView) view.findViewById(R.id.homeScreenStartExerciseImageView);
        startExerciseImageView.setOnTouchListener(startExerciseImageViewOnTouchListener);

        bottomTextBackgroundImageView = (ImageView) view.findViewById(R.id.homeScreenBottomTextBackgroundImageView);
//        bottomTextImageView = (ImageView) view.findViewById(R.id.homeScreenBottomTextImageView);

        unlockTextView = (TextView) view.findViewById(R.id.homeScreenUnlockTextView);
        unlockTextView.setOnClickListener(unlockTextViewOnClickListener);

        initItems();

        startFlipAnimation(flip1ImageView);

        if (getActivity() != null && workouts!=null && workouts.size()>0) {

            homeFragmentResponseManagerString.responseArrived(workouts.get(selectedWorkout).getName());

        }

        return view;
    }

    @Override
    public void onResume() {
        if (isWorkOutStarted) {
            isWorkOutStarted = false;
            RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams) startExerciseImageView.getLayoutParams());
            LogService.Log("HomeFragment", "startX: " + startX);
            layoutParams.leftMargin = (int) startX;
            startExerciseImageView.setLayoutParams(layoutParams);
            sliderView.setAlpha(1);
        }
        LogService.Log("HomeFragment","onResume startX: "+startX + " isWorkOutStarted: "+isWorkOutStarted);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public ArrayList<CompletedWorkout> getCompletedWorkouts() {
        return completedWorkouts;
    }

    public void setCompletedWorkouts(ArrayList<CompletedWorkout> completedWorkouts) {
        this.completedWorkouts = completedWorkouts;
    }

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public int getSpecificAchivementPosition() {
        return specificAchivementPosition;
    }

    public void setSpecificAchivementPosition(int specificAchivementPosition) {
        this.specificAchivementPosition = specificAchivementPosition;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public void setExerciseForWorkouts(ArrayList<ExerciseForWorkout> exerciseForWorkouts) {
        this.exerciseForWorkouts = exerciseForWorkouts;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void setListItemViewSize(int listItemViewSize) {
        this.listItemViewSize = listItemViewSize;
    }

    public ResponseManagerString getHomeFragmentResponseManagerString() {
        return homeFragmentResponseManagerString;
    }

    public void setHomeFragmentResponseManagerString(ResponseManagerString homeFragmentResponseManagerString) {
        this.homeFragmentResponseManagerString = homeFragmentResponseManagerString;
    }

    public ResponseManagerString getGoProResponseManagerString() {
        return goProResponseManagerString;
    }

    public void setGoProResponseManagerString(ResponseManagerString goProResponseManagerString) {
        this.goProResponseManagerString = goProResponseManagerString;
    }

    private void initItems() {

        initImagePager();

    }

    private void initImagePager() {

        workoutBitmaps = new ArrayList<Bitmap>();

        if(workouts==null){
            workouts = new ArrayList<Workout>();
        }

        for (int i = 0; i < workouts.size(); ++i) {

            if (i < imgaPagerDrawableIds.length) {
                workoutBitmaps.add(BitmapFactory.decodeResource(getResources(),
                        imgaPagerDrawableIds[i]));
            } else {
                workoutBitmaps.add(null);
            }
        }

        imageViewPagerAdapter = new ImageViewPagerAdapter(getActivity(),
                workoutBitmaps, imageViewPageIndicatorManager);

        imageViewPager.setAdapter(imageViewPagerAdapter);

        initialisePageIndicators();

    }

    private void initialisePageIndicators() {

        RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(
                getActivity(), null);
        params_soiled.setMargins(
                getResources().getDimensionPixelSize(
                        R.dimen.home_screen_indicator_size),
                0,
                getResources().getDimensionPixelSize(
                        R.dimen.home_screen_indicator_size), 0);

        radioButtonsViewPageIndicators = new ArrayList<RadioButton>();
        for (int i = 0; i < workouts.size(); ++i) {
            RadioButton rB = new RadioButton(getActivity());
            rB.setButtonDrawable(android.R.color.transparent);
            rB.setHeight(getResources().getDimensionPixelSize(
                    R.dimen.home_screen_indicator_size));
            rB.setWidth(getResources().getDimensionPixelSize(
                    R.dimen.home_screen_indicator_size));
//            rB.setBackgroundResource(R.drawable.page_indicator_background_selector);
            rB.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.page_indicator_background_selector));
            rB.setLayoutParams(params_soiled);
            radioButtonsViewPageIndicators.add(rB);
            pageIndicatorRadioGroup.addView(rB);
        }

        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                imageViewPageIndicatorManager.setSelectedItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        if (radioButtonsViewPageIndicators.size() > 0) {
            radioButtonsViewPageIndicators.get(0).setChecked(true);
        }

        selectedWorkout = 0;

    }

    public void initTimes() {
        exerciseTimeTextView.setText(UtilsMethods.getAdjustTimeFromSharedPreferences(getActivity(), UtilsValues.SHARED_PREFERENCES_EXERCISE_TIME) + " " + getResources().getString(R.string.sec));
        restTimeTextView.setText(UtilsMethods.getAdjustTimeFromSharedPreferences(getActivity(), UtilsValues.SHARED_PREFERENCES_REST_TIME) + " " + getResources().getString(R.string.rest));
        circuitTimeTextView.setText(UtilsMethods.getAdjustTimeFromSharedPreferences(getActivity(), UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME) + " " + getResources().getString(R.string.circuit));
    }

    private ImageViewPageIndicatorManager imageViewPageIndicatorManager = new ImageViewPageIndicatorManager() {
        @Override
        public void setSelectedItem(int positon) {

            LogService.Log("imageViewPageIndicatorManager", "positon: " + positon);
            radioButtonsViewPageIndicators.get(positon).setChecked(true);
            selectedWorkout = positon;

            updateBottomView();

            initListView();

        }
    };

    private void startRightButtonsTranslateRightToLeftAnimation(final LinearLayout linearLayout, final LinearLayout containerLinearLayout, int offSet, final boolean isClick) {

        int startOffset = linearLayout.getWidth();

        boolean isOpen = false;

        if (isClick) {

            isOpen = Boolean.valueOf(linearLayout.getTag().toString());

            if (isOpen) {
                startOffset = 0;
                offSet = linearLayout.getWidth() / 2;
            } else {
                startOffset = 0;
                offSet = -linearLayout.getWidth() / 2;
            }

        }

        TranslateAnimation moveAnim = new TranslateAnimation(startOffset, offSet, 0, 0);
        moveAnim.setDuration(300);
        moveAnim.setFillAfter(true);
        linearLayout.setVisibility(View.VISIBLE);

        final int finalOffSet = offSet;
        final boolean finalIsOpen = isOpen;
        moveAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) containerLinearLayout.getLayoutParams();

                ++whichButtonLayoutAnimated;

                if (!isClick) {

                    layoutParams.setMargins(0, 0, -finalOffSet, 0);
                    containerLinearLayout.setLayoutParams(layoutParams);

                    switch (whichButtonLayoutAnimated) {
                        case 0:
                            startRightButtonsTranslateRightToLeftAnimation(exerciseButtonsLinearLayout, exerciseContainerLinearLayout, exerciseButtonsLinearLayout.getWidth() / 2, false);
                            break;
                        case 1:
                            startRightButtonsTranslateRightToLeftAnimation(restButtonsLinearLayout, restContainerLinearLayout, restButtonsLinearLayout.getWidth() / 2, false);
                            break;
                        case 2:
                            startRightButtonsTranslateRightToLeftAnimation(circuitButtonsLinearLayout, circuitContainerLinearLayout, circuitButtonsLinearLayout.getWidth() / 2, false);
                            break;
                    }
                } else {

                    int offSetClick = 0;

                    if (finalIsOpen) {
                        offSetClick = -linearLayout.getWidth() / 2;
                    }

                    layoutParams.setMargins(0, 0, offSetClick, 0);
                    containerLinearLayout.setLayoutParams(layoutParams);

                    linearLayout.setTag(!finalIsOpen);

                }

                linearLayout.clearAnimation();

                LogService.Log("startRightButtonsTranslateRightToLeftAnimation", "onAnimationEnd whichButtonLayoutAnimated: " + whichButtonLayoutAnimated);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearLayout.startAnimation(moveAnim);

    }

    private void startFlipAnimation(final ImageView imageView) {

        long delay = 0;

        if (whichImageAnimated == 0) {
            delay = 1000;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    flipAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_animation);
                    flipAnimatorSet.setTarget(imageView);
                    flipAnimatorSet.addListener(flipAnimationListener);
                    flipAnimatorSet.start();
                }
            }
        }, delay);


    }

    private Animator.AnimatorListener flipAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
            switch (whichImageAnimated) {
                case 0:
                    flip1ImageView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    flip2ImageView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    flip3ImageView.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    flip4ImageView.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    flip5ImageView.setVisibility(View.VISIBLE);
                    break;
                case 5:
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

            flipAnimatorSet = null;
            System.gc();

            whichImageAnimated += 1;

            switch (whichImageAnimated) {
                case 0:
                    startFlipAnimation(flip1ImageView);
                    break;
                case 1:
                    startFlipAnimation(flip2ImageView);
                    break;
                case 2:
                    startFlipAnimation(flip3ImageView);
                    break;
                case 3:
                    startFlipAnimation(flip4ImageView);
                    break;
                case 4:
                    startFlipAnimation(flip5ImageView);
                    break;
                case 5:
                    //show behind items
                    hideFlipedImagesAndShowViewPager();
                    break;
            }

            LogService.Log("HomeFragment", "onAnimationEnd whichImageAnimated: " + whichImageAnimated);


        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // TODO Auto-generated method stub

        }
    };

    private void hideFlipedImagesAndShowViewPager() {

        flipLinearLayout.setVisibility(View.GONE);

        imageViewPager.setVisibility(View.VISIBLE);
        pageIndicatorRadioGroup.setVisibility(View.VISIBLE);

        initListView();

    }

    private View.OnClickListener exerciseLeftSideLinearLayoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startRightButtonsTranslateRightToLeftAnimation(exerciseButtonsLinearLayout, exerciseContainerLinearLayout, exerciseButtonsLinearLayout.getWidth() / 2, true);
        }
    };

    private View.OnClickListener restLeftSideLinearLayoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startRightButtonsTranslateRightToLeftAnimation(restButtonsLinearLayout, restContainerLinearLayout, restButtonsLinearLayout.getWidth() / 2, true);
        }
    };

    private View.OnClickListener circuitLeftSideLinearLayoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startRightButtonsTranslateRightToLeftAnimation(circuitButtonsLinearLayout, circuitContainerLinearLayout, circuitButtonsLinearLayout.getWidth() / 2, true);
        }
    };

    private int getAdjustTimeOnChange(boolean isIncreasing, int[] array, String key) {

        int value = UtilsMethods.getAdjustTimeFromSharedPreferences(getActivity(), key);
        int index = 0;
        for (int i = 0; i < array.length; ++i) {
            if (value == array[i]) {
                index = i;
            }
        }

        if (isIncreasing) {
            index += 1;
        } else {
            index -= 1;
        }

        if (index >= 0 && index < array.length) {
            value = array[index];
        }

        UtilsMethods.saveIntInSharedPreferences(getActivity(), key, value);

        return value;
    }

    private View.OnClickListener exerciseArrowUpImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogService.Log("HomeFragment", "exerciseArrowUpImageViewOnClickListener");
            exerciseTimeTextView.setText(getAdjustTimeOnChange(true, AdjustTimeEnum.EXERCISE_TIMES, UtilsValues.SHARED_PREFERENCES_EXERCISE_TIME) + " " + getResources().getString(R.string.sec));
        }
    };

    private View.OnClickListener exerciseArrowDownImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogService.Log("HomeFragment", "exerciseArrowDownImageViewOnClickListener");
            exerciseTimeTextView.setText(getAdjustTimeOnChange(false, AdjustTimeEnum.EXERCISE_TIMES, UtilsValues.SHARED_PREFERENCES_EXERCISE_TIME) + " " + getResources().getString(R.string.sec));
        }
    };

    private View.OnClickListener restArrowUpImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogService.Log("HomeFragment", "restArrowUpImageViewOnClickListener");
            restTimeTextView.setText(getAdjustTimeOnChange(true, AdjustTimeEnum.REST_TIMES, UtilsValues.SHARED_PREFERENCES_REST_TIME) + " " + getResources().getString(R.string.rest));
        }
    };

    private View.OnClickListener restArrowDownImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogService.Log("HomeFragment", "restArrowDownImageViewOnClickListener");
            restTimeTextView.setText(getAdjustTimeOnChange(false, AdjustTimeEnum.REST_TIMES, UtilsValues.SHARED_PREFERENCES_REST_TIME) + " " + getResources().getString(R.string.rest));
        }
    };

    private View.OnClickListener circuitArrowUpImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogService.Log("HomeFragment", "circuitArrowUpImageViewOnClickListener");
            circuitTimeTextView.setText(getAdjustTimeOnChange(true, AdjustTimeEnum.CIRCUIT_TIMES, UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME) + " " + getResources().getString(R.string.circuit));
        }
    };

    private View.OnClickListener circuitArrowDownImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogService.Log("HomeFragment", "circuitArrowDownImageViewOnClickListener");
            circuitTimeTextView.setText(getAdjustTimeOnChange(false, AdjustTimeEnum.CIRCUIT_TIMES, UtilsValues.SHARED_PREFERENCES_CIRCUIT_TIME) + " " + getResources().getString(R.string.circuit));
        }
    };

    private void initListView() {

        if (getActivity() != null) {

            homeFragmentResponseManagerString.responseArrived(workouts.get(selectedWorkout).getName());

//        int workoutId = workouts.get(selectedWorkout).getId();
//
//        ArrayList<Integer> exerciseIds = new ArrayList<Integer>();
//
//        for (int i = 0; i < exerciseForWorkouts.size(); ++i) {
//            if (exerciseForWorkouts.get(i).getWorkoutid() == workoutId) {
//                exerciseIds.add(exerciseForWorkouts.get(i).getExerciseId());
//            }
//        }

//        ArrayList<Exercise> selectedWorkoutExercise = new ArrayList<Exercise>();
//
//        for (int i = 0; i < exercises.size(); ++i) {
//            if(exerciseIds.contains(exercises.get(i).getId())){
//                selectedWorkoutExercise.add(exercises.get(i));
//            }
//        }

            if (workoutExerciseListView.getChildCount() > 0) {
                workoutExerciseListView.removeAllViews();
            }

            LogService.Log("fillListAsyncTask", "listViewContainerLinearLayout h: " + listViewContainerLinearLayout.getHeight());
            String[] value = {listViewContainerLinearLayout.getHeight() + ""};
            imageViewPager.setOnTouchListener(disabledViewPagerOnTouchListener);
            new fillListAsyncTask().execute(value);

        }

//        listAdapter = new HomeWorkoutListAdapter(getActivity(),selectedWorkoutExercise);
//        for (int i = 0; i < listAdapter.getCount(); ++i) {
//
//            final int finalInt = i;
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    workoutExerciseListView
//                            .addView(listAdapter.getView(finalInt, null, workoutExerciseListView));
//                }
//
//            }, 1000);
//
//        }
//        workoutExerciseListView.setAdapter(listAdapter);


    }

    private class fillListAsyncTask extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {

            int height = Integer.valueOf(params[0]);

            int workoutId = workouts.get(selectedWorkout).getId();

            ArrayList<Integer> exerciseIds = new ArrayList<Integer>();

            for (int i = 0; i < exerciseForWorkouts.size(); ++i) {
                if (exerciseForWorkouts.get(i).getWorkoutid() == workoutId) {
                    exerciseIds.add(exerciseForWorkouts.get(i).getExerciseId());
                }
            }

            ArrayList<Exercise> selectedWorkoutExercise = new ArrayList<Exercise>();

            for (int i = 0; i < exercises.size(); ++i) {
                if (exerciseIds.contains(exercises.get(i).getId())) {
                    selectedWorkoutExercise.add(exercises.get(i));
                }
            }

            listAdapter = new HomeWorkoutListAdapter(getActivity(), selectedWorkoutExercise, listItemViewSize);
            for (int i = 0; i < listAdapter.getCount(); ++i) {

                if ((i * listItemViewSize) <= (height + listItemViewSize)) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                publishProgress(i);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            View view = listAdapter.getView(values[0], null, workoutExerciseListView);
            workoutExerciseListView
                    .addView(view);
            LogService.Log("fillListAsyncTask", "workoutExerciseListView view size: " + view.getHeight());
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            imageViewPager.setOnTouchListener(enableViewPagerOnTouchListener);
            showAnimatedBottomLayout();
        }
    }

    private void showAnimatedBottomLayout() {

        if (bottomRelativeLayout.getTag().toString().equals("0")) {

            if (getActivity() != null) {

                System.gc();

                Animation animation = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.translate_bottom_top_animation);
                animation.setFillAfter(true);

                final boolean finalCanShowSlider = checkIfCanShowStartWorkout();
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        if (finalCanShowSlider) {
                            startExerciseImageView.setVisibility(View.VISIBLE);
                            ((RelativeLayout.LayoutParams) startExerciseImageView.getLayoutParams()).setMargins(listItemViewSize - getResources().getDimensionPixelSize(R.dimen.home_screen_start_button_w) / 2, 0, 0, 0);
                            initPlayButtonAnimation();
                            unlockTextView.setVisibility(View.GONE);
                        } else {
                            startExerciseImageView.setVisibility(View.GONE);
                            unlockTextView.setVisibility(View.VISIBLE);
                        }

                        bottomRelativeLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        updateVisibilityRightThreeLayouts();

                        animation.reset();
                        animation = null;
                        System.gc();

//                    bottomRelativeLayout.clearAnimation();
                        startExerciseImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                startExerciseImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                float x = startExerciseImageView.getX();
                                int w = startExerciseImageView.getWidth();
                                int viewWidth = bottomTextRelativeLayout.getWidth();
                                LogService.Log("startExerciseImageView.getViewTreeObserver", "x: " + x + " width: " + w + " viewWidth: " + viewWidth);
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bottomTextRelativeLayout.getLayoutParams();
                                layoutParams.addRule(RelativeLayout.RIGHT_OF, 0);
//                            layoutParams.width = viewWidth;
                                layoutParams.setMargins((int) (x + w), 0, getResources().getDimensionPixelSize(R.dimen.home_screen_bottom_view_size), 0);
                                bottomTextRelativeLayout.setLayoutParams(layoutParams);
//                            bottomTextRelativeLayout.setX(x+w);
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                bottomRelativeLayout.startAnimation(animation);
                bottomRelativeLayout.setTag("1");


            }

        }

    }

    private View.OnClickListener startExerciseImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogService.Log("HomeFragment", "startExerciseImageViewOnClickListener click");
        }
    };

    private void initPlayButtonAnimation() {

        System.gc();

        if( ((BitmapDrawable)startExerciseImageView.getDrawable())!=null){
            ((BitmapDrawable)startExerciseImageView.getDrawable()).getBitmap().recycle();
        }


        startExerciseImageView.setImageResource(R.drawable.play_button_animation);
        AnimationDrawable loadingAnimation = (AnimationDrawable) startExerciseImageView.getDrawable();
        loadingAnimation.start();
        startBottomTextAnimation();

    }

    private View.OnTouchListener startExerciseImageViewOnTouchListener = new View.OnTouchListener() {

        private float delta = 0;
        private float screenWidth = 0;

        //        private float alphaValue = 1;
        private float alphaChangeValue = 0.1f;

        @Override
        public boolean onTouch(final View v, final MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

//                    LogService.Log(">>>>>", "ACTION_DOWN");
//                    LogService.Log(">>>>>", "post_start: "+startExerciseImageView.getX() + " half size: "+startExerciseImageView.getWidth());

                    startX = startExerciseImageView.getX();
                    delta = startExerciseImageView.getX() - (startExerciseImageView.getWidth() / 2);
                    screenWidth = UtilsMethods.getDeviceWidth(getActivity()) - startExerciseImageView.getWidth() - startX;
                    LogService.Log(">>>>>", "delta: " + delta + " screenWidth: " + screenWidth);

//                    alphaValue -= alphaChangeValue;
//                    sliderView.setAlpha(alphaValue);

                    return true;

                case MotionEvent.ACTION_MOVE:

                    LogService.Log(">>>>>", "ACTION_MOVE " + event.getRawX());
                    int margin = (int) event.getRawX() - (int) delta;
                    RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams) startExerciseImageView.getLayoutParams());

                    float alpha = (event.getRawX() * 100) / (screenWidth);
                    alpha = 1 - (alpha / 100);
//                    alpha = 1-alpha;
                    sliderView.setAlpha(alpha - alphaChangeValue);

                    if (margin >= screenWidth) {
                        //start
                        if (!isWorkOutStarted) {

                            isWorkOutStarted = true;

                            Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                            intent.putExtra(WorkoutActivity.EXTRA_DATE_WORKOUT_ID, workouts.get(selectedWorkout).getId());
                            intent.putExtra(WorkoutActivity.EXTRA_DATE_EXERCISE_LIST, new Gson().toJson(listAdapter.getItems()).toString());
                            LogService.Log(">>>>>", "ACTION_MOVE " + "START_WORKOUT");
                            startActivity(intent);
//
//                            Intent intent = new Intent(getActivity(), WorkoutCompletedActivity.class);
//                            intent.putExtra(WorkoutActivity.EXTRA_DATE_WORKOUT_ID, workouts.get(selectedWorkout).getId());
//                            LogService.Log(">>>>>", "ACTION_MOVE " + "START_WORKOUT");
//                            startActivity(intent);

                        }
//                        layoutParams.leftMargin = (int) startX;
//                        startExerciseImageView.setLayoutParams(layoutParams);
                    } else {
                        layoutParams = ((RelativeLayout.LayoutParams) startExerciseImageView.getLayoutParams());
                        layoutParams.leftMargin = margin;
                        LogService.Log(">>>>>", "ACTION_MOVE " + margin + "<---> screenWidth: " + screenWidth + "sum: " + (margin + delta) + " alpha: " + alpha);
                        startExerciseImageView.setLayoutParams(layoutParams);
                    }

                    return true;

                case MotionEvent.ACTION_CANCEL:
                    LogService.Log(">>>>>", "ACTION_CANCEL");
                    return false;
                case MotionEvent.ACTION_UP:

                    LogService.Log(">>>>>", "ACTION_UP");

                    if (!isWorkOutStarted) {
                        layoutParams = ((RelativeLayout.LayoutParams) startExerciseImageView.getLayoutParams());
                        layoutParams.leftMargin = (int) startX;
                        LogService.Log("startExerciseImageViewOnTouchListener"," startX: "+startX);
                        startExerciseImageView.setLayoutParams(layoutParams);
                        sliderView.setAlpha(1);
//                        isWorkOutStarted = false;
                    }

//                    alphaValue = 1;


                    return false;

                case MotionEvent.ACTION_OUTSIDE:
                    LogService.Log(">>>>>", "ACTION_OUTSIDE");
                    return false;
            }

            return false;
        }
    };

    private void startBottomTextAnimation() {

        System.gc();

        bottomTextBackgroundImageView.setImageResource(R.drawable.text_white_animation);
        AnimationDrawable loadingAnimation = (AnimationDrawable) bottomTextBackgroundImageView.getDrawable();
        loadingAnimation.start();

    }

    private View.OnTouchListener disabledViewPagerOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    private View.OnTouchListener enableViewPagerOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };

    private View.OnClickListener unlockTextViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goProResponseManagerString.responseArrived(workouts.get(selectedWorkout).getUnlockItem());
        }
    };

    private boolean checkIfCanShowStartWorkout() {

        boolean canShowSlider = false;

        if (UtilsMethods.getBooleanFromSharedPreferences(getActivity(), workouts.get(selectedWorkout).getUnlockItem(), false)) {
            canShowSlider = true;
        }

        if (UtilsMethods.getBooleanFromSharedPreferences(getActivity(), MainActivity.ALL_IN_ONE_IAP_ITEM, false)) {
            canShowSlider = true;
        }

        if (workouts.get(selectedWorkout).getUnlockItem().equals("")) {
            canShowSlider = true;
        }

        return canShowSlider;

    }

    public void updateBottomView() {
        if (checkIfCanShowStartWorkout()) {
            startExerciseImageView.setVisibility(View.VISIBLE);
            unlockTextView.setVisibility(View.GONE);
        } else {
            startExerciseImageView.setVisibility(View.GONE);
            unlockTextView.setVisibility(View.VISIBLE);
        }
    }

    public void updateVisibilityRightThreeLayouts() {

        if (UtilsMethods.getBooleanFromSharedPreferences(getActivity(), MainActivity.SETTINGS_IAP_ITEM, false) || UtilsMethods.getBooleanFromSharedPreferences(getActivity(), MainActivity.ALL_IN_ONE_IAP_ITEM, false) || AchievementsCheckBadgesUtils.showBadges(specificAchivementPosition, achievements, completedWorkouts)) {
            startRightButtonsTranslateRightToLeftAnimation(exerciseButtonsLinearLayout, exerciseContainerLinearLayout, exerciseButtonsLinearLayout.getWidth() / 2, false);
        }

    }



}
