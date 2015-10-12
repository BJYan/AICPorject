package com.aic.aicdetactor.activity;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;

import android.os.Bundle;

public class BlueToothRenameActivity extends CommonActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_sensor_rename);
		
		setActionBar("传感器设置",true); 
	}

}
