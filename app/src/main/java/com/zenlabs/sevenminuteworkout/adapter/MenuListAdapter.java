package com.zenlabs.sevenminuteworkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.MenuItemEnum;
import com.zenlabs.sevenminuteworkout.utils.MenuListItem;
import com.zenlabs.sevenminuteworkout.utils.MenuResponseManager;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by madarashunor on 06/10/15.
 */
public class MenuListAdapter extends BaseAdapter {

    private ArrayList<MenuListItem> items;
    private LayoutInflater inflater;
    private MenuResponseManager menuResponseManager;

    public MenuListAdapter() {
        super();
    }

    public MenuListAdapter(Context context,
                           ArrayList<MenuListItem> items, MenuResponseManager menuResponseManager) {
        super();
        this.items = items;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.menuResponseManager = menuResponseManager;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        convertView = inflater.inflate(
                R.layout.menu_list_item_layout, parent,
                false);

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.menuListItemIconImageView);
        iconImageView.setImageResource(items.get(position).getIcon());

        if (items.get(position).getId() == MenuItemEnum.TICK_SOUNDS_EVERY_SECOND || items.get(position).getId() == MenuItemEnum.TIPS_SCREEN || items.get(position).getId() == MenuItemEnum.KIIP) {

            final ImageView switchImageView = (ImageView) convertView.findViewById(R.id.menuListItemSwitchImageView);
            switchImageView.setVisibility(View.VISIBLE);

            String prefKey = "";

            if (items.get(position).getId() == MenuItemEnum.TICK_SOUNDS_EVERY_SECOND) {
                prefKey = UtilsValues.SHARED_PREFERENCES_TICK_SOUNDS;
            } else if (items.get(position).getId() == MenuItemEnum.TIPS_SCREEN) {
                prefKey = UtilsValues.SHARED_PREFERENCES_TIPS_SCREEN;
            }
            else if (items.get(position).getId() == MenuItemEnum.KIIP) {
                prefKey = UtilsValues.SHARED_PREFERENCES_KIIP_REWARDS;
            }

            boolean isSet = UtilsMethods.getBooleanFromSharedPreferences(parent.getContext(), prefKey);

            setSwitchOnOffInit(switchImageView, isSet);

            final String finalPrefKey = prefKey;
            switchImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isSet = Boolean.valueOf(switchImageView.getTag().toString());
                    if (isSet) {
                        switchImageView.setImageResource(R.drawable.sw_off);
                    } else {
                        switchImageView.setImageResource(R.drawable.sw_on);
                    }
                    isSet = !isSet;
                    switchImageView.setTag(isSet);
                    UtilsMethods.saveBooleanInSharedPreferences(parent.getContext(), finalPrefKey, isSet);
                }
            });

        }

        if(items.get(position).getId() == MenuItemEnum.REMINDER){

            TextView reminderInfoTextView = (TextView) convertView.findViewById(R.id.menuListItemReminderTimeTextView);
            String time = UtilsMethods.getStringFromSharedPreferences(parent.getContext(), UtilsValues.SHARED_PREFERENCES_REMINDER_TIME);
            Calendar calendar = Calendar.getInstance();
            if(!time.equals("")){
                calendar.setTimeInMillis(Long.valueOf(time));
                reminderInfoTextView.setVisibility(View.VISIBLE);

                String hour = "";
                String minute = "";
                String amPm = "";

                hour = calendar.get(Calendar.HOUR)+"";
                minute = calendar.get(Calendar.MINUTE)+"";

                if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                    amPm = parent.getContext().getResources().getString(R.string.am);
                }
                else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {

                    amPm = parent.getContext().getResources().getString(R.string.pm);
                }

                amPm = amPm.toLowerCase();

                time = hour + ":"+minute+" "+amPm;

                reminderInfoTextView.setText(time);


            }
            LogService.Log("MenuListadapter", "time: " + time);

        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.menuListItemTitleTextView);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuResponseManager.response(items.get(position).getId());
            }
        });
        titleTextView.setText(items.get(position).getTitle());

        return convertView;
    }

    private void setSwitchOnOffInit(ImageView switchImageView, boolean isSet) {
        if (isSet) {
            switchImageView.setImageResource(R.drawable.sw_on);
        } else {
            switchImageView.setImageResource(R.drawable.sw_off);
        }
        switchImageView.setTag(isSet);
    }

}
