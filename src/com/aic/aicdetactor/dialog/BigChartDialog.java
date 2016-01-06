package com.aic.aicdetactor.dialog;

import com.aic.aicdetactor.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BigChartDialog extends Dialog{
	Context context;
	LayoutInflater mInflater;
	View contentView;

	public BigChartDialog(Context context) {
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		contentView = mInflater.inflate(R.layout.dialog_big_chart, null);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(contentView);
		
	}
	
	public void setChartView(View chartView){
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.MATCH_PARENT);
		((LinearLayout) contentView).addView(chartView,lp);
	}

}
