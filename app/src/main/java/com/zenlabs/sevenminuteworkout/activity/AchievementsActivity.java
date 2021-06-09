package com.zenlabs.sevenminuteworkout.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.adapter.AchievementsListAdapter;
import com.zenlabs.sevenminuteworkout.database.Achievement;
import com.zenlabs.sevenminuteworkout.database.CompletedWorkout;
import com.zenlabs.sevenminuteworkout.database.DatabaseHelper;
import com.zenlabs.sevenminuteworkout.utils.AchievementsCheckBadgesUtils;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.PercentageCircle;
import com.zenlabs.sevenminuteworkout.utils.PercentageCircleAnimation;

import java.util.ArrayList;
import java.util.Collections;

public class AchievementsActivity extends Activity {

    private ImageView closeImageView;
    private PercentageCircle percentageFirstInnerCircle;
    private PercentageCircle percentageFirstBorderCircle;
    private PercentageCircle percentageSecondInnerCircle;
    private PercentageCircle percentageSecondBorderCircle;
    private TextView precentTextView;
    private AchievementsListAdapter listAdapter;
    private ListView listView;

    private DatabaseHelper databaseHelper;

    private int paddingPrecent = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        initialiseViews();

        initListView();

    }

    @Override
    protected void onPause() {

        super.onPause();

        if (databaseHelper != null) {
            databaseHelper.close();
        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            databaseHelper.openDataBase();
        }

    }

    private void initialiseViews() {

        closeImageView = (ImageView) findViewById(R.id.achievementsActivityActionBarBackImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        percentageFirstInnerCircle = (PercentageCircle) findViewById(R.id.achievementsActivityFirstInnerPercentageCircle);
        percentageFirstBorderCircle = (PercentageCircle) findViewById(R.id.achievementsActivityFirstBorderPercentageCircle);

        percentageSecondInnerCircle = (PercentageCircle) findViewById(R.id.achievementsActivitySecondInnerPercentageCircle);
        percentageSecondBorderCircle = (PercentageCircle) findViewById(R.id.achievementsActivitySecondtBorderPercentageCircle);

        precentTextView = (TextView) findViewById(R.id.achievementsActivityPercentTextView);

        listView = (ListView) findViewById(R.id.achievementsActivityListView);

    }

    private void initListView() {

        ArrayList<Achievement> achievements = new ArrayList<Achievement>();
        ArrayList<CompletedWorkout> completedWorkouts = new ArrayList<CompletedWorkout>();

        try {
            databaseHelper = new DatabaseHelper(AchievementsActivity.this);

            achievements = databaseHelper.getAllAchivements();
            LogService.Log("AchievementsActivity", "achievements: " + achievements.toString());
            completedWorkouts = databaseHelper.getAllCompletedWorkouts();
            LogService.Log("AchievementsActivity", "completedWorkouts: " + completedWorkouts.toString());

        } catch (Exception ex) {
            LogService.Log("AchievementsActivity", "db ex: " + ex.toString());
        }

        Collections.sort(completedWorkouts);

        LogService.Log("AchievementsActivity", "sorted completedWorkouts: " + completedWorkouts.toString());

        listAdapter = new AchievementsListAdapter(AchievementsActivity.this, achievements, completedWorkouts);
        listView.setAdapter(listAdapter);

        int nrAchieved = 0;

        for(int i=0;i<achievements.size();++i){
            if(AchievementsCheckBadgesUtils.showBadges(i,achievements,completedWorkouts)){
                ++nrAchieved;
            }
        }

        showFirstPrecentCircle(100*nrAchieved/achievements.size());

    }

    private void showFirstPrecentCircle(int precent) {

        final int timePrecent = precent;

        if (precent == 0 || precent == 100) {
            paddingPrecent = 0;
        } else {
            precent = ((360 * precent) / 100);
        }

        percentageFirstBorderCircle.setStroke(PercentageCircle.STROKE_DARK_WIDTH);
        percentageFirstBorderCircle.setBorderColor(R.color.green_dark_achievement);
        percentageFirstBorderCircle.setTheSize(R.dimen.achievements_border_circle_size);

        PercentageCircleAnimation innerFirstAnimation = new PercentageCircleAnimation(percentageFirstInnerCircle, 0, -90, precent, false);
        innerFirstAnimation.setFillEnabled(true);
        innerFirstAnimation.setFillAfter(true);
        innerFirstAnimation.setDuration(getPercentAnimationDuration(timePrecent));

        PercentageCircleAnimation borderFirstAnimation = new PercentageCircleAnimation(percentageFirstBorderCircle, 0, -90, precent, false);
        borderFirstAnimation.setFillEnabled(true);
        borderFirstAnimation.setFillAfter(true);
        borderFirstAnimation.setDuration(getPercentAnimationDuration(timePrecent));

        final int finalPrecent = precent;
        borderFirstAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showLastPrecentCircle(timePrecent, finalPrecent, (-90 + finalPrecent + paddingPrecent));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        percentageFirstInnerCircle.startAnimation(innerFirstAnimation);
        percentageFirstBorderCircle.startAnimation(borderFirstAnimation);


    }

    private void showLastPrecentCircle(final int timePrecent, int precent, int precentStart) {

        percentageSecondInnerCircle.setBorderColor(R.color.green_light_not_achievement);
        percentageSecondBorderCircle.setBorderColor(R.color.green_dark_not_achievement);

        percentageSecondBorderCircle.setStroke(PercentageCircle.STROKE_DARK_WIDTH);
        percentageSecondBorderCircle.setTheSize(R.dimen.achievements_border_circle_size);

        PercentageCircleAnimation innerLastAnimation = new PercentageCircleAnimation(percentageSecondInnerCircle, 0, precentStart, 360 - precent - 2 * paddingPrecent, false);
        innerLastAnimation.setFillEnabled(true);
        innerLastAnimation.setFillAfter(true);
        innerLastAnimation.setDuration(getPercentAnimationDuration(100 - timePrecent));

        PercentageCircleAnimation borderLastAnimation = new PercentageCircleAnimation(percentageSecondBorderCircle, 0, precentStart, 360 - precent - 2 * paddingPrecent, false);
        borderLastAnimation.setFillEnabled(true);
        borderLastAnimation.setFillAfter(true);
        borderLastAnimation.setDuration(getPercentAnimationDuration(100 - timePrecent));
        borderLastAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                precentTextView.setText(timePrecent + " " + getResources().getString(R.string.precent_text_at_achievements));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        percentageSecondInnerCircle.startAnimation(innerLastAnimation);
        percentageSecondBorderCircle.startAnimation(borderLastAnimation);

    }

    private long getPercentAnimationDuration(int precent) {

        long defaultDuration = 2000;

        return precent * defaultDuration / 100;

    }

}
