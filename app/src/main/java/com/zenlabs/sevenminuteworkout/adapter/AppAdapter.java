package com.zenlabs.sevenminuteworkout.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.utils.App;
import com.zenlabs.sevenminuteworkout.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by tunde on 29.09.2015.
 */
public class AppAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<App> items = null;
    private ImageLoader imageLoader;
    private boolean isMoreApps;

    public AppAdapter(Context c, ArrayList<App> items, boolean isMoreApps) {
        context = c;
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        imageLoader = new ImageLoader(c);
        this.isMoreApps = isMoreApps;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid = null;

        grid = layoutInflater.inflate(R.layout.layout_more_apps_item, parent,
                false);
        TextView text = (TextView) grid.findViewById(R.id.app_name);
        if (isMoreApps) {
            text.setBackgroundResource(R.drawable.shadow_icons_more_apps);
        } else {
            text.setText(items.get(position).getName());
        }
        ImageView img = (ImageView) grid.findViewById(R.id.app_image);
        imageLoader.DisplayImage(items.get(position).getImage(), img, true);

        return grid;
    }
}
