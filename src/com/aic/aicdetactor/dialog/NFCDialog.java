package com.aic.aicdetactor.dialog;

import com.aic.aicdetactor.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class NFCDialog extends Dialog{
	android.view.View.OnClickListener listener;

	public NFCDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	
	public NFCDialog(Context context, android.view.View.OnClickListener listener) {
		super(context,R.style.Theme_Light_FullScreenDialogAct);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_nfc_layout);
		Button cancel = (Button) findViewById(R.id.dialog_nfc_cancel_btn);
	//	cancel.setOnClickListener(listener);
		cancel.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}});
	}

}
