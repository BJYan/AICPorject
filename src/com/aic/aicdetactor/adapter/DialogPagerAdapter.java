package com.aic.aicdetactor.adapter;

import java.util.List;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
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
		return 3;
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		View v = null;
		switch (position) {
		case 0:
			v = context.getLayoutInflater().inflate(R.layout.dialog_content_thr_charts1_layout, null);
			break;
		case 1:
			v = context.getLayoutInflater().inflate(R.layout.dialog_content_thr_charts1_layout, null);
			break;
		case 2:
			v = context.getLayoutInflater().inflate(R.layout.dialog_content_thr_charts1_layout, null);
			break;
		default:
			break;
		}
		return v;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		//return arg0 == arg1;
		return false;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(views.get(position));
	}
}
