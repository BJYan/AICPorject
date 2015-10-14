package com.aic.aicdetactor.dialog;

import com.aic.aicdetactor.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CommonAlterDialog extends Dialog implements android.view.View.OnClickListener{
	Context context;
	TextView dialogTitle,dialogContent;
	Button OkBtn,CancelBtn;
	AltDialogOKListener okListener;
	AltDialogCancelListener cancelListener;
	String title,content;
	View line;
	
	public interface AltDialogOKListener{
		void onComDialogOKListener(CommonAlterDialog dialog);
	}

	public interface AltDialogCancelListener{
		void onComDialogCancelListener(CommonAlterDialog dialog);
	}
	
	public CommonAlterDialog(Context context, String title, String content,
			AltDialogOKListener okListener,AltDialogCancelListener cancelListener) {
		// TODO Auto-generated constructor stub
		super(context,R.style.Common_Alter_Dialog);
		this.context = context;
		this.okListener = okListener;
		this.cancelListener = cancelListener;
		this.title = title;
		this.content = content;
	}
	
	public CommonAlterDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public CommonAlterDialog(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_common_layout);
		CommonAlterDialogInit();
	}
	
	private void CommonAlterDialogInit(){
		dialogTitle = (TextView) findViewById(R.id.common_dia_title);
		dialogContent = (TextView) findViewById(R.id.common_dia_content);
		CancelBtn = (Button) findViewById(R.id.common_dia_cancel);
		OkBtn = (Button) findViewById(R.id.common_dia_ok);
		line = findViewById(R.id.common_dia_line);
		
		if(title!=null) dialogTitle.setText(title);
		else {
			line.setVisibility(View.GONE);
			dialogTitle.setVisibility(View.GONE);
		}
		if(content!=null) dialogContent.setText(content);
		else {
			line.setVisibility(View.GONE);
			dialogContent.setVisibility(View.GONE);
		}
		if(okListener==null) OkBtn.setVisibility(View.GONE);
		else OkBtn.setOnClickListener(this);
		if(cancelListener==null) CancelBtn.setVisibility(View.GONE);
		else CancelBtn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.common_dia_ok:
			if(okListener!=null) okListener.onComDialogOKListener(this);
			break;
		case R.id.common_dia_cancel:
			if(cancelListener!=null) cancelListener.onComDialogCancelListener(this);
			break;
		default:
			break;
		}
	}

}
