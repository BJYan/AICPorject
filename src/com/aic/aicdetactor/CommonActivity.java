package com.aic.aicdetactor;

import com.aic.aicdetactor.dialog.FlippingLoadingDialog;

import android.app.Activity;
import android.os.Bundle;

public class CommonActivity extends Activity{
	protected FlippingLoadingDialog mLoadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLoadingDialog = new FlippingLoadingDialog(this, "请求提交中");
	}
	
	protected void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
		}
		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

}
