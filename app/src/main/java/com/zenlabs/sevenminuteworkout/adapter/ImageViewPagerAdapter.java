package com.zenlabs.sevenminuteworkout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zenlabs.sevenminuteworkout.utils.ImageViewPageIndicatorManager;

import java.util.ArrayList;

public class ImageViewPagerAdapter extends PagerAdapter {

	private Context context;
	private ArrayList<Bitmap> images;
	
	public ImageViewPagerAdapter(Context context,
			ArrayList<Bitmap> images,
			ImageViewPageIndicatorManager imageViewPageIndicatorManager) {
		super();
		this.context = context;
		this.images = images;
	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setImageBitmap(images.get(position));
		((ViewPager) container).addView(imageView, 0);	

		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

}
