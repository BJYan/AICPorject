package com.aic.aicdetactor.activity;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;

import android.os.Bundle;

public class TempRouteActivity extends CommonActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_temp_layout);
		
		setActionBar("临时测量",true);
	}
}
