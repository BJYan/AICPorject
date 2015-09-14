package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.acharEngine.AverageTemperatureChart;
import com.aic.aicdetactor.acharEngine.IDemoChart;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.DeviceItemActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.data.PartItemItem;
import com.aic.aicdetactor.fragment.Vibrate_fragment;
import com.aic.aicdetactor.view.GroupViewHolder;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class DeviceListAdapter  extends BaseExpandableListAdapter {
	private Context context;
	private LayoutInflater mInflater;
	//PartItemNameList
	ArrayList<ArrayList<PartItemItem>> mChildrenList;
	//List<PartItemItem> mChildrenList;
	//deviceNameList
	private List<Map<String, String>> mDataList = null;
	//deviceObjectList
	private List<Object> mDeviceItemList = null;
	//partItemObjectList
	private List<Object> mPartItemList = null;
	private int mStationIndex=0;
	private int mDeviceIndex=0;
	private myApplication app = null;
	private Activity mActivity = null;
	public DeviceListAdapter(Context context, Activity av,
			int stationIndex,int deviceIndex){
			//ArrayList<ArrayList<Map<String, String>>> mChildrenList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		//this.mChildrenList = mChildrenList;
		mStationIndex = stationIndex;
		mDeviceIndex = deviceIndex;
		mActivity = av;
		mDataList = new ArrayList<Map<String, String>>();
		app = ((myApplication) av.getApplication());
		InitData();
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mChildrenList.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	public ExpandableListView getExpandableListView(){
		ExpandableListView ExListView = new ExpandableListView(context);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ExListView.setPadding(40, 5, 10, 0);
		ExListView.setLayoutParams(lp);
		LayoutParams params = new LayoutParams();
		params.height=LayoutParams.MATCH_PARENT;
		params.width=LayoutParams.MATCH_PARENT;
		ExListView.setLayoutParams(params);
		ExListView.setGroupIndicator(null);
		return ExListView;
	}
	//是否三级list
	final boolean LEVEL3=true;
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		if(!LEVEL3){
		final ExpandableListView thrGroupView = getExpandableListView();
		final PartItemListAdapter thrExListAdapter = new PartItemListAdapter(context, mActivity,mChildrenList.get(arg0));
		
		thrGroupView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				thrGroupView.removeAllViewsInLayout();
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						CommonDef.LISTITEM_HEIGHT*mChildrenList.size());
				thrGroupView.setLayoutParams(lp);
			}
		});
		
		thrGroupView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT);
				thrGroupView.setLayoutParams(lp);
			}
			
		});
		thrGroupView.setAdapter(thrExListAdapter);
		thrGroupView.setSelectedGroup(arg1);
		return thrGroupView;
		} else {

				GroupViewHolder holder = null;

				if (arg3 == null) {
					arg3 = mInflater.inflate(R.layout.checkitem_thr_item, null);
					holder = new GroupViewHolder();
					holder.NameText = (TextView) arg3
							.findViewById(R.id.pathname);
					holder. image = (ImageView) arg3.findViewById(R.id.history);
					holder.image.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							//添加拉起趋势图的操作
							Intent intent = null;
							View v= (View) arg0.getParent();
							TextView tv= (TextView) v.findViewById(R.id.pathname);
							IDemoChart[] mCharts = new IDemoChart[] {
									 new AverageTemperatureChart()};
						      intent = mCharts[0].execute(context,tv.getText().toString());
						      mActivity.startActivity(intent);
						}
						
					});
					arg3.setTag(holder);
				} else {
					holder = (GroupViewHolder) arg3.getTag();
					
				}
				arg3.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
					//	mChildrenList.get(arg0).get(arg1)
						//拉起测量等数据的检测界面
						//TextView typeT= (TextView) arg0.findViewById(R.id.type);
						//获取测试项的类型
						//int itype = Integer.valueOf(typeT.getText().toString());
						//根据类型选择显示不同的UI，调用界面类似于PartItemActivity:switchFragment函数
