package com.aic.aicdetactor.adapter;

import java.util.List;

import com.aic.aicdetactor.CommonActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class DialogPagerAdapter extends PagerAdapter{
	CommonActivity context;
	List<View> views;
	
	public DialogPagerAdapter(CommonActivity context, List<View> views) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.views = views;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(views.get(position), 0);
		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(views.get(position));
	}
}
