package com.zenlabs.sevenminuteworkout.adapter;

/**
 * Created by tunde on 21.08.2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zenlabs.sevenminuteworkout.fragment.GridFragment;
import com.zenlabs.sevenminuteworkout.utils.App;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int pageCount;
    private int imagesPerPage;
    private int framesCount;
    private ArrayList<App> items = new ArrayList<App>();

    public ViewPagerAdapter(FragmentManager fm, int pageCount,
                            int itemsPerPage, int framesCount,
                            ArrayList<App> items) {
        super(fm);
        this.pageCount = pageCount;
        this.imagesPerPage = itemsPerPage;
        this.framesCount = framesCount;
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("number", position);
        args.putInt("firstImage", position * imagesPerPage);

        int imageCount = imagesPerPage;
        if (position == (pageCount - 1)) {
            int numTopics = framesCount;
            int rem = numTopics % imagesPerPage;
            if (rem > 0)
                imageCount = rem;
        }
        args.putInt("imageCount", imageCount);

        GridFragment fragment = new GridFragment();
        fragment.setArguments(args);
        fragment.setItems(items);
        return fragment;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

}
