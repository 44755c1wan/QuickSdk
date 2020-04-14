package com.ledi.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
	List<View> list;

	public MyPagerAdapter(ArrayList<View> list) {
		this.list = list;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(list.get(position));

	}

	/**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(list.get(position), 0);
		return list.get(position);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}
}