//						Intent i = new Intent();
//						mActivity.startActivity(i);
						Toast.makeText(context, "000", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX, 0);
						intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX, 0);
						intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX, 0);
						intent.setClass(mActivity, DeviceItemActivity.class);
						mActivity.startActivity(intent); 
					}
					
				});
				holder.NameText.setText(mChildrenList.get(arg0).get(arg1)
						.getCheckContent());
			return arg3;
		}
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG,"getChildrenCount "+mChildrenList.get(arg0).size());
		return mChildrenList.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mDataList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	boolean bTest =false;
	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		
		
		GroupViewHolder holder =null;
		HashMap<String, String> map = (HashMap<String, String>) mDataList.get(arg0);
		Log.i(TAG, "getGroupView groupPosition = "+arg0);
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.device_list_item, null);
			holder = new GroupViewHolder();
			holder.image = (ImageView) arg2.findViewById(R.id.arrow);
			holder.NameText = (TextView) arg2.findViewById(R.id.pathname);
		//	holder.DeadLineText = (TextView) arg2.findViewById(R.id.deadtime);
		//	holder.StausText = (TextView) arg2.findViewById(R.id.status);
		//	holder.ProcessText = (TextView) arg2.findViewById(R.id.progress);
			arg2.setTag(holder);
		}else{
			holder=(GroupViewHolder) arg2.getTag();
			
		}
		if(arg1){
			holder.image.setBackgroundResource(R.drawable.arrow_ex);
		}else{
			holder.image.setBackgroundResource(R.drawable.arrow);
		}
		holder.NameText.setText(map.get(CommonDef.device_info.NAME).toString());
		//holder.DeadLineText.setText(map.get(CommonDef.device_info.DEADLINE));
		//holder.StausText.setText(map.get(CommonDef.device_info.STATUS));
		//holder.ProcessText.setText(map.get(CommonDef.device_info.PROGRESS));
		//NameText.setTextColor(Color.RED);	
		//NameText = (TextView) arg2.findViewById(R.id.pathname);
		//NameText.setText("我是二级目录");
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
		
		return true;
	}
	
	void InitData() {
		try {
			//mStationItemList = app.getStationList(mrouteIndex);
			mDeviceItemList = app.getDeviceItemList(mStationIndex);
			CheckStatus status = null;
			// int itemindex = mStationIndex;
			int itemindex = 0;
			mDataList.clear();
			for (int i = 0; i < mDeviceItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				status = app.getNodeCount(mDeviceItemList.get(i), 2, 0);
				status.setContext(this.context);
				map.put(CommonDef.device_info.NAME,
						app.getDeviceItemName(mDeviceItemList.get(i)));
				Log.d(TAG,"name is"+
						app.getDeviceItemName(mDeviceItemList.get(i)));
				map.put(CommonDef.device_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.device_info.STATUS, status.getStatus());

				map.put(CommonDef.device_info.PROGRESS, status.mCheckedCount
						+ "/" + status.mSum);
				mDataList.add(map);
				itemindex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG,"mDeviceItemList.size()="+mDeviceItemList.size());
		mChildrenList = new ArrayList<ArrayList<PartItemItem>>();
		for (int i = 0; i < mDeviceItemList.size(); i++) {
			InitChidrenData(mStationIndex, i);
		}
	}


	
private final String TAG="luotest";
	void InitChidrenData(int stationIndex, int itemIndexs) {
		try {

			Object object = mDeviceItemList.get(itemIndexs);
			Log.d(TAG,"InitChidrenData() object is "+object.toString());
			mPartItemList = app.getPartItemDataList(mStationIndex, itemIndexs);
			Log.d(TAG,"InitChidrenData() mPartItemList size is "+mPartItemList.size());
			ArrayList<PartItemItem> childList = new ArrayList<PartItemItem>();
			PartItemItem item =null;
			for (int i = 0; i < mPartItemList.size(); i++) {
				Gson g = new Gson();
				item = g.fromJson(mPartItemList.get(i).toString(), PartItemItem.class);
//				Map<String, String> map = new HashMap<String, String>();
//				Log.d(TAG,"InitChidrenData() PartItemData is "+mPartItemList.get(i).toString());
//				map.put(CommonDef.device_info.NAME,	app.getDeviceItemName(mPartItemList.get(i)));
//				map.put(CommonDef.check_item_info.DATA_TYPE,
//						app.getPartItemCheckUnitName(mDeviceItemList.get(i),
//								CommonDef.partItemData_Index.PARTITEM_DATA_TYPE));

//				map.put(CommonDef.check_item_info.VALUE,
//						app.getPartItemCheckUnitName(
//								mDeviceItemList.get(i),
//								CommonDef.partItemData_Index.PARTITEM_ADDITIONAL_INFO));

//				map.put(CommonDef.check_item_info.DEADLINE,
//						app.getPartItemCheckUnitName(
//								mDeviceItemList.get(i),
//								CommonDef.partItemData_Index.PARTITEM_ADD_END_DATE_20));

			
				childList.add(item);
			}
			mChildrenList.add(childList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
