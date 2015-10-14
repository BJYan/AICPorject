package com.aic.aicdetactor.activity;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogCancelListener;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogOKListener;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BlueToothRenameActivity extends CommonActivity implements OnClickListener,
		AltDialogOKListener,AltDialogCancelListener{
	Button bindBtn;
	boolean binded = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_sensor_rename);
		
		setActionBar("传感器设置",true); 
		
		bindBtn = (Button) findViewById(R.id.bluetooth_bind_btn);
		bindBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bluetooth_bind_btn:
			if(!binded) showAlterDialog(this,"是否绑定？","原传感器将解除绑定",this,this);
			else showAlterDialog(this,"是否取消绑定？",null,this,this);
			break;

		default:
			break;
		}
	}

	@Override
	public void onComDialogCancelListener(CommonAlterDialog dialog) {
		// TODO Auto-generated method stub
		dialog.dismiss();
	}

	@Override
	public void onComDialogOKListener(CommonAlterDialog dialog) {
		// TODO Auto-generated method stub
		if(binded) {
			binded = false;
			bindBtn.setText("绑定");
		} else {
			binded = true;
			bindBtn.setText("取消绑定");
		}
		dialog.dismiss();
	}

}
