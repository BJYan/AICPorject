package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonDialog.ChartDialogBtnListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MessageListViewAdapter extends BaseAdapter implements ChartDialogBtnListener{
	Context context;
	private LayoutInflater mInflater;

	public MessageListViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View msgItem = mInflater.inflate(R.layout.message_listview_item, null);
		msgItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				View msgView = mInflater.inflate(R.layout.dialog_message_layout, null);
				CommonDialog msgDialog = new CommonDialog(context);
				msgDialog.setCloseBtnVisibility(View.VISIBLE);
				msgDialog.setTitle("任务");
				msgDialog.setChartView(msgView);
				msgDialog.setButtomBtn(MessageListViewAdapter.this, "", "确定");
				msgDialog.show();
			}
		});
		return msgItem;
	}

	@Override
	public void onClickBtn1Listener(CommonDialog dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickBtn2Listener(CommonDialog dialog) {
		// TODO Auto-generated method stub
		dialog.dismiss();
	}

}
