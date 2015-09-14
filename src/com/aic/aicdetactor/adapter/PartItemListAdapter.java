package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.DeviceItemActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.PartItemItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PartItemListAdapter extends BaseExpandableListAdapter {
	Context context;
	private LayoutInflater mInflater;
	//ArrayList<ArrayList<Map<String, String>>> mChildrenList;
	private Activity mActivity = null;
	private myApplication app = null;
	List<PartItemItem> itemList=null;;
	private final String TAG="luotest";
	public PartItemListAdapter(Context context,
			Activity av,List<PartItemItem> itemList){
			//ArrayList<ArrayList<Map<String, String>>> mChildrenList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		//this.mChildrenList = mChildrenList;
		mActivity = av;
		app = ((myApplication) av.getApplication());
		this.itemList =itemList;
		//Log.d(TAG, "PartItemListAdapter:"+item.toString());
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		View argtest = mInflater.inflate(R.layout.motor_load_vibration_layout, null);
		//TextView NameText = (TextView) argtest.findViewById(R.id.pathname);
		//NameText.setText("我是4级目录");
		return argtest;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return itemList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		if(arg2==null){
		arg2 = mInflater.inflate(R.layout.checkitem_thr_item, null);
		}
		final TextView NameText;
		NameText = (TextView) arg2.findViewById(R.id.pathname);
		NameText.setText(itemList.get(arg0).getCheckContent());
		ImageView image = (ImageView) arg2.findViewById(R.id.history);
		image.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//添加拉起趋势图的
				TextView NameText;
				View p =(View) arg0.getParent();
				NameText = (TextView) p.findViewById(R.id.pathname);
				// TODO Auto-generated method stub
				Toast.makeText(context, NameText.getText(), Toast.LENGTH_SHORT).show();
			}
			
		});
		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
