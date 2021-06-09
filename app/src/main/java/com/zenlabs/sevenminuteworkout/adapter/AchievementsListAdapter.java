package com.zenlabs.sevenminuteworkout.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.database.Achievement;
import com.zenlabs.sevenminuteworkout.database.CompletedWorkout;
import com.zenlabs.sevenminuteworkout.utils.AchievementsCheckBadgesUtils;
import com.zenlabs.sevenminuteworkout.utils.LogService;

import java.util.ArrayList;

/**
 * Created by madarashunor on 17/11/15.
 */
public class AchievementsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Achievement> items;
    private ArrayList<CompletedWorkout> completedWorkouts;
    private LayoutInflater inflater;

    public AchievementsListAdapter() {
        super();
    }

    public AchievementsListAdapter(Context context, ArrayList<Achievement> items, ArrayList<CompletedWorkout> completedWorkouts) {
        super();
        this.context = context;
        this.items = items;
        this.completedWorkouts = completedWorkouts;
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

        convertView = inflater.inflate(R.layout.achievements_list_item_layout,
                parent, false);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.achievementListItemTitileTextView);
        titleTextView.setText(items.get(position).getName());

        TextView subTitleTextView = (TextView) convertView.findViewById(R.id.achievementListItemSubTitileTextView);

        if (items.get(position).getSubText().contains("\n")) {
            LogService.Log("AcievementsListAdapter getView", " contains \n " + items.get(position).getSubText());
        }

        subTitleTextView.setText(Html.fromHtml((items.get(position).getSubText())));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.achievementsListItemImageView);

        if (AchievementsCheckBadgesUtils.showBadges(position, items, completedWorkouts)) {

            imageView.setImageResource(context.getResources()
                    .getIdentifier(items.get(position).getImageId(), "drawable", context.getPackageName()));

        }

        return convertView;

    }


}
