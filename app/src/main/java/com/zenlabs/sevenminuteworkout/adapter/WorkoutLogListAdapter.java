package com.zenlabs.sevenminuteworkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.database.CompletedWorkout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by madarashunor on 09/11/15.
 */
public class WorkoutLogListAdapter extends BaseAdapter {

    private  ArrayList<CompletedWorkout> items;
    private LayoutInflater inflater;

    public WorkoutLogListAdapter() {
        super();
    }

    public WorkoutLogListAdapter(Context context,
                           ArrayList<CompletedWorkout> items) {
        super();
        this.items = items;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(
                R.layout.workout_log_list_item_layout, parent,
                false);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.workoutLogDateTextView);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(items.get(position).getDate()));

        String dateString = String.format("%1$tb %1$td, %1$tY", calendar);
        dateString = dateString.substring(0,1).toUpperCase()+dateString.substring(1).toLowerCase();
        String workoutDateString = dateString  + " @ " + String.format("%1$tI:%1$tM %1$Tp", calendar).toLowerCase();

        dateTextView.setText(workoutDateString);

        TextView workoutOptionTextView = (TextView) convertView.findViewById(R.id.workoutLogWorkoutOptionTextView);
        workoutOptionTextView.setText(items.get(position).getWorkoutId()+"");

        TextView workoutCyclesTextView = (TextView) convertView.findViewById(R.id.workoutLogCyclesCompTextView);
        workoutCyclesTextView.setText(items.get(position).getCycles()+"");

        return convertView;

    }

}
