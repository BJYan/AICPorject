package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.StationActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.fragment.RouteFragment;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RouteNormalListAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private final  String TAG="luotest.RouteNormalListAdapter";
	private List<Map<String, String>> mItemDatas = null;
	private myApplication app = null;
	private Activity mActivity=null;
	public RouteNormalListAdapter(Context context,Activity av) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mItemDatas = new ArrayList<Map<String, String>>();
		app =(myApplication) av.getApplication();
		mActivity =av;
		initListData();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mItemDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GroupViewHolder holder =null;
		final HashMap<String, String> mapItem = (HashMap<String, String>) (mItemDatas.get(arg0));
		if(arg1==null){
			holder = new GroupViewHolder();
		
			arg1 = mInflater.inflate(R.layout.route_list_item, null);
			holder.indexText  = (TextView) arg1.findViewById(R.id.index);
			holder.NameText  = (TextView) arg1.findViewById(R.id.pathname);
			holder.DeadLineText  = (TextView) arg1.findViewById(R.id.deadtime);
			holder.ProcessText  = (TextView) arg1.findViewById(R.id.progress);
			arg2.setTag(holder);
		}else{
			holder=(GroupViewHolder) arg2.getTag();
		}
		holder.indexText.setText(mapItem.get(CommonDef.route_info.INDEX));
		holder.NameText.setText(mapItem.get(CommonDef.route_info.NAME));
		holder.DeadLineText.setText(mapItem.get(CommonDef.route_info.DEADLINE));
		holder.ProcessText.setText(mapItem.get(CommonDef.route_info.PROGRESS));
		    arg1.setOnClickListener(new OnClickListener(){
			int position=arg0;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
		app.gRouteName = mapItem.get(CommonDef.route_info.NAME);
		app.mRouteIndex = position;
		Intent intent = new Intent();
		intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX,
				position);
		app.setCurrentRouteIndex(position);
		intent.putExtra(CommonDef.route_info.NAME,
				(String) mapItem.get(CommonDef.route_info.NAME));
		intent.putExtra(CommonDef.route_info.INDEX,
				(String) mapItem.get(CommonDef.route_info.INDEX));
		intent.setClass(context,
				StationActivity.class);
		mActivity.startActivity(intent);
			}});
				
		return arg1;
	}

	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}
	void initListData() {
		//final int type =types;
		MLog.Logd(TAG,"initListData()>>");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
					MLog.Logd(TAG,
							"in init() 1 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

					int iRouteCount = app.InitData();
					MLog.Logd(TAG,
							"in init() 2 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					CheckStatus status = null;
					mItemDatas.clear();
					for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
						try {
							MLog.Logd(TAG,
									"in init() for start i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
							Map<String, String> map = new HashMap<String, String>();
							status = app.getNodeCount(null, 0, routeIndex);
							status.setContext(context);
							map.put(CommonDef.route_info.NAME,
									app.getRoutName(routeIndex));
							map.put(CommonDef.route_info.DEADLINE,
									status.mLastTime);

							map.put(CommonDef.route_info.PROGRESS,
									status.mCheckedCount + "/" + status.mSum);
						
							String index = "" + (routeIndex + 1);
							map.put(CommonDef.route_info.INDEX, index);

							mItemDatas.add(map);
							MLog.Logd(TAG,
									"in init() for end i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				mHander.sendMessage(mHander.obtainMessage(MSG_UPDATE_LISTVIEW));
				MLog.Logd(TAG,"initListData()<<");
			}
		}).start();

	}
	
	private final int MSG_UPDATE_LISTVIEW =0;
	Handler mHander= new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case MSG_UPDATE_LISTVIEW:
				RouteNormalListAdapter.this.notifyDataSetChanged();
		//	if (mListViewAdapter != null) {
		//		mListViewAdapter.notifyDataSetChanged();
		//	}
			break;
			}
			super.handleMessage(msg);
		}
		
	};

//	@Override
//	public void notifyDataSetChanged() {
//		// TODO Auto-generated method stub
//		initListData();
//		super.notifyDataSetChanged();
//	}
}