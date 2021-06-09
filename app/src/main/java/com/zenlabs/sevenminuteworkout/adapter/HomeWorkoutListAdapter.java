package com.zenlabs.sevenminuteworkout.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.activity.ExerciseInfoActivity;
import com.zenlabs.sevenminuteworkout.database.Exercise;
import com.zenlabs.sevenminuteworkout.utils.LogService;

import java.util.ArrayList;

/**
 * Created by madarashunor on 13/10/15.
 */
public class HomeWorkoutListAdapter extends BaseAdapter {

    private ArrayList<Exercise> items;
    private LayoutInflater inflater;
    private int itemSize;

    public HomeWorkoutListAdapter() {
        super();
    }

    public HomeWorkoutListAdapter(Context context, ArrayList<Exercise> items, int itemSize) {
        super();
        this.items = items;
        this.itemSize = itemSize;
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
        return items.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final View convertView2 = inflater.inflate(R.layout.workout_exercise_list_item_layout,
                parent, false);

        TextView nrTextView = (TextView) convertView2.findViewById(R.id.workoutExerciseListItemIndexTextView);
        nrTextView.setText((position + 1) + "");

        TextView titleTextView = (TextView) convertView2.findViewById(R.id.workoutExerciseListItemTitleTextView);
        titleTextView.setText(items.get(position).getName());

        LogService.Log("HomeWorkoutListAdapter","convertView.getHeight(): "+itemSize);

        Animation animation = new TranslateAnimation(0, 0, -itemSize, 0);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                convertView2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                convertView2.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        convertView2.startAnimation(animation);

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogService.Log("HomeWorkoutListAdapter", "< on item click >");
                Intent intent = new Intent(parent.getContext(), ExerciseInfoActivity.class);
                intent.putExtra(ExerciseInfoActivity.EXERCISE_ACTIVITY_EXTRA_INFO,new Gson().toJson(items.get(position)));
                parent.getContext().startActivity(intent);
            }
        });

//        AnimatorSet flipAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(parent.getContext(), R.animator.flip_animation);
//        flipAnimatorSet.setTarget(convertView);
//        flipAnimatorSet.start();


        return convertView2;

    }

    public ArrayList<Exercise> getItems() {
        return items;
    }
}
