package com.zenlabs.sevenminuteworkout.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.activity.AchievementsActivity;
import com.zenlabs.sevenminuteworkout.adapter.WorkoutLogListAdapter;
import com.zenlabs.sevenminuteworkout.database.CompletedWorkout;
import com.zenlabs.sevenminuteworkout.utils.LogService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by madarashunor on 09/11/15.
 */
public class WorkoutLogFragment extends Fragment {

    private Calendar calendar;
    private CaldroidFragment caldroidFragment;

    private ArrayList<CompletedWorkout> completedWorkouts;
    private WorkoutLogListAdapter listAdapter;
    private ListView listView;

    private Button myAchievementsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workout_log,
                null);

        listView = (ListView) view.findViewById(R.id.workoutLogListView);

        myAchievementsButton = (Button) view.findViewById(R.id.workoutLogMyAchievementsButton);
        myAchievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AchievementsActivity.class));
            }
        });

        myAchievementsButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                myAchievementsButton.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                setupCaldroidFragment();
            }
        });

        LogService.Log("WorkoutLogFragment", " listView size: " + listView.getCount());

        return view;

    }

    public ArrayList<CompletedWorkout> getCompletedWorkouts() {
        return completedWorkouts;
    }

    public void setCompletedWorkouts(ArrayList<CompletedWorkout> completedWorkouts) {
        this.completedWorkouts = completedWorkouts;
    }

    private void setupCaldroidFragment() {

        if (caldroidFragment == null) {

            caldroidFragment = new CaldroidFragment();
            Bundle args = new Bundle();
            calendar = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            caldroidFragment.setMySelectedDate(new Date());

            setBackgroundsForExisitngDates();

            caldroidFragment.setCaldroidListener(caldroidListener);

            getFragmentManager().beginTransaction().replace(R.id.workoutLogFrameLayout, caldroidFragment).commit();

            initListView();

        }

    }

    private final CaldroidListener caldroidListener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            // Do something
            LogService.Log("WorkoutLogFragment", "onSelectDate date: " + date.toString() + " view: " + view.toString());
//            calendar.setTimeInMillis(date.getTime());
            caldroidFragment.setNewTodayDate(date);
            caldroidFragment.setMySelectedDate(date);
            setBackgroundsForExisitngDates();
            initListView();
        }

        @Override
        public void onCaldroidViewCreated() {

        }

        @Override
        public void onChangeMonth(int month, int year) {
            super.onChangeMonth(month, year);
            LogService.Log("WorkoutLogFragment", "onChangeMonth month: " + month + " year: " + year);
        }
    };

    private void initListView() {

        ArrayList<CompletedWorkout> selectedDates = new ArrayList<CompletedWorkout>();

        if (caldroidFragment != null && caldroidFragment.getMySelectedDate() != null) {
            Calendar theCalendar = Calendar.getInstance();
            theCalendar.setTimeInMillis(caldroidFragment.getMySelectedDate().getTime());


            for (int i = 0; i < completedWorkouts.size(); ++i) {
                LogService.Log("initListView", "i: " + completedWorkouts.get(i).getDate());

                if (completedWorkouts.get(i).getDate() != null && caldroidFragment.getMySelectedDate() != null) {

                    Calendar tmpCalendar = Calendar.getInstance();
                    tmpCalendar.setTimeInMillis(Long.valueOf(completedWorkouts.get(i).getDate()));

                    if (tmpCalendar.get(Calendar.YEAR) == theCalendar.get(Calendar.YEAR) && tmpCalendar.get(Calendar.DAY_OF_YEAR) == theCalendar.get(Calendar.DAY_OF_YEAR)) {
                        selectedDates.add(completedWorkouts.get(i));
                    }

                }

            }

        }

        listAdapter = new WorkoutLogListAdapter(getActivity(), selectedDates);
        listView.setAdapter(listAdapter);

    }

    private void setBackgroundsForExisitngDates() {

        if (caldroidFragment != null && caldroidFragment.getMySelectedDate() != null) {

            Calendar theCalendar = Calendar.getInstance();
            theCalendar.setTimeInMillis(caldroidFragment.getMySelectedDate().getTime());

            for (int i = 0; i < completedWorkouts.size(); ++i) {

                Calendar tmpCalendar = Calendar.getInstance();
                tmpCalendar.setTimeInMillis(Long.valueOf(completedWorkouts.get(i).getDate()));

                if (tmpCalendar.get(Calendar.YEAR) == theCalendar.get(Calendar.YEAR) && tmpCalendar.get(Calendar.DAY_OF_YEAR) == theCalendar.get(Calendar.DAY_OF_YEAR)) {
                    caldroidFragment.clearBackgroundResourceForDate(tmpCalendar.getTime());
                } else {
                    caldroidFragment.setTextColorForDate(R.color.caldroid_white, tmpCalendar.getTime());
                    caldroidFragment.setBackgroundResourceForDate(R.drawable.calendar_hexa_gray, tmpCalendar.getTime());
                }

            }
        }


    }

}
