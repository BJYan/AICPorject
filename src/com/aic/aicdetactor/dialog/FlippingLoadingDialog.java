package com.aic.aicdetactor.dialog;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.view.FlippingImageView;
import com.aic.aicdetactor.view.HandyTextView;

import android.app.Dialog;
import android.content.Context;

public class FlippingLoadingDialog extends Dialog {

	private FlippingImageView mFivIcon;
	private HandyTextView mHtvText;
	private String mText;

	public FlippingLoadingDialog(Context context, String text) {
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		mText = text;
		init();
	}

	private void init() {
		setContentView(R.layout.common_flipping_loading_diloag);
		mFivIcon = (FlippingImageView) findViewById(R.id.loadingdialog_fiv_icon);
		mHtvText = (HandyTextView) findViewById(R.id.loadingdialog_htv_text);
		mFivIcon.startAnimation();
		mHtvText.setText(mText);
	}

	public void setText(String text) {
		mText = text;
		mHtvText.setText(mText);
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}
}
